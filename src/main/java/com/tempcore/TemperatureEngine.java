package com.tempcore;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public final class TemperatureEngine {

    //Thermal comfort limits
    private static final int MIN_COMFORT = 5;
    private static final int MAX_COMFORT = 14;

    //Thermal cold limits
    private static final int COLD_LIMIT_I = 0;
    private static final int COLD_LIMIT_II = -5;

    //Thermal hot limits
    private static final int HOT_LIMIT_I = 19;
    private static final int HOT_LIMIT_II = 24;

    //Player armor slots
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET};

    private TemperatureEngine() {}

    //Cold resistance calculation
    public static int coldResist(int temp, int coldResistance) {
        if (temp >= MIN_COMFORT) {
            return 0;
        }
        return Math.min(coldResistance, MIN_COMFORT - temp);
    }

    //Heat resistance calculation
    public static int heatResist(int temp, int heatResistance) {
        if (temp <= MAX_COMFORT) {
            return 0;
        }
        return Math.min(heatResistance, temp - MAX_COMFORT);
    }

    //Calculate total equipment protection
    public static ProtectionStats total(ServerPlayer player) {

        int totalImpacts = 0;
        int totalColdResist = 0;
        int totalHeatResist = 0;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            Item item = stack.getItem();
            ProtectionStats value = ThermalEquipmentRegistry.get(item);
            totalImpacts += value.getThermalImpact();
            totalColdResist += value.getColdResistance();
            totalHeatResist += value.getHeatResistance();
        }
        return new ProtectionStats(totalImpacts, totalColdResist, totalHeatResist);
    }

    //Calculate perceived player temperature
    public static int calculateTemperature(ServerPlayer player) {
        ProtectionStats stats = total(player);

        int ambientTemperature = BiomeTemperature.environmentTemperature(player);

        int temperature = ambientTemperature + stats.getThermalImpact();

        int coldProtection = coldResist(temperature, stats.getColdResistance());

        int heatProtection = heatResist(temperature, stats.getHeatResistance());

        return temperature + coldProtection - heatProtection;
    }

    //Determine thermal state
    public static ThermalState getThermalState(int playerTemp) {

        if (playerTemp < COLD_LIMIT_II) {
            return ThermalState.COLD_III;
        } else if (playerTemp < COLD_LIMIT_I) {
            return ThermalState.COLD_II;
        } else if (playerTemp < MIN_COMFORT) {
            return ThermalState.COLD_I;
        } else if (playerTemp > HOT_LIMIT_II) {
            return ThermalState.HOT_III;
        } else if (playerTemp > HOT_LIMIT_I) {
            return ThermalState.HOT_II;
        }  else if (playerTemp > MAX_COMFORT) {
            return ThermalState.HOT_I;
        } else {
            return ThermalState.NORMAL;
        }
    }
}
