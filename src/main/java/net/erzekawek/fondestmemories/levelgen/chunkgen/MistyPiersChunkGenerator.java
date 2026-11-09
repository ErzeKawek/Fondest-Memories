package net.erzekawek.fondestmemories.levelgen.chunkgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * "Misty piers": an endless, perfectly still sheet of water with rows of weathered posts standing in it.
 * Pair it with a biome that has dense light-grey fog (data/.../worldgen/biome/misty_piers.json).
 * Everything is a pure function of (seed, position), so it is endless and chunks always line up.
 */
public final class MistyPiersChunkGenerator extends ChunkGenerator {

    private static Identifier mc(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    public static final MapCodec<MistyPiersChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(MistyPiersChunkGenerator::getBiomeSource),
            Codec.LONG.optionalFieldOf("seed", 0L).forGetter(g -> g.seed),
            Codec.INT.optionalFieldOf("water_level", 62).forGetter(g -> g.waterLevel),
            Codec.intRange(2, 300).optionalFieldOf("depth", 24).forGetter(g -> g.depth),
            Identifier.CODEC.optionalFieldOf("floor_block", mc("stone")).forGetter(g -> g.floorId),
            Identifier.CODEC.optionalFieldOf("post_block", mc("dark_oak_log")).forGetter(g -> g.postId),
            Identifier.CODEC.optionalFieldOf("cap_block", mc("white_carpet")).forGetter(g -> g.capId),
            Identifier.CODEC.optionalFieldOf("bar_block", mc("dark_oak_slab")).forGetter(g -> g.barId),
            Codec.intRange(8, 512).optionalFieldOf("line_spacing", 40).forGetter(g -> g.lineSpacing),
            Codec.intRange(3, 64).optionalFieldOf("post_spacing", 7).forGetter(g -> g.postSpacing),
            Codec.floatRange(0f, 1f).optionalFieldOf("post_chance", 0.7f).forGetter(g -> g.postChance),
            Codec.floatRange(0f, 1f).optionalFieldOf("scatter_chance", 0.12f).forGetter(g -> g.scatterChance),
            Codec.intRange(1, 64).optionalFieldOf("min_height", 3).forGetter(g -> g.minHeight),
            Codec.intRange(1, 64).optionalFieldOf("max_height", 9).forGetter(g -> g.maxHeight),
            Codec.floatRange(0f, 1f).optionalFieldOf("bar_chance", 0.12f).forGetter(g -> g.barChance)
    ).apply(i, MistyPiersChunkGenerator::new));

    private final long seed;
    private final int waterLevel, depth, lineSpacing, postSpacing, minHeight, maxHeight;
    private final Identifier floorId, postId, capId, barId;
    private final float postChance, scatterChance, barChance;
    private volatile Resolved resolved;

    private record Resolved(BlockState floor, BlockState post, BlockState cap, BlockState bar) {

    }
    private record Post(int x, int z, int top, boolean bar, boolean barAlongX) {

    }

    public MistyPiersChunkGenerator(
            BiomeSource biomes,
            long seed,
            int waterLevel,
            int depth,
            Identifier floorId,
            Identifier postId,
            Identifier capId,
            Identifier barId,
            int lineSpacing,
            int postSpacing,
            float postChance,
            float scatterChance,
            int minHeight, int maxHeight, float barChance
    ) {
        super(biomes);
        this.seed = seed;
        this.waterLevel = waterLevel;
        this.depth = depth;
        this.floorId = floorId;
        this.postId = postId;
        this.capId = capId;
        this.barId = barId;
        this.lineSpacing = lineSpacing;
        this.postSpacing = postSpacing;
        this.postChance = postChance;
        this.scatterChance = scatterChance;
        this.minHeight = Math.min(minHeight, maxHeight);
        this.maxHeight = Math.max(minHeight, maxHeight);
        this.barChance = barChance;
    }

    private static BlockState state(Identifier id) {
        return BuiltInRegistries.BLOCK.getOptional(id)
                .orElseThrow(() -> new IllegalStateException("[FM] unknown block id: " + id))
                .defaultBlockState();
    }

    private Resolved res() {
        Resolved r = resolved;
        if (r == null) resolved = r = new Resolved(state(floorId), state(postId), state(capId), state(barId));
        return r;
    }

    @Override protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> buildTerrain(ChunkAccess chunk, Blender blender, RandomState randomState, StructureManager structureManager, BiomeManager biomeManager, @Nullable WorldGenRegion carverBiomeRegion, Set<Holder<Biome>> possibleBiomes) {
        generate(chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    public void generate(ChunkAccess chunk) {
        Resolved r = res();
        ChunkPos cp = chunk.getPos();
        int minX = cp.getMinBlockX(), minZ = cp.getMinBlockZ(), maxX = minX + 15, maxZ = minZ + 15;
        int minY = chunk.getMinY(), maxY = chunk.getMaxY();
        int floorY = Math.max(minY + 1, waterLevel - depth);
        int top = Math.min(waterLevel, maxY);
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        // 1) floor + perfectly still water (source blocks only)
        for (int x = minX; x <= maxX; x++)
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= Math.min(floorY, maxY); y++)
                    chunk.setBlockState(pos.set(x, y, z), y == minY ? bedrock : r.floor(), 0);
                for (int y = floorY + 1; y <= top; y++)
                    chunk.setBlockState(pos.set(x, y, z), water, 0);
            }

        // 2) posts overlapping this chunk (+3 border so crossbars at the chunk edge are complete)
        forEachPost(minX - 3, maxX + 3, minZ - 3, maxZ + 3,
                p -> placePost(chunk, r, p, floorY, minX, minZ, maxX, maxZ, minY, maxY));
    }

    private void placePost(ChunkAccess chunk, Resolved r, Post p, int floorY,
                           int minX, int minZ, int maxX, int maxZ, int minY, int maxY) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        if (p.x() >= minX && p.x() <= maxX && p.z() >= minZ && p.z() <= maxZ) {
            for (int y = floorY + 1; y <= p.top() && y <= maxY; y++)
                chunk.setBlockState(pos.set(p.x(), y, p.z()), r.post(), 0);
            if (p.top() + 1 <= maxY) chunk.setBlockState(pos.set(p.x(), p.top() + 1, p.z()), r.cap(), 0);
        }
        if (p.bar()) {
            int y = p.top() - 1;
            if (y < minY || y > maxY) return;
            for (int d = -1; d <= 1; d += 2) {
                int bx = p.barAlongX() ? p.x() + d : p.x();
                int bz = p.barAlongX() ? p.z() : p.z() + d;
                if (bx >= minX && bx <= maxX && bz >= minZ && bz <= maxZ)
                    chunk.setBlockState(pos.set(bx, y, bz), r.bar(), 0);
            }
        }
    }

    /** Deterministic layout: broken rows ("old pier lines") plus a few lone posts. */
    private void forEachPost(int minX, int maxX, int minZ, int maxZ, Consumer<Post> out) {
        int ls = lineSpacing, ps = postSpacing;

        // rows running along Z; some rows don't exist at all, some posts have "rotted away"
        for (int k = Math.floorDiv(minX - ls / 4 - 3, ls); k <= Math.floorDiv(maxX + 3, ls); k++) {
            if (unit(mix(seed, k, 1, 11)) > 0.65) continue;
            int lineX = k * ls + (int) (unit(mix(seed, k, 2, 12)) * (ls / 4));
            if (lineX < minX - 3 || lineX > maxX + 3) continue;
            for (int j = Math.floorDiv(minZ - 3, ps); j <= Math.floorDiv(maxZ + 3, ps); j++) {
                long h = mix(seed, k, j, 13);
                if (unit(h) >= postChance) continue;
                int z = j * ps + (int) (unit(mix(seed, k, j, 14)) * 3) - 1;
                int x = lineX + (int) (unit(mix(seed, k, j, 15)) * 3) - 1;
                out.accept(makePost(x, z, h));
            }
        }

        // lone posts
        int cs = 24;
        for (int cx = Math.floorDiv(minX - 3, cs); cx <= Math.floorDiv(maxX + 3, cs); cx++)
            for (int cz = Math.floorDiv(minZ - 3, cs); cz <= Math.floorDiv(maxZ + 3, cs); cz++) {
                long h = mix(seed, cx, cz, 21);
                if (unit(h) >= scatterChance) continue;
                int x = cx * cs + (int) (unit(mix(seed, cx, cz, 22)) * cs);
                int z = cz * cs + (int) (unit(mix(seed, cx, cz, 23)) * cs);
                out.accept(makePost(x, z, h));
            }
    }

    private Post makePost(int x, int z, long h) {
        int height = minHeight + (int) (unit(mix(h, 1, 2, 3)) * (maxHeight - minHeight + 1));
        boolean bar = unit(mix(h, 4, 5, 6)) < barChance;
        boolean alongX = (mix(h, 7, 8, 9) & 1L) == 0L;
        return new Post(x, z, waterLevel + height, bar, alongX);
    }

    private static double unit(long h) {
        return (h >>> 11) * 0x1.0p-53;
    }

    private static long mix(long a, long b, long c, long d) {
        long h = a * 0x9E3779B97F4A7C15L ^ b * 0xC2B2AE3D27D4EB4FL ^ c * 0x165667B19E3779F9L ^ d * 0x27D4EB2F165667C5L;
        h ^= h >>> 33; h *= 0xff51afd7ed558ccdL; h ^= h >>> 33; h *= 0xc4ceb9fe1a85ec53L; h ^= h >>> 33;
        return h;
    }

    @Override public void spawnOriginalMobs(WorldGenRegion region) {

    }
    @Override public int getGenDepth() {
        return 384;
    }
    @Override public int getSeaLevel() {
        return waterLevel;
    }
    @Override public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState randomState) {
        return waterLevel + 1;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        BlockState[] air = new BlockState[level.getHeight()];
        Arrays.fill(air, Blocks.AIR.defaultBlockState());
        return new NoiseColumn(level.getMinY(), air);
    }

    @Override
    public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos, SamplerContext ctx) {
        result.add("Misty piers, water level " + waterLevel);
    }
}
