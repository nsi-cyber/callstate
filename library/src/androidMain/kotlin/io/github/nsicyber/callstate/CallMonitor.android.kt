package io.github.nsicyber.callstate

import android.content.Context
import io.github.nsicyber.callstate.internal.AndroidCallSignalSource
import io.github.nsicyber.callstate.internal.toCallState

actual class CallMonitor internal constructor(context: Context) {
    private val source = AndroidCallSignalSource(context)

    actual fun callState() = source.signals().toCallState()
}

actual fun createCallMonitor(platformContext: Any?): CallMonitor {
    require(platformContext is Context) {
        "Android requires a Context as platformContext (use applicationContext)."
    }
    return CallMonitor(platformContext)
}
