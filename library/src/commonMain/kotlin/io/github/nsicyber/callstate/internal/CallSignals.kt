package io.github.nsicyber.callstate.internal

internal data class CallSignals(
    val telephonyOffHook: Boolean = false,
    val telecomInCall: Boolean = false,
    val audioInCallMode: Boolean = false,
)
