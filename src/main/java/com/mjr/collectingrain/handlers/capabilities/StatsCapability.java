package com.mjr.collectingrain.handlers.capabilities;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class StatsCapability implements IStatsCapability {
	public WeakReference<EntityPlayerMP> player;
	public double rainAmount;
	public int buildFlags = 0;

	@Override
	public double getRainAmount() {
		return rainAmount;
	}

	@Override
	public void setRainAmount(double rainAmount) {
		this.rainAmount = rainAmount;
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt.setDouble("rainAmount", this.rainAmount);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		try {
			this.rainAmount = nbt.getDouble("rainAmount");
		} catch (Exception e) {
			System.out.println("Found error in saved Collecting Rain player data for " + player.get().getGameProfile().getName() + " - this should fix itself next relog.");
			e.printStackTrace();
		}

		System.out.println("Finished loading Collecting Rain player data for " + player.get().getGameProfile().getName() + " : " + this.buildFlags);
	}

	@Override
	public void copyFrom(IStatsCapability oldData, boolean keepInv) {
		this.rainAmount = oldData.getRainAmount();
	}

	@Override
	public WeakReference<EntityPlayerMP> getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(WeakReference<EntityPlayerMP> player) {
		this.player = player;
	}
}
