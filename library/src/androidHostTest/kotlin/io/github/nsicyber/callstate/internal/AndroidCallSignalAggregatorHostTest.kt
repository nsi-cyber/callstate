package io.github.nsicyber.callstate.internal

import io.github.nsicyber.callstate.CallState
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidCallSignalAggregatorHostTest {

    @Test
    fun aggregatorMatchesCommonModuleBehavior() {
        assertEquals(
            CallState.OnCall,
            CallSignalAggregator.aggregate(CallSignals(audioInCallMode = true)),
        )
    }
}
