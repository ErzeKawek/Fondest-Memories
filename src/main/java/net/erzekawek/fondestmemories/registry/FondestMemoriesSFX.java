package net.erzekawek.fondestmemories.registry;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

import net.erzekawek.veyl.utils.registry.UnifiedRegistry;

import static net.erzekawek.fondestmemories.FondestMemories.MOD_ID;

public class FondestMemoriesSFX {

    public static final SoundEvent STALKER_SPOT = UnifiedRegistry.registerSound(MOD_ID, "entity.stalker.spot");
    public static final SoundEvent PIPE_HIT = UnifiedRegistry.registerSound(MOD_ID, "item.pipe.hit");
    public static final SoundEvent PAN_HIT = UnifiedRegistry.registerSound(MOD_ID, "item.pan.hit");

    public static final Holder<SoundEvent> SPACES_AMBIENCE = UnifiedRegistry.registerReference(MOD_ID, "ambient.spaces.loop");
    public static final Holder<SoundEvent> LIM_MUSIC = UnifiedRegistry.registerReference(MOD_ID, "music.liminal");

    public static void initialize() {
    }

}
