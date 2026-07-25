package io.github.nsicyber.callstate.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CallKit.CXCall
import platform.CallKit.CXCallObserver
import platform.CallKit.CXCallObserverDelegateProtocol
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
internal class IosCallSignalSource : CallSignalSource {

    override fun signals(): Flow<CallSignals> = callbackFlow {
        val observer = CXCallObserver()
        val delegate = CallObserverDelegate {
            trySend(CallSignals(telephonyOffHook = observer.hasActiveCalls()))
        }
        observer.setDelegate(delegate, dispatch_get_main_queue())
        trySend(CallSignals(telephonyOffHook = observer.hasActiveCalls()))
        awaitClose {
            observer.setDelegate(null, null)
        }
    }

    private class CallObserverDelegate(
        private val onCallsChanged: () -> Unit,
    ) : NSObject(), CXCallObserverDelegateProtocol {
        override fun callObserver(callObserver: CXCallObserver, callChanged: CXCall) {
            onCallsChanged()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun CXCallObserver.hasActiveCalls(): Boolean =
    calls.any { call ->
        val cxCall = call as? CXCall ?: return@any false
        !cxCall.hasEnded
    }
