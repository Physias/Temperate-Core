package com.tempcore;


import net.minecraft.server.level.ServerPlayer;

public final class TemperatureExposure {

    //Exposure thresholds (seconds)
    private static final int EXPOSURE_I = 15;
    private static final int EXPOSURE_II = 20;
    private static final int EXPOSURE_III =30;

    //Extreme thermal damage
    private static final int DAMAGE_INTERVAL = 5;
    private static final float THERMAL_DAMAGE = 1.0F;

    private TemperatureExposure() {}

    //Thermal state categories
    private static boolean isCold(ThermalState state) {
        return state == ThermalState.COLD_I || state == ThermalState.COLD_II || state == ThermalState.COLD_III;
    }

    private static boolean isHot(ThermalState state) {
        return state == ThermalState.HOT_I || state == ThermalState.HOT_II || state == ThermalState.HOT_III;
    }

    //Determine applied state from exposure duration
    private static ThermalState getAppliedState(ThermalState currentState, int exposureSeconds) {
        if (currentState == ThermalState.NORMAL || exposureSeconds < EXPOSURE_I) {
            return ThermalState.NORMAL;
        }

        switch (currentState) {
            case COLD_I -> {
                return ThermalState.COLD_I;
            }
            case COLD_II -> {
                if (exposureSeconds >= EXPOSURE_II) {
                    return ThermalState.COLD_II;
                } else {
                    return ThermalState.COLD_I;
                }
            }
            case COLD_III -> {
                if (exposureSeconds >=EXPOSURE_III) {
                    return ThermalState.COLD_III;
                } else if (exposureSeconds >=EXPOSURE_II) {
                    return ThermalState.COLD_II;
                } else {
                    return ThermalState.COLD_I;
                }
            }

            case HOT_I ->  {
                return ThermalState.HOT_I;
            }

            case HOT_II ->  {
                if (exposureSeconds >= EXPOSURE_II) {
                    return ThermalState.HOT_II;
                } else {
                    return ThermalState.HOT_I;
                }
            }

            case HOT_III ->  {
                if (exposureSeconds >=EXPOSURE_III) {
                    return ThermalState.HOT_III;
                } else if (exposureSeconds >=EXPOSURE_II) {
                    return ThermalState.HOT_II;
                } else {
                    return ThermalState.HOT_I;
                }
            }
        }

        return ThermalState.NORMAL;
    }

    //Access persistent exposure data
    private static ThermalExposureSavedData storage(ServerPlayer player) {
        return ThermalExposureSavedData.get(player.level().getServer());
    }

    //Read saved thermal state
    private static ThermalState readState(String name) {
        if (name == null) {
            return ThermalState.NORMAL;
        }
        try {
            return ThermalState.valueOf(name);
        } catch (IllegalArgumentException exception) {
            return ThermalState.NORMAL;
        }
    }

    //Update player exposure
    public static ThermalState updateExposure(ServerPlayer player, ThermalState currentState) {

        ThermalExposureSavedData saved = storage(player);
        ThermalExposureSavedData.ExposureSnapshot previous = saved.get(player.getUUID());

        ThermalState lastState = readState(previous.lastState());

        int exposureSeconds = previous.exposureSeconds();
        int extremeSeconds = previous.extremeSeconds();

        //Reset counters on temperature reversal
        if ((isCold(lastState) && isHot(currentState)) || (isHot(lastState) && isCold(currentState))) {
            exposureSeconds = 0;
            extremeSeconds = 0;
        }

        //Update exposure counters
        switch (currentState) {

            case NORMAL -> {
                exposureSeconds = 0;
                extremeSeconds = 0;
            }

            case COLD_I, COLD_II, HOT_I, HOT_II -> {
                exposureSeconds++;
                extremeSeconds = 0;
            }

            case COLD_III, HOT_III -> {
                exposureSeconds++;
                extremeSeconds++;
            }
        }

        //Persist updated counters
        saved.put(player.getUUID(), new ThermalExposureSavedData.ExposureSnapshot(currentState.name(),  exposureSeconds, extremeSeconds));

        return getAppliedState(currentState, exposureSeconds);
    }

    //Reset player exposure on death
    public static void resetExposure(ServerPlayer player) {
        storage(player).remove(player.getUUID());
    }

    //Apply periodic extreme thermal damage
    public static void applyThermalDamage (ServerPlayer player, ThermalState currentState) {
        ThermalExposureSavedData.ExposureSnapshot data = storage(player).get(player.getUUID());

        int extremeSeconds = data.extremeSeconds();

        if (extremeSeconds < EXPOSURE_III || extremeSeconds % DAMAGE_INTERVAL != 0) {
            return;
        }

        if (currentState == ThermalState.COLD_III) {
            player.hurtServer(player.level(), player.damageSources().freeze(),THERMAL_DAMAGE);
        } else if (currentState == ThermalState.HOT_III) {
            player.hurtServer(player.level(), player.damageSources().hotFloor(),THERMAL_DAMAGE);
        }
    }
}
