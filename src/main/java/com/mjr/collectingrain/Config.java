package com.mjr.collectingrain;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	public static double mbPerTick;

	public static void load() {
		Configuration config = new Configuration(new File("config/CollectingRain.cfg"));
		config.load();

		mbPerTick = config.get(Configuration.CATEGORY_GENERAL, "Amount of mb gained per tick", false, "Default: 1").getDouble(1.0);

		config.save();
	}
}
