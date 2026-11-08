# Temperate Core 1.0.0 — Initial Release

The first public release of Temperate Core, a lightweight, extensible temperature engine for Minecraft Java 26.3 (Fabric).

## Features

- Biome-based environmental temperature and perceived temperature calculations.
- Seven thermal states, from `COLD_III` to `HOT_III`, with a comfort range of 5–14.
- Thermal impact, cold resistance, and heat resistance for vanilla armor.
- Progressive thermal exposure effects at 15, 20, and 30 seconds.
- Slowness, Weakness, Hunger, and periodic damage during prolonged extreme exposure.
- Persistent player exposure data stored by UUID.
- Smooth, full-screen hot/cold visual HUD with server-to-client synchronization.
- Public equipment API: `ProtectionStats` and `ThermalEquipmentRegistry`.
- Standalone Core operation without a separate content addon.

## Compatibility

- Minecraft Java 26.3
- Fabric Loader 0.20.1 or newer
- Compatible Fabric API (required)
- Java 25 or newer

## Known limitations

- Multiplayer behavior has not yet been fully validated.
- Compatibility with independently developed addons has not yet been fully validated.
- Weather, altitude, shelter, and local heat sources are not part of the 1.0.0 environmental calculation.
- Specialized Arctic and Insulated outfits are planned for the separate Temperate Addon.

Developed by **Physias** · MIT License.
