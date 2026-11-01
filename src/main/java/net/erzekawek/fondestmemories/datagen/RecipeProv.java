package net.erzekawek.fondestmemories.datagen;

import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockRegistry;
import net.erzekawek.fondestmemories.registry.FondestMemoriesItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class RecipeProv extends FabricRecipeProvider {

    public RecipeProv(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected RecipeProvider createRecipeProvider(
            HolderLookup.Provider registries,
            BootstrapContext<Recipe<?>> recipeOutput,
            BootstrapContext<Advancement> advancementOutput
    ) {
        return new RecipeProvider(recipeOutput, advancementOutput) {
            @Override
            public void buildRecipes() {

                shaped(RecipeCategory.COMBAT, FondestMemoriesItemRegistry.PAN)
                        .define('#', Items.IRON_INGOT)
                        .define('O', FondestMemoriesItemRegistry.HOLY_TALISMAN)
                        .define('X', FondestMemoriesItemRegistry.METAL_PIPE)
                        .pattern(" ##")
                        .pattern(" O#")
                        .pattern("X  ")
                        .unlockedBy("has_talisman", has(FondestMemoriesItemRegistry.HOLY_TALISMAN))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, FondestMemoriesBlockRegistry.LOCKABLE_METAL_DOOR)
                        .define('#', Items.IRON_INGOT)
                        .define('O', FondestMemoriesItemRegistry.REGULAR_KEY)
                        .pattern("##")
                        .pattern("#O")
                        .pattern("##")
                        .unlockedBy("has_key", has(FondestMemoriesItemRegistry.REGULAR_KEY))
                        .save(output);

                shaped(RecipeCategory.COMBAT, FondestMemoriesItemRegistry.METAL_PIPE)
                        .define('#', Items.IRON_INGOT)
                        .pattern("  #")
                        .pattern(" # ")
                        .pattern("#  ")
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .save(output);
            }
        };
    }
}
