package com.mjr.collectingrain;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class Config {

	static final ForgeConfigSpec serverSpec;
	public static final Server SERVER;

	static {
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static class Server {
		public DoubleValue mbPerTick;

		Server(ForgeConfigSpec.Builder builder) {
			builder.comment("Server configuration settings").push("server");
			mbPerTick = builder.comment("Default: 1.0").translation("config.amount_mb.per_tick").defineInRange("amountMBPerTick", 1.0, 0.0, 1000.0);
			builder.pop();
		}
	}
}
