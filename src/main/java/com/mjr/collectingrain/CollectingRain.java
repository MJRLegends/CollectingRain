package com.mjr.collectingrain;

import com.mjr.collectingrain.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.collectingrain.handlers.MainHandlerServer;
import com.mjr.collectingrain.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.collectingrain.proxy.ClientProxy;
import com.mjr.collectingrain.proxy.IProxy;
import com.mjr.collectingrain.proxy.ServerProxy;
import com.mjr.mjrlegendslib.util.RegisterUtilities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CollectingRain.MODID)
public class CollectingRain {
	public static final String MODID = "collectingrain";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    
    public CollectingRain() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
		RegisterUtilities.registerEventHandler(new MainHandlerServer());
		CapabilityStatsHandler.register();
		CapabilityStatsClientHandler.register();
		//packetPipeline = ChannelHandler.init();
	}
	
	//public static ChannelHandler packetPipeline;


/*	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		MessageUtilities.fatalErrorMessageToLog(MODID, "Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported!");
	}*/
	
	public static boolean isSupportedBucket(ItemStack stack) {
		if(stack.getItem().equals(Items.BUCKET))
			return true;
		if(stack.getItem().getRegistryName().toString().equalsIgnoreCase("ceramics:clay_bucket") && stack.getTag() == null)
			return true;
		return false;		
	}
	
	public static ItemStack getWaterBucket(ItemStack stack) {
		if(stack.getItem().equals(Items.BUCKET))
			return new ItemStack(Items.WATER_BUCKET);
		if(stack.getItem().getRegistryName().toString().equalsIgnoreCase("ceramics:clay_bucket")) {
			stack = stack.copy();
			NBTTagCompound tags = stack.getTag();
			if(tags == null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag("fluids", new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
				stack.setTag(tag);
				stack.setCount(1);
				return stack;
			}
		}
		return null;
	}
}
