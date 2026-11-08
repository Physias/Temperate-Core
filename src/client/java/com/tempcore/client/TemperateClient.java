package com.tempcore.client;

import com.tempcore.Temperate;
import com.tempcore.ThermalHudPayload;
import com.tempcore.ThermalState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

public class TemperateClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientPlayNetworking.registerGlobalReceiver(ThermalHudPayload.TYPE, ((payload, context) -> {

			ThermalState state;

			try {
				state = ThermalState.valueOf(payload.state());
			} catch (IllegalArgumentException exception) {
				state = ThermalState.NORMAL;
			}

			ClientThermalHudState.setState(state);
		}));

		HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Temperate.id("thermal_hud"), ThermalHudRenderer::render);

		//Reset client state on disconnect
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			ClientThermalHudState.reset();
			ThermalHudRenderer.reset();
		});
	}
}