# Temperate Core 1.0.2 — Heat Effects Hotfix

This maintenance release corrects the effects applied during severe heat exposure.

## Fixed

- Fixed the `HOT_III` effect mapping: level III heat exposure now applies **Hunger II and Slowness I**, as intended.
- Replaced the duplicate Hunger effect (`Hunger II + Hunger I`) with the correct Slowness effect.

## Technical

- Improved the GitHub Actions release workflow by building the distribution JAR once and publishing it through independent GitHub, Modrinth, and CurseForge jobs.
- Disabled automatic unfeaturing of older Modrinth versions to avoid the version-edit authorization failure observed during the 1.0.1 release.

## Compatibility

- Minecraft Java Edition **26.3**
- Fabric Loader **0.20.1+** and compatible Fabric API
- Java **25+**
- No intentional changes to temperature thresholds, equipment statistics, player exposure persistence, or the visual HUD.

**Testing note:** Multiplayer behavior and third-party addon compatibility have not yet been fully validated.
