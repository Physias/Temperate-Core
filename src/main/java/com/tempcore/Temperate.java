package com.tempcore;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Temperate implements ModInitializer {

	public static final String MOD_ID = "temperate_core";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//Engine update interval
	private static final int UPDATE_INTERVAL = 20;

	private int tickCounter = 0;

	@Override
	public void onInitialize() {

		LOGGER.info("Temperate 1.0.1 Enabled");

		//Register thermal HUD networking
		PayloadTypeRegistry.clientboundPlay().register(ThermalHudPayload.TYPE, ThermalHudPayload.CODEC);

		//Server thermal update loop
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			tickCounter++;
			if (tickCounter < UPDATE_INTERVAL) {
				return;
			}

			tickCounter = 0;

			for (ServerPlayer player : server.getPlayerList().getPlayers()) {

				//Skip players without thermal simulation
				if (!player.isAlive() || player.gameMode.isCreative() || player.isSpectator()) {

					sendThermalState(player, ThermalState.NORMAL);
					continue;
				}

				//Calculate perceived temperature
				int perceivedTemp = TemperatureEngine.calculateTemperature(player);

				//Determine environmental thermal state
				ThermalState currentState = TemperatureEngine.getThermalState(perceivedTemp);

				//Update exposure and applied state
				ThermalState appliedState = TemperatureExposure.updateExposure(player, currentState);

				//Synchronize thermal HUD
				sendThermalState(player, appliedState);

				//Apply thermal effects
				TemperatureEffects.applyTemperatureEffects(player, appliedState);

				//Apply extreme thermal damage
				TemperatureExposure.applyThermalDamage(player, currentState);
			}
		});

		//Reset exposure on player death
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof ServerPlayer player) {
				TemperatureExposure.resetExposure(player);
			}
		});
	}

	//Send applied thermal state to player client
	private static void sendThermalState(ServerPlayer player, ThermalState state) {
		if (ServerPlayNetworking.canSend(
				player.connection, ThermalHudPayload.TYPE
		)) {
			ServerPlayNetworking.send(
					player, new ThermalHudPayload(state.name())
			);
		}
	}

	//Create Temperate resource identifiers
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

