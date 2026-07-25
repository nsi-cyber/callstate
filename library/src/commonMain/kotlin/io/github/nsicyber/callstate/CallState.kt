package io.github.nsicyber.callstate

/**
 * Whether the device currently has an active voice call (best effort; see README for platform limits).
 */
sealed interface CallState {
    data object OnCall : CallState
    data object NotOnCall : CallState
}
