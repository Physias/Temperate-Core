package com.tempcore;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Public registry for thermal equipment statistics.
 * Allows external addons to register custom equipment.
 */
public final class ThermalEquipmentRegistry {

    //Default protection values
    private static final ProtectionStats NONE = new ProtectionStats(0,0,0);

    //Registered equipment statistics
    private static final Map<Item, ProtectionStats> EQUIPMENT_STATS = new HashMap<>();

    private ThermalEquipmentRegistry() {}

    //Vanilla equipment registeration
    static {

        //Leather equipment
        register(Items.LEATHER_HELMET, new ProtectionStats(1, 0, 0));
        register(Items.LEATHER_CHESTPLATE, new ProtectionStats(3, 0, 0));
        register(Items.LEATHER_LEGGINGS, new ProtectionStats(3, 0, 0));
        register(Items.LEATHER_BOOTS, new ProtectionStats(1, 0, 0));

        //Copper equipment
        register(Items.COPPER_HELMET, new ProtectionStats(1, 0, 0));
        register(Items.COPPER_CHESTPLATE, new ProtectionStats(1, 1, 1));
        register(Items.COPPER_LEGGINGS, new ProtectionStats(1, 1, 1));
        register(Items.COPPER_BOOTS, new ProtectionStats(1, 0, 0));

        //Iron equipment
        register(Items.IRON_HELMET, new ProtectionStats(1, 0, 0));
        register(Items.IRON_CHESTPLATE, new ProtectionStats(2, 0, 0));
        register(Items.IRON_LEGGINGS, new ProtectionStats(2, 0, 0));
        register(Items.IRON_BOOTS, new ProtectionStats(1, 0, 0));

        //Chainmail equipment
        register(Items.CHAINMAIL_HELMET, new ProtectionStats(0, 0, 1));
        register(Items.CHAINMAIL_CHESTPLATE, new ProtectionStats(1, 0, 2));
        register(Items.CHAINMAIL_LEGGINGS, new ProtectionStats(1, 0, 2));
        register(Items.CHAINMAIL_BOOTS, new ProtectionStats(0, 0, 1));

        //Golden equipment
        register(Items.GOLDEN_HELMET, new ProtectionStats(0, 0, 1));
        register(Items.GOLDEN_CHESTPLATE, new ProtectionStats(0, 0, 2));
        register(Items.GOLDEN_LEGGINGS, new ProtectionStats(0, 0, 2));
        register(Items.GOLDEN_BOOTS, new ProtectionStats(0, 0, 1));

        //Diamond equipment
        register(Items.DIAMOND_HELMET, new ProtectionStats(0, 0, 1));
        register(Items.DIAMOND_CHESTPLATE, new ProtectionStats(0, 0, 2));
        register(Items.DIAMOND_LEGGINGS, new ProtectionStats(0, 0, 2));
        register(Items.DIAMOND_BOOTS, new ProtectionStats(0, 0, 1));

        //Netherite equipment
        register(Items.NETHERITE_HELMET, new ProtectionStats(1, 1, 0));
        register(Items.NETHERITE_CHESTPLATE, new ProtectionStats(2, 1, 0));
        register(Items.NETHERITE_LEGGINGS, new ProtectionStats(2, 1, 0));
        register(Items.NETHERITE_BOOTS, new ProtectionStats(1, 1, 0));
    }

    /**
     * Return thermal Statistics for an item.
     * Unregistered items receive zero protection.
     */
    public static ProtectionStats get(Item item) {
        return EQUIPMENT_STATS.getOrDefault(item, NONE);
    }

    /**
     * Registers or replaces thermal statistics for an item.
     *
     * thermalImpact may be positive, negative or zero.
     * Resistances must be non-negative.
     */
    public static void register(Item item, ProtectionStats stats) {

        Objects.requireNonNull(item, "Equipment item cannot be null");
        Objects.requireNonNull(stats, "Protection Stats cannot be null");

        if (stats.getColdResistance() < 0 || stats.getHeatResistance() < 0) {
            throw new IllegalArgumentException("Cold Resistance or Heat Resistance cannot be negative");
        }

        EQUIPMENT_STATS.put(item, stats);
    }
}
