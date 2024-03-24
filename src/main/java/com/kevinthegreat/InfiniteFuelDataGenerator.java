package com.kevinthegreat;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;

public class InfiniteFuelDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(InfiniteFuelRecipeProvider::new);
        pack.addProvider(InfiniteFuelEnglishLangProvider::new);
    }

    private static class InfiniteFuelRecipeProvider extends FabricRecipeProvider {
        public InfiniteFuelRecipeProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(RecipeExporter exporter) {
            offerDepletedRepairRecipe(exporter, Ingredient.ofItems(Items.NETHER_STAR), ConventionalItemTags.GLASS_BLOCKS, RecipeCategory.MISC, Items.NETHER_STAR);
        }

        public static void offerDepletedRepairRecipe(RecipeExporter exporter, Ingredient input, TagKey<Item> addition, RecipeCategory category, Item result) {
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("depleted", true);
            SmithingTransformRecipeJsonBuilder.create(
                            Ingredient.empty(),
                            DefaultCustomIngredients.nbt(input, nbt, false),
                            Ingredient.fromTag(addition),
                            category,
                            result
                    )
                    .criterion("has_addition", conditionsFromTag(addition))
                    .offerTo(exporter, getItemPath(result) + "_smithing");
        }
    }

    private static class InfiniteFuelEnglishLangProvider extends FabricLanguageProvider {
        protected InfiniteFuelEnglishLangProvider(FabricDataOutput dataGenerator) {
            super(dataGenerator, "en_us");
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            translationBuilder.add("item.infinitefuel.nether_star.depleted", "Depleted");
            translationBuilder.add("item.infinitefuel.nether_star.depleted.tooltip", "Repair with any glass block in a Smithing Table to reactivate");
        }
    }
}
