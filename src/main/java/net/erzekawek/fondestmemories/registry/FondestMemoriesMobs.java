package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.entities.NullEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class FondestMemoriesMobs {

    public static final ResourceKey<EntityType<?>> NULL_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, FondestMemories.id("null"));

    public static final EntityType<NullEntity> NULL_TYPE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, NULL_KEY,
            EntityType.Builder.of(NullEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .build(NULL_KEY));


    public static void initialize() {
        FabricDefaultAttributeRegistry.register(NULL_TYPE, NullEntity.createAttributes());
    }
}
