package net.erzekawek.fondestmemories.blocks.entity;

import net.erzekawek.fondestmemories.blocks.SkyWindowBlock;
import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkyWindowBlockEntity extends TheEndPortalBlockEntity {
    public SkyWindowBlockEntity(BlockPos pos, BlockState state) {
        super(FondestMemoriesBlockEntityRegistry.SKY_WINDOW, pos, state);
    }

    /** Draw every face, except those touching another sky window (so walls connect cleanly). */
    @Override
    public boolean shouldRenderFace(Direction direction) {
        Level level = this.getLevel();
        if (level == null) {
            return true;
        }
        return !(level.getBlockState(this.getBlockPos().relative(direction)).getBlock() instanceof SkyWindowBlock);
    }
}
