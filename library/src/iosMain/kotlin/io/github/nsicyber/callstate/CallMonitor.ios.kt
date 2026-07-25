package io.github.nsicyber.callstate

import io.github.nsicyber.callstate.internal.IosCallSignalSource
import io.github.nsicyber.callstate.internal.toCallState

actual class CallMonitor {
    private val source = IosCallSignalSource()

    actual fun callState() = source.signals().toCallState()
}

actual fun createCallMonitor(platformContext: Any?): CallMonitor = CallMonitor()
