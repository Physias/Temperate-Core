# Temperate Core — Release workflow

These workflows replace the old combined `publish.yml` and the version-specific `publish-recovery.yml`.

Keep `build.yml`: it continues to compile the project on pushes to `main`.

## GitHub Release (automatic)

1. Make sure `TemperatureEffects.java` and all other source changes are committed in the GitHub repo.
2. Update the mod version, `CHANGELOG.md`, and **`RELEASE_NOTES.md`** (must include `Temperate Core X.Y.Z`).
3. Test in game and run `./gradlew.bat clean build` locally with Java 25.
4. Commit and push all changes to `main`.
5. Create and push an annotated tag `vX.Y.Z`.
6. `release-github.yml` builds with Java 25, validates the JAR metadata, creates a `.sha256` checksum file, and publishes both assets as one GitHub Release.

The GitHub Release workflow refuses to overwrite an existing release. Do not delete or retarget existing published tags.

## Modrinth and CurseForge (manual, separate from GitHub)

1. After GitHub Release is published, open **Actions → Release Temperate Core - Modrinth and CurseForge → Run workflow**.
2. Choose `main` for the workflow branch, enter version `X.Y.Z` **without `v`**, and choose `both`, `modrinth`, or `curseforge`.
3. Verify the selected platform(s) do **not** already have this version before submitting. The workflow does not guarantee an idempotent upload.
4. This workflow downloads the **exact GitHub Release JAR**, checks `fabric.mod.json`, ZIP integrity, release notes, and SHA-256 if provided, then publishes it using MC-Publish.
5. The Modrinth job does not attempt to unfeature older releases (`modrinth-featured: false`, `modrinth-unfeature-mode: none`).

If one destination fails, inspect its dashboard **before** rerunning. Choose the failed destination only; do not republish to a successful destination.

## Required repository settings

GitHub **Repository variables**:
- `MODRINTH_PROJECT_ID`
- `CURSEFORGE_PROJECT_ID`

GitHub **Repository secrets**:
- `MODRINTH_TOKEN`
- `CURSEFORGE_TOKEN`

No new GitHub token is needed; GitHub Actions supplies `GITHUB_TOKEN`.

## Migration

Delete these old files from `.github/workflows/`, if they exist:
- `publish.yml`
- `publish-recovery.yml`
- `temperate_core_1_0_2_recovery.yml`

Add:
- `.github/workflows/release-github.yml`
- `.github/workflows/release-platforms.yml`

**Do not recreate or rerun the `v1.0.2` publication**. The two replacement workflows are for the next version and any explicitly chosen manual distribution.
