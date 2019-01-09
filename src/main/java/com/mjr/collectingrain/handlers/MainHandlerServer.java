package com.mjr.collectingrain.handlers;

import java.util.List;

import com.google.common.collect.Lists;
import com.mjr.collectingrain.CollectingRain;
import com.mjr.collectingrain.Config;
import com.mjr.collectingrain.client.handlers.capabilities.CapabilityProviderStatsClient;
import com.mjr.collectingrain.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.collectingrain.handlers.capabilities.CapabilityProviderStats;
import com.mjr.collectingrain.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.collectingrain.handlers.capabilities.IStatsCapability;
import com.mjr.collectingrain.network.PacketHandler;
import com.mjr.collectingrain.network.PacketSimpleEP;
import com.mjr.collectingrain.network.PacketSimpleEP.EnumSimplePacket;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MainHandlerServer {
	private static List<PacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

	@SubscribeEvent
	public void onPlayer(PlayerTickEvent event) {
		if (event.player.getEntityWorld().getWorldInfo().isRaining() && event.player.getEntityWorld().canSeeSky(event.player.getPosition())) {
			IStatsCapability stats = null;

			if (event.player != null) {
				stats = event.player.getCapability(CapabilityStatsHandler.STATS_CAPABILITY, null);
				if (stats != null) {
					if (event.player.getHeldItemMainhand().getItem() instanceof ItemBucket && !event.player.getHeldItemMainhand().getItem().equals(Items.WATER_BUCKET)) {
						if (stats.getRainAmount() >= 100) {
							ItemStack itemSlack = event.player.getHeldItemMainhand();
							itemSlack = new ItemStack(Items.WATER_BUCKET);
							event.player.setHeldItem(EnumHand.MAIN_HAND, itemSlack);
							stats.setRainAmount(0);
						} else {
							stats.setRainAmount(stats.getRainAmount() + Config.mbPerTick);
						}
					} else if (event.player.getHeldItemOffhand().getItem() instanceof ItemBucket && !event.player.getHeldItemOffhand().getItem().equals(Items.WATER_BUCKET)) {
						if (stats.getRainAmount() >= 100) {
							ItemStack itemSlack = event.player.getHeldItemOffhand();
							itemSlack = new ItemStack(Items.WATER_BUCKET);
							event.player.setHeldItem(EnumHand.MAIN_HAND, itemSlack);
							stats.setRainAmount(0);
						} else {
							stats.setRainAmount(stats.getRainAmount() + Config.mbPerTick);
						}
					}
				}
			}
		}
	}

	public static void addPacketHandler(PacketHandler handler) {
		MainHandlerServer.packetHandlers.add(handler);
	}

	@SubscribeEvent
	public void worldUnloadEvent(WorldEvent.Unload event) {
		for (PacketHandler packetHandler : packetHandlers) {
			packetHandler.unload(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		if (event.phase == Phase.END) {
			final WorldServer world = (WorldServer) event.world;

			for (PacketHandler handler : packetHandlers) {
				handler.tick(world);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerCloned(PlayerEvent.Clone event) {
		IStatsCapability oldStats = event.getOriginal().getCapability(CapabilityStatsHandler.STATS_CAPABILITY, null);
		IStatsCapability newStats = event.getEntityPlayer().getCapability(CapabilityStatsHandler.STATS_CAPABILITY, null);
		newStats.copyFrom(oldStats, !event.isWasDeath() || event.getOriginal().worldObj.getGameRules().getBoolean("keepInventory"));
	}

	@SubscribeEvent
	public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayerMP) {
			event.addCapability(CapabilityStatsHandler.PLAYER_PROP, new CapabilityProviderStats((EntityPlayerMP) event.getObject()));
		} else if (event.getObject() instanceof EntityPlayer && ((EntityPlayer) event.getObject()).worldObj.isRemote) {
			this.onAttachCapabilityClient(event);
		}
	}

	@SideOnly(Side.CLIENT)
	private void onAttachCapabilityClient(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayerSP)
			event.addCapability(CapabilityStatsClientHandler.PLAYER_CLIENT_PROP, new CapabilityProviderStatsClient((EntityPlayerSP) event.getObject()));
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		final EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving instanceof EntityPlayerMP) {
			onPlayerUpdate((EntityPlayerMP) entityLiving);
		}
	}

	public void onPlayerUpdate(EntityPlayerMP player) {
		int tick = player.ticksExisted - 1;
		IStatsCapability stats = player.getCapability(CapabilityStatsHandler.STATS_CAPABILITY, null);

		if (player.getEntityWorld().getWorldInfo().isRaining() && player.getEntityWorld().canSeeSky(player.getPosition())) {
			if (tick % 20 == 0) {
				this.sendRainClientUpdatePacket(player, stats);
			}
		}
	}

	protected void sendRainClientUpdatePacket(EntityPlayerMP player, IStatsCapability stats) {
		CollectingRain.packetPipeline.sendTo(new PacketSimpleEP(EnumSimplePacket.C_UPDATE_RAIN_AMOUNT_LEVEL, player.worldObj.provider.getDimension(), new Object[] { stats.getRainAmount() }), player);
	}
}
