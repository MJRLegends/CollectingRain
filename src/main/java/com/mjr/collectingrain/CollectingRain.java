package com.mjr.collectingrain;

import com.mjr.collectingrain.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.collectingrain.handlers.MainHandlerServer;
import com.mjr.collectingrain.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.collectingrain.network.ChannelHandler;
import com.mjr.collectingrain.proxy.CommonProxy;
import com.mjr.mjrlegendslib.util.MessageUtilities;
import com.mjr.mjrlegendslib.util.RegisterUtilities;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CollectingRain.MODID, version = CollectingRain.VERSION, dependencies = "required-after:forge@[14.23.1.2555,); required-after:mjrlegendslib@[1.12.2-1.1.8,);", certificateFingerprint = "b02331787272ec3515ebe63ecdeea0d746653468")
public class CollectingRain {
	public static final String MODID = "collectingrain";
	public static final String VERSION = "1.12.2-1.0.3";

	@SidedProxy(clientSide = "com.mjr.collectingrain.proxy.ClientProxy", serverSide = "com.mjr.collectingrain.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(MODID)
	public static CollectingRain instance;

	public static ChannelHandler packetPipeline;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load();
		RegisterUtilities.registerEventHandler(new MainHandlerServer());
		CollectingRain.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetPipeline = ChannelHandler.init();
		CollectingRain.proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Register Capability Handlers
		CapabilityStatsHandler.register();
		CapabilityStatsClientHandler.register();
		CollectingRain.proxy.postInit(event);
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		MessageUtilities.fatalErrorMessageToLog(MODID, "Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported!");
	}
}
