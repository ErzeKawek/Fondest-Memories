package net.erzekawek.fondestmemories.registry;

import com.google.common.collect.Maps;
import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.blocks.*;
import net.erzekawek.fondestmemories.misc.SkyVariants;
import net.erzekawek.veyl.types.SimpleMaterial;
import net.erzekawek.veyl.types.VeylStorage;
import net.erzekawek.veyl.types.VeylWood;
import net.erzekawek.veyl.utils.registry.CBlock;
import net.erzekawek.veyl.utils.registry.UnifiedRegistry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.erzekawek.fondestmemories.FondestMemories.MOD_ID;

public class FondestMemoriesBlockRegistry {

    // liminal unbreakable blocks
    public static Block REAL_GATE = CBlock.makeUnbreakable(MOD_ID, "real_gate", 15);
    public static Block FALSE_GATE = CBlock.makeUnbreakable(MOD_ID, "false_gate", 15);
    public static Block POOL_TILING = CBlock.makeUnbreakable(MOD_ID, "pool_tiling", 0);


    public static final Map<DyeColor, Block> PASS_CONCRETE = Maps.newEnumMap(DyeColor.class);

    static {

        for (int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.byId(i);
            Block rorialcolored = UnifiedRegistry.registerBlock(
                    MOD_ID,
                    color.getName() + "_passable_concrete",
                    settings -> new Block(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.CONCRETE.white())
                                    .noCollision()
                                    .noLootTable()
                                    .replaceable()
                                    .setId(settings.blockId()))
            );
            PASS_CONCRETE.put(color, rorialcolored);
        }
    }

    // poolrooms blocks
    public static final Tiles POOL_TILE = new Tiles("pool_tile", MapColor.TERRACOTTA_WHITE);

    public static Block SKY_WINDOW = registerWindow("sky_window", 15, SkyVariants.DAY);
    public static Block SKY_PANEL_NOON = registerPanel("sky_panel_noon", 15, SkyVariants.DAY);
    public static Block SKY_PANEL_NOON_LIGHTLESS = registerPanel("sky_panel_noon_lightless", 0, SkyVariants.DAY);

    public static Block SKY_WINDOW_DUSK = registerWindow("sky_window_dusk", 15, SkyVariants.DUSK);
    public static Block SKY_PANEL_DUSK = registerPanel("sky_panel_dusk", 15, SkyVariants.DUSK);
    public static Block SKY_PANEL_DUSK_LIGHTLESS = registerPanel("sky_panel_dusk_lightless", 0, SkyVariants.DUSK);

    // utility blocks

    public static Block LOCKABLE_METAL_DOOR = registerKeyedDoor("lockable_metal_door");
    public static Block OFFICE_DOOR = UnifiedRegistry.registerBlock(MOD_ID, "office_door", settings -> new DoorBlock(BlockSetType.OAK, applyWoodSettings(settings)));

    public static VeylStorage.Entry SMALL_CRATE = VeylStorage.register(
            FondestMemories.id("small_crate"), 9,
            settings -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)
                    .setId(settings.blockId())
    );

    // rift blocks

    public static Block POOL_RIFT = CBlock.makeRift(MOD_ID, "poolrooms_rift", 15, FondestMemoriesDimensionRegistry.FRONTIERS, 60, 90, 60, 0f);
    public static Block HIGHWAY_RIFT = CBlock.makeRift(MOD_ID, "highway_rift", 15, FondestMemoriesDimensionRegistry.ENDLESS_HIGHWAY, 5, 3, 0, 0f);
    public static Block LIGHTS_OUT_RIFT = CBlock.makeRift(MOD_ID, "lights_out_rift", 0, FondestMemoriesDimensionRegistry.CARDINAL_LEVEL, 47, 75, 47, 0f);
    public static Block FIELD_RIFT = CBlock.makeRift(MOD_ID, "field_rift", 15, FondestMemoriesDimensionRegistry.BARNFIELDS, 0, 3, 0, 0f);
    public static Block OCEANIC_RIFT = CBlock.makeRift(MOD_ID, "oceanic_rift", 0, FondestMemoriesDimensionRegistry.THALASSOPHOBIA, 0, 896, 0, 0f);
    public static Block PIERS_RIFT = CBlock.makeRift(MOD_ID, "piers_rift", 15, FondestMemoriesDimensionRegistry.MISTY_PIERS, 0, 64, 0, 0f);
    public static Block LIMINAL_GLASS = CBlock.makeLight(MOD_ID, "liminal_glass", 15);

    public static Block CORN_PLANT = CBlock.makeDoublePlant(MOD_ID,"corn_stalk", FondestMemoriesTagsRegistry.CORN_PLACEABLE_ON);

    private static Block registerPanel(String name, int light, SkyVariants variant) {
        return UnifiedRegistry.registerBlock(MOD_ID, name, settings -> new SkyPanelBlock(
                BlockBehaviour.Properties.of()
                        .strength(-1, 3600000)
                        .sound(SoundType.GLASS)
                        .lightLevel(blockState -> light)
                        .noLootTable()
                        .noOcclusion()
                        .setId(settings.blockId()),
                variant));
    }

    private static Block registerWindow(String name, int light, SkyVariants variant) {
        return UnifiedRegistry.registerBlock(MOD_ID, name, settings -> new SkyWindowBlock(
                BlockBehaviour.Properties.of()
                .strength(-1, 3600000)
                .sound(SoundType.GLASS)
                .lightLevel(blockState -> light)
                .noLootTable()
                .noOcclusion()
                .setId(settings.blockId()),
                variant));
    }

    private static Block registerKeyedDoor(String name) {
        return UnifiedRegistry.registerBlock(MOD_ID, name, settings -> new LockableDoorBlock(BlockBehaviour.Properties.of()
                .strength(50, 1200)
                .sound(SoundType.NETHERITE_BLOCK)
                .pushReaction(PushReaction.IMMOVEABLE)
                .noOcclusion()
                .setId(settings.blockId())));
    }

    public static BlockBehaviour.Properties applyWoodSettings(BlockBehaviour.Properties settings) {
        return settings
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .sound(SoundType.WOOD)
                .strength(2.0F, 3.0F)
                .ignitedByLava();
    }

    public static class Tiles {

        public static final List<Tiles> ALL = new ArrayList<>();

        public final String baseName;
        public final Block tiles;
        public final Block tileStairs;
        public final Block tileSlab;
        public final Block tileWall;
        public final List<Block> blocks;
        private final MapColor mapColor;


        public Tiles(String name, MapColor mapColor) {
            this.baseName = name;
            this.mapColor = mapColor;

            tiles = UnifiedRegistry.registerBlock(MOD_ID, baseName, settings -> new Block(applySettings(settings)));
            tileStairs = UnifiedRegistry.registerBlock(MOD_ID, baseName + "_stairs",
                    settings -> new StairBlock(tiles.defaultBlockState(), applySettings(settings)));
            tileSlab = UnifiedRegistry.registerBlock(MOD_ID, baseName + "_slab",
                    settings -> new SlabBlock(applySettings(settings)));
            tileWall = UnifiedRegistry.registerBlock(MOD_ID, baseName + "_wall",
                    settings -> new WallBlock(applySettings(settings)));

            blocks = Arrays.asList(tiles, tileStairs, tileSlab, tileWall);
            ALL.add(this);
        }

        public BlockBehaviour.Properties applySettings(BlockBehaviour.Properties settings) {
            return settings
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(-1, 3600000)
                    .sound(SoundType.GLASS)
                    .noLootTable()
                    .mapColor(this.mapColor);
        }
    }

    public static void initialize() {
    }
}
