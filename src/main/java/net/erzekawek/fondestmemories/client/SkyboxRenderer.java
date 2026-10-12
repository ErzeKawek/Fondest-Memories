package net.erzekawek.fondestmemories.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import net.erzekawek.fondestmemories.FondestMemories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public class SkyboxRenderer {

    private static final String[] NAMES = {"down", "north", "south", "up", "east", "west"};
    private static final Identifier[] TEXTURES = new Identifier[6];
    private static final GpuBuffer[] BUFFERS = new GpuBuffer[6];
    private static final float E = 0.0005F;

    static {
        for (int i = 0; i < 6; i++) {
            TEXTURES[i] = FondestMemories.id("textures/environment/skybox/" + NAMES[i] + ".png");
        }
    }

    private static GpuBuffer buildFace(final int i) {
        Matrix4f pose = new Matrix4f();
        switch (i) {
            case 1 -> pose.rotationX((float) Math.PI / 2F);
            case 2 -> pose.rotationX(-(float) Math.PI / 2F);
            case 3 -> pose.rotationX((float) Math.PI);
            case 4 -> pose.rotationZ((float) Math.PI / 2F);
            case 5 -> pose.rotationZ(-(float) Math.PI / 2F);
        }
        try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(
                4 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize())) {
            BufferBuilder b = new BufferBuilder(bytes, PrimitiveTopology.QUADS,
                    DefaultVertexFormat.POSITION_TEX_COLOR);
            b.addVertex(pose, -100F, -100F, -100F).setUv(E, E).setColor(-1);
            b.addVertex(pose, -100F, -100F,  100F).setUv(E, 1 - E).setColor(-1);
            b.addVertex(pose,  100F, -100F,  100F).setUv(1 - E, 1 - E).setColor(-1);
            b.addVertex(pose,  100F, -100F, -100F).setUv(1 - E, E).setColor(-1);
            try (MeshData mesh = b.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(
                        () -> "Pool Skybox " + NAMES[i], 40, mesh.vertexBuffer());
            }
        }
    }

    public static void render(final RenderPass pass) {
        for (int i = 0; i < 6; i++) {
            if (BUFFERS[i] == null) BUFFERS[i] = buildFace(i);
        }
        TextureManager textures = Minecraft.getInstance().getTextureManager();
        RenderSystem.AutoStorageIndexBuffer auto = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer indices = auto.getBuffer(6);
        GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrixCopy());

        pass.pushDebugGroup(() -> "Pool Skybox");
        pass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.END_SKY));
        RenderSystem.bindDefaultUniforms(pass);
        pass.setUniform("DynamicTransforms", transforms);
        for (int i = 0; i < 6; i++) {
            AbstractTexture tex = textures.getTexture(TEXTURES[i]);
            pass.setUniform("Sampler0", tex.getTextureView(), tex.getSampler());
            pass.setVertexBuffer(0, BUFFERS[i].slice());
            pass.setIndexBuffer(indices, auto.type());
            pass.drawIndexed(6, 1, 0, 0, 0);
        }
        pass.popDebugGroup();
    }
}
