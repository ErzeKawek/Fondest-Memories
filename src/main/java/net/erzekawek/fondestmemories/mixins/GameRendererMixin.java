package net.erzekawek.fondestmemories.mixins;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.misc.InterspaceUtil;
import net.erzekawek.fondestmemories.registry.FondestMemoriesPostEffectRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void fm$addDimensionEffect(DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!FondestMemories.CONFIG.enablePostEffects()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && InterspaceUtil.FMLVL.contains(mc.level.dimension())) {
            ((GameRenderer) (Object) this).getRequestedPostEffects().add(FondestMemoriesPostEffectRegistry.VHS);
        }
    }
}
