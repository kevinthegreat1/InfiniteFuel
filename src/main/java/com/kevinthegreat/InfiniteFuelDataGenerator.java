package com.kevinthegreat;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class InfiniteFuelDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(InfiniteFuelEnglishLangProvider::new);
	}

	private static class InfiniteFuelEnglishLangProvider extends FabricLanguageProvider {
		protected InfiniteFuelEnglishLangProvider(FabricDataOutput dataGenerator) {
			super(dataGenerator, "en_us");
		}

		@Override
		public void generateTranslations(TranslationBuilder translationBuilder) {
			translationBuilder.add("item.infinitefuel.nether_star.depleted", "Depleted");
		}
	}
}
