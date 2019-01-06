package com.mjr.collectingrain.client.handlers;

import java.util.List;

import com.google.common.collect.Lists;
import com.mjr.collectingrain.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.collectingrain.client.handlers.capabilities.IStatsClientCapability;
import com.mjr.collectingrain.client.render.OverlayRainCollection;
import com.mjr.collectingrain.network.PacketHandler;
import com.mjr.mjrlegendslib.util.MCUtilities;
import com.mjr.mjrlegendslib.util.PlayerUtilties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class MainHandlerClient {

	private static List<PacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

	public static void addPacketHandler(PacketHandler handler) {
		MainHandlerClient.packetHandlers.add(handler);
	}

	@SubscribeEvent
	public void worldUnloadEvent(WorldEvent.Unload event) {
		for (PacketHandler packetHandler : packetHandlers) {
			packetHandler.unload(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		final Minecraft minecraft = MCUtilities.getClient();
		final WorldClient world = minecraft.world;

		if (event.phase == Phase.END) {
			if (world != null) {
				for (PacketHandler handler : packetHandlers) {
					handler.tick(world);
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		final Minecraft minecraft = MCUtilities.getClient();
		final EntityPlayerSP player = minecraft.player;
		final EntityPlayerSP playerBaseClient = PlayerUtilties.getPlayerBaseClientFromPlayer(player, false);
		if (player != null && player.world.getWorldInfo().isRaining() && player.getEntityWorld().canSeeSky(player.getPosition())) {
			IStatsClientCapability stats = null;

			if (player != null) {
				stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY, null);
			}
			if (minecraft.currentScreen == null && player.getEntityWorld().getWorldInfo().isRaining() && player.getEntityWorld().canSeeSky(player.getPosition())
					&& (player.getHeldItemMainhand().getItem() instanceof ItemBucket || player.getHeldItemOffhand().getItem() instanceof ItemBucket)) {
				OverlayRainCollection.renderOverlay();
			}
		}
	}
}
