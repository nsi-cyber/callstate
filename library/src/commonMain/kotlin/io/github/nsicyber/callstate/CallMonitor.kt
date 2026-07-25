package io.github.nsicyber.callstate

import kotlinx.coroutines.flow.Flow

/**
 * Observes device call activity. Monitoring runs only while [callState] is collected.
 */
expect class CallMonitor {
    fun callState(): Flow<CallState>
}

/**
 * Creates a [CallMonitor].
 *
 * - **Android:** pass an [android.content.Context] (prefer `applicationContext`).
 * - **iOS:** [platformContext] is ignored and may be null.
 */
expect fun createCallMonitor(platformContext: Any? = null): CallMonitor
