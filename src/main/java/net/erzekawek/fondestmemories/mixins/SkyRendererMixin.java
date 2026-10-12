package net.erzekawek.fondestmemories.mixins;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import net.erzekawek.fondestmemories.client.NewSkyboxRenderer;
import net.erzekawek.fondestmemories.registry.FondestMemoriesDimensionRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void fondest_memories$prepare(GpuBufferSlice skyFog, SkyRenderState state, CallbackInfo ci) {
        if (NewSkyboxRenderer.isActive()) {
            NewSkyboxRenderer.prepare();
        }
    }

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    private void fondest_memories$poolSky(RenderPass renderPass, CallbackInfo ci) {
        if (NewSkyboxRenderer.isActive()) {
            NewSkyboxRenderer.render(renderPass);
            ci.cancel();
        }
    }
}
