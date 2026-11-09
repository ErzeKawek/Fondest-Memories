package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FondestMemoriesItemGroup {

    private static final List<Predicate<String>> GROUPS = List.of(
            p -> p.startsWith("piedene_") || p.equals("moist_wool_block"),
            p -> p.startsWith("sky_"),
            p -> p.startsWith("pool_tile"),
            p -> p.endsWith("_passable_concrete"),
            p -> p.startsWith("ceiling_"),
            p -> p.contains("door"),
            p -> p.contains("_gate"),
            p -> p.endsWith("_rift"),
            p -> p.contains("crate"),
            p -> p.startsWith("corn_"),
            p -> p.equals("liminal_glass")
    );

    private static int groupOf(Identifier id) {
        String path = id.getPath();
        for (int i = 0; i < GROUPS.size(); i++) {
            if (GROUPS.get(i).test(path)) return i;
        }
        return GROUPS.size();
    }

    public static final CreativeModeTab FEITEM_GROUP = register("fondest_memories_tab",
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("creativeTab.fondest_memories.fondest_memories_tab"))
                    .icon(() -> new ItemStack(FondestMemoriesBlockRegistry.PIEDENE_BLOCK))
                    .displayItems((displayContext, output) -> BuiltInRegistries.ITEM.keySet()
                            .stream()
                            .filter(id -> id.getNamespace().equals(FondestMemories.MOD_ID))
                            .sorted(Comparator.comparingInt(FondestMemoriesItemGroup::groupOf).thenComparingInt(id -> BuiltInRegistries.ITEM.getId(BuiltInRegistries.ITEM.getValue(id))))
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
