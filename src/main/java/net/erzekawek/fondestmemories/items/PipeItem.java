package net.erzekawek.fondestmemories.items;

import net.erzekawek.fondestmemories.registry.FondestMemoriesSFX;
import net.erzekawek.fondestmemories.registry.FondestMemoriesStatusEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PipeItem extends Item {

    public PipeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.playSound(FondestMemoriesSFX.PIPE_HIT);
        super.postHurtEnemy(stack, target, attacker);
    }
}
