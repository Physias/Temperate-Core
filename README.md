# Temperate Core

**A lightweight, extensible temperature system for Minecraft Java Edition.**

Temperate Core adds environmental temperature, thermal protection from equipment, progressive exposure effects, and a visual hot/cold screen overlay. It is designed as a standalone foundation for future content addons and integrations by other mod developers.

> **Version:** 1.0.0  
> **Minecraft:** Java Edition 26.3  
> **Mod loader:** Fabric  
> **Author:** Physias  
> **License:** MIT

## Features

- **Biome-based temperature:** reads the current biome's base temperature and converts it to Temperate's scale.
- **Equipment protection:** vanilla armor can change perceived temperature and provide cold or heat resistance.
- **Seven thermal states:** `COLD_III`, `COLD_II`, `COLD_I`, `NORMAL`, `HOT_I`, `HOT_II`, and `HOT_III`.
- **Progressive exposure:** effects build up over time rather than activating immediately when the player enters a hostile temperature.
- **Survival consequences:** cold may cause Slowness and Weakness; heat may cause Hunger and Slowness. Prolonged extreme exposure can deal periodic damage.
- **Visual-only HUD:** a single full-screen overlay is tinted blue or orange, with intensity and smooth transitions based on the active thermal state. No text or temperature gauge is displayed.
- **Persistent exposure:** player exposure is stored by UUID in the world's saved data, surviving disconnections and normal world/server restarts. Exposure resets on death.
- **Addon API:** other mods can register fixed thermal statistics for their equipment.

## Requirements

- Minecraft Java Edition **26.3**
- Fabric Loader **0.20.1 or newer**
- **Fabric API** compatible with Minecraft 26.3
- **Java 25 or newer**

## Installation

1. Install Fabric Loader for Minecraft 26.3.
2. Install the matching Fabric API version.
3. Place the **Temperate Core 1.0.0** JAR in your Minecraft `mods` folder.
4. Launch the game with your Fabric profile.

Temperate Core works without a separate Temperate content addon. For future multiplayer use, the mod is designed to run on the server and client so the client can display synchronized thermal effects; **multiplayer behavior has not yet been fully tested**.

## How temperature works

Temperate first determines the environmental temperature from the current biome. The equipment's general thermal impact is added, then its cold or heat resistance adjusts the result toward the comfort range.

**Comfort range: 5–14** on the Temperate scale.

| Perceived temperature | Thermal state |
| --- | --- |
| −6 or below | `COLD_III` |
| −5 to −1 | `COLD_II` |
| 0 to 4 | `COLD_I` |
| 5 to 14 | `NORMAL` |
| 15 to 19 | `HOT_I` |
| 20 to 24 | `HOT_II` |
| 25 or above | `HOT_III` |

Cold and heat resistances cannot push temperature beyond their respective comfort boundary. General thermal impact is applied regardless of conditions, so wearing the wrong equipment can make the player too hot or too cold.

### Exposure and effects

Thermal states become **applied** after continuous exposure: level I at **15 seconds**, level II at **20 seconds**, and level III at **30 seconds**, provided the current temperature is severe enough. Returning to `NORMAL` resets exposure, as does switching directly between cold and hot conditions.

| Applied state | Status effects |
| --- | --- |
| `NORMAL` | None |
| `COLD_I` | Slowness I |
| `COLD_II` | Slowness I, Weakness I |
| `COLD_III` | Slowness II, Weakness I |
| `HOT_I` | Hunger I |
| `HOT_II` | Hunger I, Slowness I |
| `HOT_III` | Hunger II, Slowness I |

Effects are refreshed while exposure continues. Existing effects may take a few seconds to expire after conditions improve. A **separate extreme-exposure timer** starts while the player is in level III; after **30 seconds at level III**, damage is applied every **5 seconds** for as long as that extreme exposure continues.

Thermal simulation is suspended in Creative and Spectator modes.

## For addon developers

Temperate Core 1.0.0 provides a Java registry for equipment-based thermal statistics. An addon can register its own items without changing the Core's temperature engine.

```java
import com.tempcore.ProtectionStats;
import com.tempcore.ThermalEquipmentRegistry;

// Example: call after your addon's item registration.
ThermalEquipmentRegistry.register(
        ModItems.ARCTIC_CHESTPLATE,
        new ProtectionStats(3, 5, 0)
);
```

The `ProtectionStats` constructor takes these values **in order**:

| Parameter | Meaning |
| --- | --- |
| `thermalImpact` | General temperature adjustment; may be positive, negative, or zero |
| `coldResistance` | Reduces cold exposure toward the comfort range; must be non-negative |
| `heatResistance` | Reduces heat exposure toward the comfort range; must be non-negative |

Registered stats can be queried with `ThermalEquipmentRegistry.get(item)`. Unregistered items have zero thermal stats. Registration for an existing item **replaces** its earlier stats, so addon load order can matter if multiple mods register the same item.

**API scope in 1.0.0:** stats are attached to the `Item` type, not individual `ItemStack`s, and the temperature engine reads the four vanilla armor slots (head, chest, legs, feet). Dynamic per-item upgrades, JSON datapack registration, and non-armor sources are not part of this release.

Addons should declare a dependency on `temperate_core` and initialize their item registrations before registering thermal stats. **Compatibility with independently developed addons is planned for post-release testing.**

## Current scope and testing

Version 1.0.0 has been built and tested locally for core temperature calculation, thermal effects, persistence, and HUD behavior.

- **Multiplayer:** not yet fully tested.
- **Third-party addon compatibility:** not yet fully tested.
- **Environment:** this release uses biome base temperature; weather, altitude, shelter, and nearby heat sources are not yet included in the environmental calculation.
- **Content:** Temperate Core does not add Arctic or Insulated outfits; specialized equipment is planned for the separate Temperate Addon.

## License

Temperate Core is licensed under the **MIT License**. See the `LICENSE` file for the full terms.

Copyright © 2026 **Physias**.
