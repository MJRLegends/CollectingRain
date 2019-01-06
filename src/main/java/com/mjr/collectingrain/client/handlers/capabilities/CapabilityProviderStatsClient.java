package com.mjr.collectingrain.client.handlers.capabilities;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityProviderStatsClient implements ICapabilityProvider {
	@SuppressWarnings("unused")
	private EntityPlayerSP owner;
	private IStatsClientCapability statsCapability;

	public CapabilityProviderStatsClient(EntityPlayerSP owner) {
		this.owner = owner;
		this.statsCapability = CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY.getDefaultInstance();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY != null && capability == CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY) {
			return CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY.cast(statsCapability);
		}

		return null;
	}
}
