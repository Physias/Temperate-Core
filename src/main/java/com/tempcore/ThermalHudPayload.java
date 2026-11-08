package com.tempcore;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ThermalHudPayload(String state) implements CustomPacketPayload {

    //Network packet identifier
    public static final Type<ThermalHudPayload> TYPE = new Type<>(Temperate.id("thermal_hud"));

    //Thermal state serialization
    public static final StreamCodec<RegistryFriendlyByteBuf, ThermalHudPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ThermalHudPayload::state, ThermalHudPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
