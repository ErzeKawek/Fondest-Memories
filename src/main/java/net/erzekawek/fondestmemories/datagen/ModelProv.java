package net.erzekawek.fondestmemories.datagen;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.blocks.PanelBlock;
import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockRegistry;
import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockRegistry.Tiles;
import net.erzekawek.fondestmemories.registry.FondestMemoriesItemRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModelProv extends FabricModelProvider {

    protected ModelProv(FabricPackOutput output) {
        super(output);
    }

    private static final ModelTemplate PANEL_TEMPLATE = new ModelTemplate(
            Optional.of(FondestMemories.id("block/template_panel")),
            Optional.empty(),
            TextureSlot.TEXTURE);

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.REAL_GATE);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.FALSE_GATE);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.POOL_TILING);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.HIGHWAY_RIFT);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.POOL_RIFT);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.LIGHTS_OUT_RIFT);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.FIELD_RIFT);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.OCEANIC_RIFT);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.PIERS_RIFT);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.MOIST_WOOL_BLOCK);
        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.PIEDENE_BLOCK);

        blockModelGenerator.createAxisAlignedPillarBlock(FondestMemoriesBlockRegistry.PIEDENE_WALL, TexturedModel.COLUMN);

        blockModelGenerator.createDoor(FondestMemoriesBlockRegistry.LOCKABLE_METAL_DOOR);

        blockModelGenerator.createDoor(FondestMemoriesBlockRegistry.OFFICE_DOOR);

        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.LIMINAL_GLASS);

        blockModelGenerator.createDoublePlant(FondestMemoriesBlockRegistry.CORN_PLANT,
                BlockModelGenerators.PlantType.NOT_TINTED);

        generateTileModels(blockModelGenerator, FondestMemoriesBlockRegistry.POOL_TILE);

        blockModelGenerator.createTrivialCube(FondestMemoriesBlockRegistry.SMALL_CRATE.block());

        FondestMemoriesBlockRegistry.PASS_CONCRETE.forEach((dyeColor, block) ->
                blockModelGenerator.createTrivialCube(block)
        );


        for (Block panel : List.of(
                FondestMemoriesBlockRegistry.CEILING_TILES,
                FondestMemoriesBlockRegistry.CEILING_LIGHT_W,
                FondestMemoriesBlockRegistry.CEILING_LIGHT_YL
        )) {
            generatePanel(blockModelGenerator, panel);
        }
    }

    private static void generatePanel(BlockModelGenerators gen, Block block) {
        TextureMapping mapping = new TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(block));
        Identifier model = PANEL_TEMPLATE.create(block, mapping, gen.modelOutput);

        gen.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
                        .with(PropertyDispatch.modify(PanelBlock.FACING)
                                .select(Direction.UP, BlockModelGenerators.NOP)
                                .select(Direction.DOWN, BlockModelGenerators.X_ROT_180)
                                .select(Direction.NORTH, BlockModelGenerators.X_ROT_90)
                                .select(Direction.EAST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))
                                .select(Direction.SOUTH, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_180))
                                .select(Direction.WEST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270))
                        ));

        gen.registerSimpleItemModel(block, model);
    }

    public static void generateTileModels(BlockModelGenerators blockModelGenerator, Tiles tiles) {
        blockModelGenerator.family(tiles.tiles).stairs(tiles.tileStairs)
                .slab(tiles.tileSlab).wall(tiles.tileWall);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (Item item : Arrays.asList(
                FondestMemoriesItemRegistry.MASTER_KEY,
                FondestMemoriesItemRegistry.REGULAR_KEY,
                FondestMemoriesItemRegistry.PAN,
                FondestMemoriesBlockRegistry.CORN_PLANT.asItem(),
                FondestMemoriesItemRegistry.NULL_EGG,
                FondestMemoriesItemRegistry.METAL_PIPE,
                FondestMemoriesItemRegistry.HOLY_TALISMAN
        )) {
            itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
    }
}
