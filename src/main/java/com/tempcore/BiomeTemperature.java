package com.tempcore;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;

public final class BiomeTemperature {

    //Temperature conversion settings
    private static final float TEMPERATURE_SCALE = 20.0f;
    private static final float TEMPERATURE_OFFSET = 11.0f;

    private BiomeTemperature() {}

    //Extract vanilla biome temperature
    private static float extractVanillaTemperature(ServerPlayer player) {

        BlockPos playerPos = player.blockPosition();
        ServerLevel playerLev = player.level();

        Biome playerBiome = playerLev.getBiome(playerPos).value();

        return playerBiome.getBaseTemperature();
    }

    //Convert vanilla temperature to Temperate scale
    private static int convertTemperature(float vanillaTemperature) {
        return Math.round(vanillaTemperature * TEMPERATURE_SCALE -  TEMPERATURE_OFFSET);
    }

    //Get environmental temperature
    public static int environmentTemperature(ServerPlayer player) {

        float vanillaTemperature = extractVanillaTemperature(player);

        return convertTemperature(vanillaTemperature);
    }
}
