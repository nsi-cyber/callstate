package io.github.nsicyber.callstate.internal

import io.github.nsicyber.callstate.CallState
import kotlin.test.Test
import kotlin.test.assertEquals

class CallSignalAggregatorTest {

    @Test
    fun `returns NotOnCall when no signals active`() {
        assertEquals(
            CallState.NotOnCall,
            CallSignalAggregator.aggregate(CallSignals()),
        )
    }

    @Test
    fun `returns OnCall when telephony off hook`() {
        assertEquals(
            CallState.OnCall,
            CallSignalAggregator.aggregate(CallSignals(telephonyOffHook = true)),
        )
    }

    @Test
    fun `returns OnCall when telecom in call`() {
        assertEquals(
            CallState.OnCall,
            CallSignalAggregator.aggregate(CallSignals(telecomInCall = true)),
        )
    }

    @Test
    fun `returns OnCall when audio in call mode`() {
        assertEquals(
            CallState.OnCall,
            CallSignalAggregator.aggregate(CallSignals(audioInCallMode = true)),
        )
    }

    @Test
    fun `returns OnCall when any combination of signals is active`() {
        assertEquals(
            CallState.OnCall,
            CallSignalAggregator.aggregate(
                CallSignals(
                    telephonyOffHook = true,
                    telecomInCall = true,
                    audioInCallMode = true,
                ),
            ),
        )
    }
}
