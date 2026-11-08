package com.tempcore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ThermalExposureSavedData extends SavedData {

    //Player exposure snapshot
    public record ExposureSnapshot(
            String lastState,
            int exposureSeconds,
            int extremeSeconds
    ) {
        public static final Codec<ExposureSnapshot> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.STRING.fieldOf("lastState").forGetter(ExposureSnapshot::lastState),
                        Codec.INT.fieldOf("exposureSeconds").forGetter(ExposureSnapshot::exposureSeconds),
                        Codec.INT.fieldOf("extremeSeconds").forGetter(ExposureSnapshot::extremeSeconds)
                ).apply(instance, ExposureSnapshot::new));
    }

    //Default exposure values
    private static final ExposureSnapshot EMPTY = new ExposureSnapshot("NORMAL", 0,0);

    //Persistent player data
    private final Map<String, ExposureSnapshot> exposures = new HashMap<>();

    //Create saved storage
    public ThermalExposureSavedData() {}

    //Restore saved storage
    private ThermalExposureSavedData(Map<String, ExposureSnapshot> loaded) {
        exposures.putAll(loaded);
    }

    //Data serialization
    private static final Codec<ThermalExposureSavedData> CODEC =
            Codec.unboundedMap(Codec.STRING, ExposureSnapshot.CODEC).xmap(ThermalExposureSavedData::new, data -> data.exposures);

    //World saved data registration
    private static final SavedDataType<ThermalExposureSavedData> TYPE =
            new SavedDataType<>(Temperate.id("thermal_exposure"),ThermalExposureSavedData::new, CODEC, null);

    //Access persistent world storage
    public static ThermalExposureSavedData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    //Retrieve player exposure
    public ExposureSnapshot get(UUID uuid) {
        return exposures.getOrDefault(uuid.toString(), EMPTY);
    }

    //Update player exposure
    public void put(UUID uuid, ExposureSnapshot snapshot) {

        //Remove empty exposure data
        if (snapshot.lastState().equals("NORMAL")
        && snapshot.exposureSeconds() == 0 && snapshot.extremeSeconds() == 0) {
            remove(uuid);
            return;
        }

        ExposureSnapshot previous = exposures.put(uuid.toString(), snapshot);

        //Mark storage dirty only when data changes
        if (!snapshot.equals(previous)) {
            setDirty();
        }
    }

    //Remove player exposure
    public void remove(UUID uuid) {
        if (exposures.remove(uuid.toString()) != null) {
            setDirty();
        }
    }
}
