package net.erzekawek.fondestmemories.registry;

import net.erzekawek.fondestmemories.FondestMemories;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.awt.*;

public class FondestMemoriesTagsRegistry {
    public static final TagKey<Block> CORN_PLACEABLE_ON =
            TagKey.create(Registries.BLOCK, FondestMemories.id("tall_glow_plant_placeable_on"));

    public static final TagKey<Level> DARK_DIM =
            TagKey.create(Registries.DIMENSION, FondestMemories.id("dark_dimensions"));
}
