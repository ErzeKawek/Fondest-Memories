package net.erzekawek.fondestmemories.items;

import net.erzekawek.fondestmemories.registry.FondestMemoriesSFX;
import net.erzekawek.fondestmemories.registry.FondestMemoriesStatusEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.*;
import net.minecraft.world.item.ItemStack;

public class Pan extends Item {

    private static final int STUN_TICKS = 100;   // 1 second

    public Pan(Properties properties) {
        super(properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.playSound(FondestMemoriesSFX.PAN_HIT);
        if (!target.hasEffect(FondestMemoriesStatusEffects.STUNNED)) {
            target.addEffect(new MobEffectInstance(FondestMemoriesStatusEffects.STUNNED, STUN_TICKS, 0), attacker);
        }
        super.postHurtEnemy(stack, target, attacker);
    }

}
