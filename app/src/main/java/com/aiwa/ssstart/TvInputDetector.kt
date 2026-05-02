package com.aiwa.ssstart

import android.content.Context
import android.media.tv.TvInputInfo
import android.media.tv.TvInputManager
import android.util.Log

class TvInputDetector(private val context: Context) {
    
    private val tvInputManager = context.getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager
    private var currentCallback: TvInputManager.TvInputCallback? = null
    
    fun hasSignal(): Boolean {
        val inputs = tvInputManager.tvInputList
        val hdmi1 = inputs.find { it.id.contains("HDMI1", ignoreCase = true) }
        return hdmi1?.let { tvInputManager.getInputState(it.id) == TvInputManager.INPUT_STATE_CONNECTED } ?: false
    }
    
    fun startMonitoring(onSignalLost: () -> Unit, onSignalFound: () -> Unit) {
        currentCallback = object : TvInputManager.TvInputCallback() {
            override fun onInputStateChanged(inputId: String, state: Int) {
                if (inputId.contains("HDMI1", ignoreCase = true)) {
                    when (state) {
                        TvInputManager.INPUT_STATE_CONNECTED -> onSignalFound()
                        TvInputManager.INPUT_STATE_DISCONNECTED -> onSignalLost()
                    }
                }
            }
        }
        tvInputManager.registerCallback(currentCallback, null)
    }
    
    fun stopMonitoring() {
        currentCallback?.let { tvInputManager.unregisterCallback(it) }
    }
    
    fun switchToHdmi2() {
        // TODO: Intent específico da Aiwa pra trocar input
        Log.d("TvInputDetector", "Switch to HDMI2 - implement Aiwa intent")
    }
}
