package com.tempcore;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.*;

public final class TemperatureEffects {

    //Effect duration (ticks)
    private static final int EFFECT_DURATION = 100;

    //Thermal state effects
    private static final Map<ThermalState, List<MobEffectInstance>> STATE_EFFECTS = new EnumMap<>(ThermalState.class);

    private TemperatureEffects() {}

    //Thermal effects registeration
    static {

        //Slowness
        MobEffectInstance slownessI = new MobEffectInstance(MobEffects.SLOWNESS, EFFECT_DURATION, 0, false, false);
        MobEffectInstance slownessII = new MobEffectInstance(MobEffects.SLOWNESS, EFFECT_DURATION, 1, false, false);

        //Weakness
        MobEffectInstance weaknessI = new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION, 0, false, false);

        //Hunger
        MobEffectInstance hungerI = new MobEffectInstance(MobEffects.HUNGER, EFFECT_DURATION, 0, false, false);
        MobEffectInstance hungerII = new MobEffectInstance(MobEffects.HUNGER, EFFECT_DURATION, 1, false, false);

        //Cold states
        STATE_EFFECTS.put(ThermalState.COLD_II, List.of(slownessI));
        STATE_EFFECTS.put(ThermalState.COLD_II, List.of(slownessI, weaknessI));
        STATE_EFFECTS.put(ThermalState.COLD_III, List.of(slownessII,  weaknessI));

        //Hot States
        STATE_EFFECTS.put(ThermalState.HOT_I, List.of(hungerI));
        STATE_EFFECTS.put(ThermalState.HOT_II, List.of(hungerI, slownessI));
        STATE_EFFECTS.put(ThermalState.HOT_III, List.of(hungerII, hungerI));
    }

    //Apply effects associated with thermal state
    public static void applyTemperatureEffects(ServerPlayer player, ThermalState state) {

        List<MobEffectInstance> effects = STATE_EFFECTS.getOrDefault(state, List.of());

        for (MobEffectInstance effect : effects) {
            player.addEffect(new MobEffectInstance(effect));
        }
    }
}
