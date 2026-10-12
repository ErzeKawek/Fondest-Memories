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
import net.erzekawek.fondestmemories.registry.FondestMemoriesDimensionRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Matrix4f;

public class NewSkyboxRenderer {
    private static GpuBuffer cube;


    public static boolean isActive() {
        var level = Minecraft.getInstance().level;
        return level != null && level.dimension().equals(FondestMemoriesDimensionRegistry.FRONTIERS);
    }


    public static void prepare() {
        SkyWindowRendering.ensureRegistered();
        if (cube == null) {
            cube = buildCube();
        }
    }

    private static GpuBuffer buildCube() {
        try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(
                24 * DefaultVertexFormat.POSITION.getVertexSize())) {
            BufferBuilder b = new BufferBuilder(bytes, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
            for (int i = 0; i < 6; i++) {
                Matrix4f pose = new Matrix4f();
                switch (i) {
                    case 1 -> pose.rotationX((float) Math.PI / 2F);
                    case 2 -> pose.rotationX(-(float) Math.PI / 2F);
                    case 3 -> pose.rotationX((float) Math.PI);
                    case 4 -> pose.rotationZ((float) Math.PI / 2F);
                    case 5 -> pose.rotationZ(-(float) Math.PI / 2F);
                }
                b.addVertex(pose, -100F, -100F, -100F);
                b.addVertex(pose, -100F, -100F,  100F);
                b.addVertex(pose,  100F, -100F,  100F);
                b.addVertex(pose,  100F, -100F, -100F);
            }
            try (MeshData mesh = b.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(
                        () -> "Custom pool skybox cube", 32, mesh.vertexBuffer());
            }
        }
    }

    public static void render(final RenderPass pass) {
        if (cube == null) {
            return;
        }

        RenderSystem.AutoStorageIndexBuffer auto = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer indices = auto.getBuffer(36);
        GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrixCopy());
        AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(SkyWindowRendering.SKY);

        pass.pushDebugGroup(() -> "Custom pool skybox");
        pass.setPipeline(RenderSystem.getCompiledPipeline(SkyWindowRendering.PIPELINE));
        RenderSystem.bindDefaultUniforms(pass);
        pass.setUniform("DynamicTransforms", transforms);
        pass.setUniform("Sampler0", tex.getTextureView(), tex.getSampler());
        pass.setVertexBuffer(0, cube.slice());
        pass.setIndexBuffer(indices, auto.type());
        pass.drawIndexed(36, 1, 0, 0, 0);
        pass.popDebugGroup();
    }
}
