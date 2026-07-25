package io.github.nsicyber.callstate.internal

import io.github.nsicyber.callstate.CallState
import kotlin.test.Test
import kotlin.test.assertEquals

class IosCallSignalAggregatorTest {

    @Test
    fun aggregatorIsAvailableOnIosTarget() {
        assertEquals(
            CallState.NotOnCall,
            CallSignalAggregator.aggregate(CallSignals()),
        )
    }
}
