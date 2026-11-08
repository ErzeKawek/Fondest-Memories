package net.erzekawek.fondestmemories.blocks;

import net.erzekawek.fondestmemories.blocks.entity.SkyPanelBlockEntity;
import net.erzekawek.fondestmemories.misc.SkyVariants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class PanelBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.UP,    Block.box(0, 0, 0, 16, 1, 16),
            Direction.DOWN,  Block.box(0, 15, 0, 16, 16, 16),
            Direction.NORTH, Block.box(0, 0, 15, 16, 16, 16),
            Direction.SOUTH, Block.box(0, 0, 0, 16, 16, 1),
            Direction.WEST,  Block.box(15, 0, 0, 16, 16, 16),
            Direction.EAST,  Block.box(0, 0, 0, 1, 16, 16));


    public PanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return false;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }


}
