package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.blocks.entity.SkyPanelBlockEntity;
import net.erzekawek.fondestmemories.blocks.entity.SkyWindowBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class FondestMemoriesBlockEntityRegistry {

    public static BlockEntityType<SkyWindowBlockEntity> SKY_WINDOW;
    public static BlockEntityType<SkyPanelBlockEntity> SKY_PANEL_NOON;

    private FondestMemoriesBlockEntityRegistry() {
    }

    public static void initialize() {
        SKY_WINDOW = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, FondestMemories.id("sky_window"),
                new BlockEntityType<>(SkyWindowBlockEntity::new, Set.of(FondestMemoriesBlockRegistry.SKY_WINDOW)));

        SKY_PANEL_NOON = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, FondestMemories.id("sky_panel"),
                new BlockEntityType<>(SkyPanelBlockEntity::new, Set.of(FondestMemoriesBlockRegistry.SKY_PANEL_NOON)));
    }
}
