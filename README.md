# CallState (Kotlin Multiplatform)

Observe whether the **device** is on an active voice call on **Android** and **iOS**. The library exposes a simple cold `Flow` that runs platform listeners **only while you collect**.

Published coordinates: `io.github.nsi-cyber:callstate`

## Quick start

**Common API**

```kotlin
import io.github.nsicyber.callstate.CallState
import io.github.nsicyber.callstate.createCallMonitor

// In a coroutine scope (e.g. viewModelScope):
createCallMonitor(platformContext).callState().collect { state ->
    when (state) {
        CallState.OnCall -> { /* ... */ }
        CallState.NotOnCall -> { /* ... */ }
    }
}
```

**Android** — pass a `Context` (prefer `applicationContext`):

```kotlin
val monitor = createCallMonitor(applicationContext)
```

Request `READ_PHONE_STATE` at runtime before expecting accurate results (see [CallMonitorPermissions.ANDROID_READ_PHONE_STATE](library/src/commonMain/kotlin/io/github/nsicyber/callstate/CallMonitorPermissions.kt)). The library manifest merges `READ_PHONE_STATE`; your app must still request it.

**iOS** — context is not required:

```kotlin
val monitor = createCallMonitor(null)
```

Cancel collection (or cancel the parent scope) to unregister listeners.

## What “on call” means (limitations)

Detection is **best effort**, especially for third-party VoIP. Do not expect default-dialer accuracy.

| Platform | Detects well | Often missed or flaky |
|----------|----------------|------------------------|
| **Android** | Cellular calls; many calls via `TelecomManager` (API 31+) | VoIP apps that bypass Telecom; OEM differences; no permission → always `NotOnCall` |
| **iOS** | Calls visible to **CallKit** (`CXCallObserver`) | VoIP that never integrates CallKit; limited Simulator behavior |

Signals on Android combine telephony off-hook, `TelecomManager.isInCall()` (API 31+), and audio modes `IN_CALL` / `IN_COMMUNICATION`.

## Manual test checklist

- Cellular incoming / outgoing call
- Native Phone / FaceTime (iOS)
- A CallKit-integrated VoIP app
- A non-CallKit VoIP app (expect `NotOnCall` on iOS; variable on Android)

## Build

```bash
./gradlew :library:testAndroidHostTest
./gradlew :library:iosSimulatorArm64Test   # macOS + Xcode
```

## Publishing

Maven Central is configured via the Vanniktech plugin. Set POM license, developer, and signing secrets in `library/build.gradle.kts` before release. For `io.github.nsi-cyber`, verify repository ownership when registering with Central.

## License

See [LICENSE](LICENSE).
