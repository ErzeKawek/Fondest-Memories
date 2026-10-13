package net.erzekawek.fondestmemories.client.skyrendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.erzekawek.fondestmemories.blocks.SkyPanelBlock;
import net.erzekawek.fondestmemories.blocks.entity.SkyPanelBlockEntity;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class SkyPanelRenderer implements BlockEntityRenderer<SkyPanelBlockEntity, SkyPanelRenderState> {
    private static final float T = 1.0F / 16.0F;

    @Override
    public SkyPanelRenderState createRenderState() {
        return new SkyPanelRenderState();
    }

    @Override
    public void extractRenderState(SkyPanelBlockEntity be, SkyPanelRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        state.facing = be.getBlockState().getValue(SkyPanelBlock.FACING);
    }

    @Override
    public void submit(SkyPanelRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        SkyWindowRendering.ensureRegistered();
        final Direction facing = state.facing;

        final Vector3f from = new Vector3f(0, 0, 0);
        final Vector3f to = new Vector3f(1, 1, 1);
        switch (facing) {
            case UP -> to.y = T;
            case DOWN -> from.y = 1 - T;
            case NORTH -> from.z = 1 - T;
            case SOUTH -> to.z = T;
            case WEST -> from.x = 1 - T;
            case EAST -> to.x = T;
        }

        collector.submitCustomGeometry(poseStack, SkyWindowRendering.SKY_WINDOW, (pose, buffer) -> {
            for (Direction d : Direction.values()) {
                if (d == facing.getOpposite()) continue; // the back is against the support block
                FaceInfo info = FaceInfo.fromFacing(d);
                for (int i = 0; i < 4; i++) {
                    buffer.addVertex(pose, info.getVertexInfo(i).select(from, to));
                }
            }
        });
    }
}
