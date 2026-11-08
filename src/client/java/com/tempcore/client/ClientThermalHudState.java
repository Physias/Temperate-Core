package com.tempcore.client;

import com.tempcore.ThermalState;

public class ClientThermalHudState {
    private static ThermalState currentState = ThermalState.NORMAL;

    private ClientThermalHudState() {}

    //Update thermal state received from server
    public static void setState(ThermalState state) {
        currentState = state;
    }

    //Get current thermal state for HUD rendering
    public static ThermalState getState() {
        return currentState;
    }

    //Reset state when disconnecting
    public static void reset() {
        currentState = ThermalState.NORMAL;
    }
}
