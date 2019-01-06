package com.mjr.collectingrain.handlers.capabilities;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public interface IStatsCapability {

	void saveNBTData(NBTTagCompound nbt);

	void loadNBTData(NBTTagCompound nbt);

	void copyFrom(IStatsCapability oldData, boolean keepInv);

	WeakReference<EntityPlayerMP> getPlayer();

	void setPlayer(WeakReference<EntityPlayerMP> player);

	double getRainAmount();

	void setRainAmount(double d);
}
