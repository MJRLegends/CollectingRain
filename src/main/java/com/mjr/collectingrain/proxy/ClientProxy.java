package com.mjr.collectingrain.proxy;

import com.mjr.collectingrain.client.handlers.MainHandlerClient;
import com.mjr.mjrlegendslib.util.RegisterUtilities;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event) {
		RegisterUtilities.registerEventHandler(new MainHandlerClient());
	}
}
