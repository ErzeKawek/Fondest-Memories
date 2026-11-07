package net.erzekawek.fondestmemories.mixins;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.misc.InterspaceUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Unique
    private static final Identifier FM_EFFECT =
            FondestMemories.id("vhs");

    @Inject(method = "update", at = @At("TAIL"))
    private void fm$addDimensionEffect(DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && InterspaceUtil.FMLVL.contains(mc.level.dimension())) {
            ((GameRenderer) (Object) this).getRequestedPostEffects().add(FM_EFFECT);
        }
    }
}
