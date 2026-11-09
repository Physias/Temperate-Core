# Temperate Core 1.0.1 — Thermal Effects Hotfix

This maintenance update fixes a missing cold-exposure effect in Temperate Core.

## Fixed

- Corrected the `COLD_I` registration in `TemperatureEffects`, restoring **Slowness I** after 15 seconds of level I cold exposure.
- Removed an incorrect duplicate `COLD_II` mapping that prevented the level I cold effect from being registered.

## Compatibility

- Minecraft Java Edition **26.3**
- Fabric Loader **0.20.1+** and compatible Fabric API
- **Java 25+**
- No changes to thermal thresholds, armor statistics, exposure persistence, or the HUD.

**Testing note:** Multiplayer and third-party addon compatibility have not yet been fully validated.
