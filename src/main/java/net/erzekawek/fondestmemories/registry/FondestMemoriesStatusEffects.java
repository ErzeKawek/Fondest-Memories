package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.effects.StunEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class FondestMemoriesStatusEffects {

    public static final Holder<MobEffect> STUNNED = registerEffect("stunned", new StunEffect());

    public static <E extends MobEffect> Holder<MobEffect> registerEffect(String name, E effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, FondestMemories.id(name), effect);
    }

    public static void initialize() {

    }

}
