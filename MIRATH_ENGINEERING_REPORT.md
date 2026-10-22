# Mirath Engineering Baseline and Hardening Report

## Repository
- Owner: aymannali830-collab
- Name: Mirath
- Default branch: main
- Package namespace: com.example.mirath
- Platform: Native Android
- Language: Kotlin
- UI framework: Jetpack Compose
- Design system: Material 3
- Navigation: Navigation Compose
- Reported backend: Firebase Authentication and Cloud Firestore
- SDK configuration observed in repository: compileSdk 35, targetSdk 35, minSdk 24, Java 17

## Verified baseline findings

### Project health
- The repository contains a legitimate Android app module and Compose-based app structure.
- The build configuration is defined in `build.gradle.kts`, `settings.gradle.kts`, and `gradle/libs.versions.toml`.
- The repository does not currently include a Gradle wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`) in the supplied snapshot.
- This means build reproducibility cannot be confirmed from the repo alone. The project should restore a valid Gradle wrapper before claiming any successful assemble/test/lint run.

### Architecture
- App entry point: `app/src/main/kotlin/com/example/mirath/MainActivity.kt`
- Screen package: `app/src/main/kotlin/com/example/mirath/ui/screens`
- Theme package: `app/src/main/kotlin/com/example/mirath/ui/theme`
- Data package: `app/src/main/kotlin/com/example/mirath/data`
- Utility package: `app/src/main/kotlin/com/example/mirath/util`
- Main screens observed: Home, Catalog, Leaderboard, Achievements, Map, Articles, Stats, About, and a simulator route.
- The app is structured as a Compose-driven navigation app; this is a reasonable starting point.

### Firebase and security posture
- `firestore.rules` exists and grants public read access to `leaderboard_scores` documents:
  - `allow read: if true;`
- Writes are restricted to authenticated users whose UID matches the document's `userId`.
- This can be intentional for a public leaderboard, but it should be explicit product policy rather than assumed.
- Firebase app configuration is not present in the repository snapshot reviewed here, which means any real Firebase auth or Firestore flow requires external configuration.

### App configuration
- Manifest includes:
  - `INTERNET`
  - `VIBRATE`
  - RTL support (`supportsRtl="true"`)
- The app is configured for Arabic UI labels and RTL support.
- The package name remains a default scaffold name: `com.example.mirath`.

## Major gaps against the hardening specification

### 1. Build and project stability
- The repository currently lacks a working Gradle wrapper in the provided source snapshot.
- Without the wrapper, build reproducibility and CI validation are compromised.
- Required next step: restore `gradlew` and validate:
  - `./gradlew assembleDebug`
  - `./gradlew testDebugUnitTest`
  - `./gradlew lintDebug`

### 2. Game catalog completeness
- The repo contains a game catalog and simulator concept, but it does not currently demonstrate a 20-game minimum with individually validated playable engines and historical metadata.
- The app appears to be a prototype or early educational game platform rather than a complete catalog of 20+ well-documented games.
- Needed: a structured `GameDefinition` catalog with unique IDs, metadata, playability state, historical certainty, and rule sources.

### 3. Game engine correctness and state management
- The app includes game simulation behavior, but the repository does not currently show a fully separated domain logic layer with:
  - explicit rules
  - legal move validation
  - win/draw detection
  - deterministic tests
  - match serialization
  - process recreation recovery
  - offline-safe session persistence
- Needed: rules engine independent from Compose UI.

### 4. Offline-first persistence
- The codebase does not show a clear local persistence layer such as Room/DataStore/WorkManager.
- There is no evidence of structured, durable storage for in-progress matches, progress, settings, or local sync.
- Needed: persistent local state for gameplay continuity and offline access.

### 5. Security and privacy hardening
- Real Firebase config is absent from the repo snapshot.
- Firestore rules are minimal and should be documented and reviewed carefully.
- Sensitive user data or progress features should be scoped to authenticated users only where appropriate.

### 6. Test coverage and QA
- JUnit dependencies exist, but there is no evidence of a meaningful automated test suite or validated execution results in the repository snapshot reviewed here.
- Needed: rule-level tests, UI tests, session restoration tests, and regression coverage.

## Recommended implementation order
1. Restore and verify Gradle build wrapper.
2. Add missing Firebase configuration and validate auth/backend integration.
3. Replace placeholder app identity if production is intended.
4. Establish a proper game domain layer and rule models.
5. Add local persistence for in-progress matches and settings.
6. Build a complete catalog of at least 20 games with explicit playability metadata.
7. Add deterministic rule tests.
8. Harden authentication, sync, and offline behavior.
9. Improve accessibility, validation, and UI clarity.

## Final assessment
Mirath is a promising Android Compose app with a strong educational and cultural identity, but it is not yet in a hardened production-ready state. The repository shows a working app scaffold and screen architecture, but it still lacks build reproducibility, a real offline-first persistence model, a complete game catalog, and a robust game-rules domain boundary. The next phase should focus on build health and architectural hardening before feature expansion.

## Evidence files reviewed
- `README.md`
- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle/libs.versions.toml`
- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/kotlin/com/example/mirath/MainActivity.kt`
- `firestore.rules`
- `firebase-blueprint.json`
- `app/src/main/res/values/strings.xml`
- several files under `app/src/main/kotlin/com/example/mirath/ui` and `data` packages

## Status
- Audit: complete
- Build verification: not executed because the Gradle wrapper was not available in the repository snapshot reviewed here
- Implementation commitment: not yet applied beyond repository documentation and audit capture

This report is intentionally conservative and avoids claiming build/test success without actual execution evidence.
