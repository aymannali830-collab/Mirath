# Mirath Product and Engineering Blueprint

## Scope and product thesis

Mirath is a cultural heritage and gameplay platform for discovering traditional and forgotten games from around the world, learning their history, and playing them live. The project appears to exist in two complementary forms:

- `aymannali830-collab/Mirath`: a Kotlin-native Android app focused on the mobile experience.
- `aymannali830-collab/Mirath-`: a broader product/platform implementation with Kotlin, JavaScript, HTML, and CSS, suggesting a richer web or hybrid experience.

Together, they form a coherent product family: a discovery-first, educational, culturally grounded game platform with strong historical and identity value.

## Strategic interpretation

### Repository split

- Mobile app repo (`Mirath`): likely the direct user-facing mobile experience, optimized for gameplay on Android devices.
- Platform repo (`Mirath-`): likely the broader product and content layer, including richer product surfaces and multi-stack interfaces.

### Shared product mission

- Discover traditional and forgotten games globally.
- Learn the historical significance and cultural context of each game.
- Distinguish between documented, reconstructed, disputed, and modern adaptation.
- Play live in a modern experience built around trust, fairness, and accessibility.

## Product goals

### Core product outcomes

- A discovery platform for historical and traditional games.
- A cultural heritage archive with educational framing.
- A playable game platform that supports both local and online interaction where appropriate.
- A reliable local-first experience that remains useful even without network access.
- A trust model based on provenance, safety, and reproducible match integrity.

### Business and UX goals

- Make the app feel like a refined cultural archive combined with a modern game platform.
- Preserve rich historical value without turning the app into a cluttered dashboard.
- Keep Arabic and RTL workflows first-class, not secondary.
- Prioritize playability, educational clarity, and safe state handling.

## Product architecture

### 1. Content layer

Responsibilities:
- game metadata
- cultural origin and historical period
- rules certainty and source provenance
- article and heritage details
- image/audio/icon licensing metadata
- localization strings and language-specific labels

Required metadata per game:
- unique stable ID
- slug for routing and deep links
- Arabic title
- English title
- other established names or transliterations
- region/culture association
- historical period and certainty label
- player count
- estimated duration
- difficulty
- category
- required equipment
- rules summary
- setup instructions
- legal moves
- win/draw/capture rules
- source metadata
- play mode support
- playable/informational status

### 2. Catalog layer

Responsibilities:
- search and filtering
- category organization
- player-count filtering
- difficulty filtering
- cultural/heritage filtering
- historical period filtering
- search normalization for Arabic input and transliteration variants
- distinction between playable and informational content

Required catalog behaviors:
- keep filters composable and resettable
- support Arabic/Latin text search
- normalize diacritics and common Arabic variants carefully
- avoid presenting a game as complete when historical information is uncertain

### 3. Game engine layer

Responsibilities:
- deterministic game state transitions
- legal move validation
- turn handling
- win/loss/draw detection
- input validation and replay safety
- saved session restoration
- reset/rematch behavior
- replay or undo semantics only when safe
- AI logic when supported

Required engine rules:
- explicit initial state
- clear legal moves
- deterministic state transitions
- no duplicate move processing
- no silent state overwrite on activity recreation
- safe handling of invalid actions
- serialization for saved games
- clear policy for partial or interrupted matches

### 4. Persistence layer

Responsibilities:
- local game session persistence
- offline match continuation
- local achievements and progress
- settings and preferences
- pending/synced/failed sync state tracking
- protect against duplicate analytics events

Required persistence behavior:
- save completed matches locally before remote sync attempts
- assign stable unique IDs to every completed match
- record sync state, timestamp, and last error
- support local restoration after process death, low-memory events, app restarts, and upgrades
- include backup and recovery policy documentation
- never promise a backup mechanism that is not actually recoverable

### 5. Sync and account layer

Responsibilities:
- Firebase auth or alternate supported login strategy
- secure per-user profile and leaderboard data
- anonymized or local-only play when appropriate
- safe account linking and conversion
- account recovery and deletion workflows

Required behaviors:
- verify authorization at backend, not only UI
- prevent cross-account data access
- protect user statistics and achievements from unauthorized modification
- always preserve local data when switching accounts or signing out
- avoid public IDs or recovery codes as sole authentication secrets

### 6. Security and privacy layer

Responsibilities:
- audit Firebase rules and backend validations
- protect against unauthorized modifications and data tampering
- minimize logs and avoid collection of sensitive personal data
- keep signing and credentials out of repository content
- review exported components, deep links, dependencies, and network security

### 7. Accessibility and localization layer

Responsibilities:
- Arabic-first design and RTL compatibility
- English support and mixed-language readability
- large text support and touch target usability
- accessible screen state announcements
- no color-only state indicator

Required behaviors:
- localized strings in resource files
- preserve board orientation without altering game rules
- test small-screen and enlarged-font layouts
- ensure Play, reset, resume, and help controls are screen-reader friendly

## Required features and capabilities

### A. Offline-first functionality

