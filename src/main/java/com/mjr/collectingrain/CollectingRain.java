package com.mjr.collectingrain;

import com.mjr.collectingrain.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.collectingrain.handlers.MainHandlerServer;
import com.mjr.collectingrain.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.collectingrain.network.ChannelHandler;
import com.mjr.collectingrain.proxy.CommonProxy;
import com.mjr.mjrlegendslib.util.MessageUtilities;
import com.mjr.mjrlegendslib.util.RegisterUtilities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CollectingRain.MODID, version = CollectingRain.VERSION, dependencies = "required-after:Forge@[12.18.3.2239,); required-after:mjrlegendslib@[1.10.2-1.1.5,);", certificateFingerprint = "b02331787272ec3515ebe63ecdeea0d746653468")
public class CollectingRain {
	public static final String MODID = "collectingrain";
	public static final String VERSION = "1.10.2-1.0.2";

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

	public static boolean isSupportedBucket(ItemStack stack) {
		if(stack == null)
			return false;
		if(stack.getItem().equals(Items.BUCKET))
			return true;
		if(stack.getItem().getRegistryName().toString().equalsIgnoreCase("ceramics:clay_bucket") && stack.getTagCompound() == null)
			return true;
		return false;		
	}
	
	public static ItemStack getWaterBucket(ItemStack stack) {
		if(stack.getItem().equals(Items.BUCKET))
			return new ItemStack(Items.WATER_BUCKET);
		if(stack.getItem().getRegistryName().toString().equalsIgnoreCase("ceramics:clay_bucket")) {
			NBTTagCompound tags = stack.getTagCompound();
			if(tags == null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag("fluids", new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
				stack.setTagCompound(tag);
				return stack;
			}
		}
		return null;
	}
}
