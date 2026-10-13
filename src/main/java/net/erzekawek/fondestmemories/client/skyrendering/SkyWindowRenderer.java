package net.erzekawek.fondestmemories.client.skyrendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.erzekawek.fondestmemories.blocks.entity.SkyWindowBlockEntity;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;
import java.util.Map;

public class SkyWindowRenderer implements BlockEntityRenderer<SkyWindowBlockEntity, SkyWindowRenderState> {
    private static final Vector3fc FROM = new Vector3f(0, 0, 0);
    private static final Vector3fc TO = new Vector3f(1, 1, 1);
    private static final Map<Direction, List<Vector3fc>> FACES = Util.makeEnumMap(Direction.class, d -> {
        FaceInfo info = FaceInfo.fromFacing(d);
        return List.of(info.getVertexInfo(0).select(FROM, TO), info.getVertexInfo(1).select(FROM, TO),
                info.getVertexInfo(2).select(FROM, TO), info.getVertexInfo(3).select(FROM, TO));
    });

    @Override
    public SkyWindowRenderState createRenderState() {
        return new SkyWindowRenderState();
    }

    @Override
    public void extractRenderState(SkyWindowBlockEntity be, SkyWindowRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        state.facesToShow.clear();
        for (Direction d : Direction.values()) {
            if (be.shouldRenderFace(d)) state.facesToShow.add(d);
        }
    }

    @Override
    public void submit(SkyWindowRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        SkyWindowRendering.ensureRegistered();
        if (state.facesToShow.isEmpty()) return;
        collector.submitCustomGeometry(poseStack, SkyWindowRendering.SKY_WINDOW, (pose, buffer) -> {
            for (Direction d : state.facesToShow) {
                for (Vector3fc v : FACES.get(d)) buffer.addVertex(pose, v);
            }
        });
    }
}