The application must remain useful offline for:
- opening the app
- browsing locally available catalog content
- searching local games
- reading local article or rules content
- starting supported games
- local human-vs-human gameplay where applicable
- local AI gameplay where implemented
- saving and restoring match progress
- viewing local achievements and stats
- adjusting local preferences

The app should show clear state indicators for:
- offline mode
- pending sync
- synchronized successfully
- sync failed
- authentication required
- invalid or expired session

### B. Data integrity and recovery

Critical rules:
- completed match results are saved locally before remote sync attempts
- each match gets a stable UUID
- completion and achievement processing is idempotent
- duplicates cannot create multiple wins or achievements
- interrupted writes and partial state are handled safely
- unfinished sessions are restored with complete necessary state
- app upgrades and force-stops do not corrupt local state
- migration errors are handled without destructive automatic database resets

### C. Failure and empty states

Every significant screen must handle:
- loading
- empty data
- partial data
- missing resources
- offline state
- permission denied
- authentication expiration
- storage failure
- network failure
- remote service failure
- invalid input
- recoverable and unrecoverable errors

Messages must be actionable and localized.

### D. Observability and diagnostics

Requirements:
- privacy-conscious logging
- no tokens or secrets in logs
- record app version and feature context for failures
- differentiate expected user errors from application defects
- provide a support or bug-report flow
- document reproduction steps for maintainers

### E. Statistics and competitive integrity

Requirements:
- distinguish local, client-reported, and server-verified results
- define exactly when a match counts as completed
- exclude invalid or interrupted matches
- prevent duplicate completion events
- avoid fabricated or unchecked “live analytics” claims
- make achievement rules deterministic and testable

### F. Content provenance and legal compliance

Requirements:
- preserve source metadata for historical claims
- distinguish documented evidence, scholarly reconstruction, and debatable origin
- track licensing for images, icons, audio, and article reuse
- treat public availability as insufficient proof of reuse rights
- keep privacy disclosures consistent with actual behavior

### G. Resource, performance, and release safety

Requirements:
- avoid repeated network requests from recomposition
- bound retries and AI compute costs
- gracefully handle low storage, low memory, and image decoding failures
- release lifecycle-bound resources properly
- measure performance on lower-end devices
- validate debug and release builds separately
- verify migrations and account restoration before release

## Catalog target

The product should aim for at least 20 meaningful and documented games, not decorative entries.

At minimum, the catalog should make room for categories such as:
- ancient and reconstructed games
- board games
- strategy games
- mancala games
- card games
- dice games
- alignment games
- race games
- traditional regional games

Examples of candidate coverage include:
- Royal Game of Ur
- Senet
- Mehen
- Hounds and Jackals
- Twenty Squares
- Nine Men’s Morris
- Three Men’s Morris
- Alquerque
- Go
- Chess
- Checkers
- Backgammon
- Fanorona
- Tafl-family games
- Pachisi
- Mancala-family games
- regional and traditional board games

Important: each entry must be labeled honestly as documented, reconstructed, disputed, or modern adaptation. No game should be presented as fully known if the historical evidence is uncertain.

## Engineering backlog

### P0: trust and reliability
- Gradle wrapper and build verification
- dependency validation and plugin compatibility
- app/package identity cleanup
- local-first persistence and safe migration
- match deduplication and sync-state integrity
- session restoration and process-death recovery
- secure Firebase/auth review

### P1: product correctness
- complete game catalog model
- 20+ meaningful games with metadata and rules provenance
- legal move validation and deterministic tests
- win/draw logic and session serialization
- offline content and saves
- AI safety/move validity where AI exists

### P2: UX and accessibility
- Arabic-first RTL behavior
- English compatibility and mixed-language readability
- large-text and small-screen stress testing
- TalkBack and contrast review
- clear empty/loading/error states

### P3: content and platform scale
- article and learning center quality
- richer map and timeline experiences
- stronger sync strategy and API boundaries
- user profile and stat design
- support and issue reporting flow

### P4: release quality
- regression tests for all critical issues
- release candidate validation
- upgrade migration tests
- privacy and legal review
- app store compliance check

## Recommended roadmap

### Phase 1: foundation
- finalize canonical game model
- stabilize build environment
- add local-first persistence
- harden session and match recovery

### Phase 2: game depth
- implement 20+ games with real metadata and rule structure
- create deterministic rules engine tests
- add session lifecycle controls

### Phase 3: trust and security
- review Firebase auth, rules, and account flows
- add secure sync and dedupe logic
- protect statistics and achievements against invalid client submissions

### Phase 4: release quality
- complete accessibility and localization review
- add empty/loading/error states across screens
- verify offline, failure, migration, and recovery flows

## Final assessment

Mirath is a compelling product concept with strong cultural and educational value. The combination of a native app and a broader platform implementation suggests a clear vision: an interactive heritage game platform that blends education, exploration, and play.

The next stage is not cosmetic iteration. It is disciplined engineering: trust, correctness, persistence, and provenance must come first. If those are solved well, the app can become a meaningful and differentiated product rather than a clever prototype.
