package com.mjr.collectingrain.client.render;

import org.lwjgl.opengl.GL11;

import com.mjr.collectingrain.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.collectingrain.client.handlers.capabilities.IStatsClientCapability;
import com.mjr.mjrlegendslib.client.gui.Overlay;
import com.mjr.mjrlegendslib.util.ClientUtilities;
import com.mjr.mjrlegendslib.util.MCUtilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class OverlayRainCollection extends Overlay {
	private static Minecraft minecraft = MCUtilities.getClient();

	public static void renderOverlay() {
		GlStateManager.disableLighting();

		final ScaledResolution scaledresolution = ClientUtilities.getScaledRes(OverlayRainCollection.minecraft, OverlayRainCollection.minecraft.displayWidth, OverlayRainCollection.minecraft.displayHeight);
		final int width = scaledresolution.getScaledWidth();
		OverlayRainCollection.minecraft.entityRenderer.setupOverlayRendering();

		GL11.glPushMatrix();

		IStatsClientCapability stats = null;
		if (minecraft.player != null) {
			EntityPlayerSP playerBaseClient = minecraft.player;
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.STATS_CLIENT_CAPABILITY, null);
		}
		String text = "Water Bucket Filled amount" + ": " + (int) stats.getRainAmount() + "%";

		GL11.glScalef(1.0F, 1.0F, 1.0F);
		int w = (scaledresolution.getScaledWidth() - OverlayRainCollection.minecraft.fontRenderer.getStringWidth(text)) / 2;
		int h = scaledresolution.getScaledHeight() - 31 - 18;
		OverlayRainCollection.minecraft.fontRenderer.drawString(text, w, h, ClientUtilities.to32BitColor(255, 255, 255, 255));

		GL11.glPopMatrix();
	}

}
