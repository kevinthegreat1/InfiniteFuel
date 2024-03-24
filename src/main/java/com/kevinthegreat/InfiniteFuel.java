package com.kevinthegreat;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfiniteFuel implements ModInitializer {
	public static final String MOD_ID = "infinitefuel";
	public static final String MOD_NAME = "Infinite Fuel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FuelRegistry.INSTANCE.add(Items.NETHER_STAR, 100000);
		LOGGER.info(MOD_NAME + " initialized");
	}
}
