package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.items.Pan;
import net.erzekawek.fondestmemories.items.PipeItem;
import net.erzekawek.veyl.items.AbstractUsableItem;
import net.erzekawek.veyl.utils.registry.UnifiedRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;

import java.util.HashMap;
import java.util.Map;

import static net.erzekawek.fondestmemories.FondestMemories.MOD_ID;

public class FondestMemoriesItemRegistry {

    public static Map<Item, ResourceKey<Item>> idLookup = new HashMap<>();

    public static Item HOLY_TALISMAN = UnifiedRegistry.registerItem(MOD_ID, "holy_talisman", new Item.Properties().stacksTo(1));

    public static Item METAL_PIPE = UnifiedRegistry.registerItem(MOD_ID, "unusable_metal_pipe", PipeItem::new,
            new Item.Properties()
                    .durability(700)
                    .enchantable(14)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 6.0, AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.4, AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .build())
                    .component(DataComponents.WEAPON, new Weapon(1)));

    public static Item REGULAR_KEY = UnifiedRegistry.registerItem(MOD_ID, "regular_key", settings -> new AbstractUsableItem(settings, true), new Item.Properties().stacksTo(16));

    public static Item MASTER_KEY = UnifiedRegistry.registerItem(MOD_ID, "master_key", settings -> new AbstractUsableItem(settings, false), new Item.Properties().stacksTo(16));

    public static Item PAN = UnifiedRegistry.registerItem(MOD_ID, "pan", Pan::new,
            new Item.Properties()
                    .durability(700)
                    .enchantable(14)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.4, AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .build())
                    .component(DataComponents.WEAPON, new Weapon(1)));

    public static final Item NULL_EGG = UnifiedRegistry.registerItem(
            MOD_ID,
            "null_spawn_egg",
            SpawnEggItem::new,
            new Item.Properties().spawnEgg(FondestMemoriesMobs.NULL_TYPE)
    );

    public static void initialize() {
    }

}
