package net.erzekawek.fondestmemories.client.skyrendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.misc.SkyVariants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.CubeMapTexture;
import net.minecraft.resources.Identifier;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class SkyWindowRendering {

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

    private static final Map<SkyVariants, RenderType> RENDER_TYPES = new EnumMap<>(SkyVariants.class);
    private static final Set<SkyVariants> LOADED = EnumSet.noneOf(SkyVariants.class);

    private SkyWindowRendering() {
    }

    public static Identifier baseId(SkyVariants variant) {
        return FondestMemories.id("textures/environment/skybox/" + variant.textureName());
    }

    public static RenderType renderType(SkyVariants variant) {
        return RENDER_TYPES.computeIfAbsent(variant, v ->
                RenderType.create("sky_window_" + v.textureName(),
                        RenderSetup.builder(PIPELINE)
                                .withTexture("Sampler0", baseId(v))
                                .createRenderSetup()));
    }


    public static void ensureRegistered() {
        for (SkyVariants v : SkyVariants.values()) {
            if (LOADED.add(v)) {
                Identifier id = baseId(v);
                Minecraft.getInstance().getTextureManager()
                        .registerAndLoad(id, new CubeMapTexture(id));
            }
        }
    }
}
