package io.github.nsicyber.callstate

/**
 * Permissions and privacy notes for integrators.
 *
 * **Android:** declare and request [ANDROID_READ_PHONE_STATE] at runtime before expecting
 * accurate results. Without it, the library emits [CallState.NotOnCall].
 *
 * **iOS:** no special entitlement is required to observe CallKit-visible calls. If you log or
 * transmit call state, describe it in your App Store privacy questionnaire.
 */
object CallMonitorPermissions {
    const val ANDROID_READ_PHONE_STATE: String = "android.permission.READ_PHONE_STATE"
}
