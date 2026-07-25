package io.github.nsicyber.callstate.internal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.telecom.TelecomManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class AndroidCallSignalSource(
    private val context: Context,
) : CallSignalSource {

    override fun signals(): Flow<CallSignals> = callbackFlow {
        val appContext = context.applicationContext
        if (!hasReadPhoneStatePermission(appContext)) {
            trySend(CallSignals())
            awaitClose { }
            return@callbackFlow
        }

        val telephonyManager =
            appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val audioManager =
            appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val telecomManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            appContext.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        } else {
            null
        }

        var telephonyOffHook = isTelephonyOffHook(telephonyManager)
        var audioInCall = isAudioInCallMode(audioManager)
        var telecomInCall = isTelecomInCall(telecomManager)

        fun emitCurrent() {
            trySend(
                CallSignals(
                    telephonyOffHook = telephonyOffHook,
                    telecomInCall = telecomInCall,
                    audioInCallMode = audioInCall,
                ),
            )
        }

        val handler = Handler(Looper.getMainLooper())
        var telephonyCallback: TelephonyCallback? = null
        var phoneStateListener: PhoneStateListener? = null
        var modeListener: AudioManager.OnModeChangedListener? = null

        handler.post {
            emitCurrent()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val callback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                    override fun onCallStateChanged(state: Int) {
                        telephonyOffHook = state == TelephonyManager.CALL_STATE_OFFHOOK
                        telecomInCall = isTelecomInCall(telecomManager)
                        audioInCall = isAudioInCallMode(audioManager)
                        emitCurrent()
                    }
                }
                telephonyCallback = callback
                telephonyManager.registerTelephonyCallback(appContext.mainExecutor, callback)

                val listener = AudioManager.OnModeChangedListener { mode ->
                    audioInCall = isAudioInCallMode(audioManager, mode)
                    telecomInCall = isTelecomInCall(telecomManager)
                    emitCurrent()
                }
                modeListener = listener
                audioManager.addOnModeChangedListener(appContext.mainExecutor, listener)
            } else {
                @Suppress("DEPRECATION")
                val listener = object : PhoneStateListener() {
                    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                        telephonyOffHook = state == TelephonyManager.CALL_STATE_OFFHOOK
                        audioInCall = isAudioInCallMode(audioManager)
                        telecomInCall = isTelecomInCall(telecomManager)
                        emitCurrent()
                    }
                }
                phoneStateListener = listener
                @Suppress("DEPRECATION")
                telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
            }
        }

        awaitClose {
            handler.post {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    telephonyCallback?.let { telephonyManager.unregisterTelephonyCallback(it) }
                    modeListener?.let { audioManager.removeOnModeChangedListener(it) }
                } else {
                    phoneStateListener?.let { listener ->
                        @Suppress("DEPRECATION")
                        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
                    }
                }
            }
        }
    }

    private fun hasReadPhoneStatePermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    @Suppress("DEPRECATION")
    private fun isTelephonyOffHook(telephonyManager: TelephonyManager): Boolean =
        telephonyManager.callState == TelephonyManager.CALL_STATE_OFFHOOK

    private fun isTelecomInCall(telecomManager: TelecomManager?): Boolean {
        if (telecomManager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return false
        }
        return telecomManager.isInCall
    }

    private fun isAudioInCallMode(
        audioManager: AudioManager,
        mode: Int = audioManager.mode,
    ): Boolean =
        mode == AudioManager.MODE_IN_CALL || mode == AudioManager.MODE_IN_COMMUNICATION
}
