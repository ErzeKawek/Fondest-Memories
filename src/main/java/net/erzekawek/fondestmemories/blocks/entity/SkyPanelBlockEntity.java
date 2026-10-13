package net.erzekawek.fondestmemories.blocks.entity;

import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkyPanelBlockEntity extends BlockEntity {
    public SkyPanelBlockEntity(BlockPos pos, BlockState state) {
        super(FondestMemoriesBlockEntityRegistry.SKY_PANEL_NOON, pos, state);
    }
}
