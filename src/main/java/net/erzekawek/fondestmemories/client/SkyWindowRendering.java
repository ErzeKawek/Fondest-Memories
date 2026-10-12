package net.erzekawek.fondestmemories.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.erzekawek.fondestmemories.FondestMemories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.CubeMapTexture;
import net.minecraft.resources.Identifier;

public final class SkyWindowRendering {
    public static final Identifier SKY = FondestMemories.id("textures/environment/skybox/siang");

    public static final RenderPipeline PIPELINE =
            RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.GLOBALS_SNIPPET})
                    .withLocation("pipeline/sky_window")
                    .withBindGroupLayout(BindGroupLayouts.PROJECTION)
                    .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
                    .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
                    .withVertexShader(FondestMemories.id("core/sky_window"))
                    .withFragmentShader(FondestMemories.id("core/sky_window"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION)
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
                    .withColorTargetState(ColorTargetState.DEFAULT)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .withCull(false)
                    .build();

    public static final RenderType SKY_WINDOW = RenderType.create("sky_window",
            RenderSetup.builder(PIPELINE)
                    .withTexture("Sampler0", SKY)
                    .createRenderSetup());

    private static boolean registered;

    /** Registers the cubemap texture once. Call before the first draw. */
    public static void ensureRegistered() {
        if (!registered) {
            Minecraft.getInstance().getTextureManager()
                    .registerAndLoad(SKY, new CubeMapTexture(SKY));
            registered = true;
        }
    }

    private SkyWindowRendering() {
    }
}
