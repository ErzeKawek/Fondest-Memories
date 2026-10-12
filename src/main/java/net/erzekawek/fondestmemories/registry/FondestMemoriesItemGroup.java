package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FondestMemoriesItemGroup {

    public static final CreativeModeTab FEITEM_GROUP = register("fondest_memories_tab",
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("creativeTab.fondest_memories.fondest_memories_tab"))
                    .icon(() -> new ItemStack(FondestMemoriesBlockRegistry.REAL_GATE))
                    .displayItems((displayContext, output) -> BuiltInRegistries.ITEM.keySet()
                            .stream()
                            .sorted()
                            .filter(id -> id.getNamespace().equals(FondestMemories.MOD_ID))
                            .map(BuiltInRegistries.ITEM::getValue)
                            .forEach(output::accept))
                    .build());

    public static <T extends CreativeModeTab> T register(String name, T itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FondestMemories.id(name),
                itemGroup);
    }

    public static void initialize() {
    }
}
