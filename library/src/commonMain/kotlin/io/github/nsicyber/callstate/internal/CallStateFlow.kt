package io.github.nsicyber.callstate.internal

import io.github.nsicyber.callstate.CallState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private const val SIGNAL_DEBOUNCE_MS = 100L

@OptIn(FlowPreview::class)
internal fun Flow<CallSignals>.toCallState(): Flow<CallState> =
    debounce(SIGNAL_DEBOUNCE_MS)
        .map(CallSignalAggregator::aggregate)
        .distinctUntilChanged()
