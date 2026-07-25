package io.github.nsicyber.callstate.internal

import io.github.nsicyber.callstate.CallState

internal object CallSignalAggregator {
    fun aggregate(signals: CallSignals): CallState {
        val onCall = signals.telephonyOffHook ||
            signals.telecomInCall ||
            signals.audioInCallMode
        return if (onCall) CallState.OnCall else CallState.NotOnCall
    }
}
