package com.mjr.collectingrain.client.handlers.capabilities;

public class StatsClientCapability implements IStatsClientCapability {
	public double rainAmount;

	@Override
	public double getRainAmount() {
		return rainAmount;
	}

	@Override
	public void setRainAmount(double rainAmount) {
		this.rainAmount = rainAmount;
	}
}
