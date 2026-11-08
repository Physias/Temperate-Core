package com.tempcore;

/**
 * Immutable thermal protection statistics.
 * Shared between Temperate Core and external addons.
 */
public final class ProtectionStats {

    //Thermal Impact
    private final int thermalImpact;

    //Thermal Resistances
    private final int coldResistance;
    private final int heatResistance;

    //Create thermal protection statistics
    public ProtectionStats(int thermalImpact, int coldResistance, int heatResistance) {
        this.thermalImpact = thermalImpact;
        this.coldResistance = coldResistance;
        this.heatResistance = heatResistance;
    }

    //get general thermal impact
    public int getThermalImpact() {
        return thermalImpact;
    }

    //Get cold resistance
    public int getColdResistance() {
        return coldResistance;
    }

    //Get heat resistance
    public int getHeatResistance() {
        return heatResistance;
    }
}
