# callstate

Kotlin Multiplatform library that observes whether the **device** is on an active voice call (**Android** and **iOS**). Monitoring runs only while a collector is active on `callState()`.

**Maven:** `io.github.nsi-cyber:callstate`  
**Latest:** [1.0.0 on Maven Central](https://central.sonatype.com/artifact/io.github.nsi-cyber/callstate/1.0.0)

## Dependency

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.nsi-cyber:callstate:1.0.0")
}
```

## Usage

```kotlin
import io.github.nsicyber.callstate.CallState
import io.github.nsicyber.callstate.createCallMonitor

createCallMonitor(platformContext).callState().collect { state ->
    when (state) {
        CallState.OnCall -> { /* ... */ }
        CallState.NotOnCall -> { /* ... */ }
    }
}
```

**Android** — pass `applicationContext` (or any `Context`). Request [`READ_PHONE_STATE`](library/src/commonMain/kotlin/io/github/nsicyber/callstate/CallMonitorPermissions.kt) at runtime for reliable telephony signals. The library merges the permission in its manifest; your app must still request it.

**iOS** — no context required:

```kotlin
createCallMonitor(null).callState().collect { /* ... */ }
```

Cancel the coroutine scope (or stop collecting) to unregister listeners.

## Limitations

Detection is **best effort**, especially for third-party VoIP.

| Platform | Works well | Often missed |
|----------|------------|--------------|
| **Android** | Cellular; many calls via `TelecomManager` (API 31+) | VoIP outside Telecom; no permission → always `NotOnCall` |
| **iOS** | CallKit-visible calls (`CXCallObserver`) | Non–CallKit VoIP; limited Simulator testing |

## Development

```bash
./gradlew :library:testAndroidHostTest
./gradlew :library:iosSimulatorArm64Test   # macOS + Xcode
```

Releases are published to Maven Central via GitHub Actions (`.github/workflows/publish.yml`) on GitHub Release or manual workflow dispatch.

## License

Apache License 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).
