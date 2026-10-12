package net.erzekawek.fondestmemories.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.erzekawek.fondestmemories.blocks.entity.SkyWindowBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class SkyWindowRenderer extends AbstractEndPortalRenderer<SkyWindowBlockEntity, EndPortalRenderState> {
    @Override
    public EndPortalRenderState createRenderState() {
        return new EndPortalRenderState();
    }

    @Override
    public void submit(EndPortalRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        SkyWindowRendering.ensureRegistered();
        poseStack.pushPose();
        submitCube(state.facesToShow, SkyWindowRendering.SKY_WINDOW, poseStack, collector);
        poseStack.popPose();
    }
}
