package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class FondestMemoriesSFX {

    public static final Holder<SoundEvent> SPACES_AMBIENCE = registerReference(
            "ambient.spaces.loop");
    public static final Holder<SoundEvent> LIM_MUSIC = registerReference("music.liminal");

    public static SoundEvent register(String name) {
        Identifier id = FondestMemories.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static Holder.Reference<SoundEvent> registerReference(String name) {
        Identifier id = FondestMemories.id(name);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
    }

}
