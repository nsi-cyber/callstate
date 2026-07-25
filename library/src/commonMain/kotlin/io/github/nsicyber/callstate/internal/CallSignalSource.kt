package io.github.nsicyber.callstate.internal

import kotlinx.coroutines.flow.Flow

internal fun interface CallSignalSource {
    fun signals(): Flow<CallSignals>
}
