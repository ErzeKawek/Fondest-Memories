package net.erzekawek.fondestmemories.misc;

import net.erzekawek.fondestmemories.registry.FondestMemoriesDimensionRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Set;

public class InterspaceUtil {
    public static final Set<ResourceKey<Level>> NO_DEBUG = Set.of(
            FondestMemoriesDimensionRegistry.BARNFIELDS,
            FondestMemoriesDimensionRegistry.CARDINAL_LEVEL,
            FondestMemoriesDimensionRegistry.THALASSOPHOBIA
    );

    public static final Set<ResourceKey<Level>> FMLVL = Set.of(
            FondestMemoriesDimensionRegistry.BARNFIELDS,
            FondestMemoriesDimensionRegistry.CARDINAL_LEVEL,
            FondestMemoriesDimensionRegistry.ENDLESS_HIGHWAY,
            FondestMemoriesDimensionRegistry.FRONTIERS,
            FondestMemoriesDimensionRegistry.THALASSOPHOBIA,
            FondestMemoriesDimensionRegistry.MISTY_PIERS
    );

    private static final Map<ResourceKey<Level>, Component> NAMES = Map.of(
            FondestMemoriesDimensionRegistry.BARNFIELDS, Component.translatable("dimension.fondest_memories.barnfields"),
            FondestMemoriesDimensionRegistry.THALASSOPHOBIA, Component.translatable("dimension.fondest_memories.thalassophobia"),
            FondestMemoriesDimensionRegistry.ENDLESS_HIGHWAY, Component.translatable("dimension.fondest_memories.endless_higway"),
            FondestMemoriesDimensionRegistry.MISTY_PIERS, Component.translatable("dimension.fondest_memories.misty_piers"),
            FondestMemoriesDimensionRegistry.FRONTIERS, Component.translatable("dimension.fondest_memories.frontiers"),
            FondestMemoriesDimensionRegistry.CARDINAL_LEVEL, Component.translatable("dimension.fondest_memories.cardinal_level")
    );

    public static boolean noDebug(Level level) {
        return level != null && NO_DEBUG.contains(level.dimension());
    }

    private static final SystemToast.SystemToastId FM_TOAST = new SystemToast.SystemToastId(5000L);

    public static void showDimensionToast(Minecraft mc, ResourceKey<Level> dim) {
        SystemToast.add(
                mc.gui.toastManager(),
                FM_TOAST,
                NAMES.getOrDefault(dim, Component.literal("???")),
                Component.translatable("toast.fondestmemories.entered")
        );
    }
}
