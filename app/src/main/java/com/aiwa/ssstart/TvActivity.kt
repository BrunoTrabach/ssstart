package com.aiwa.ssstart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent

class TvActivity : Activity() {
    
    private lateinit var tvInputDetector: TvInputDetector
    private lateinit var inactivityMonitor: InactivityMonitor
    
    // Código secreto: CIMA, CIMA, BAIXO, BAIXO, OK
    private val secretSequence = listOf(
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_CENTER // OK/Enter
    )
    private val inputSequence = mutableListOf<Int>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SSStart", "TvActivity iniciada na TV")
        
        tvInputDetector = TvInputDetector(this)
        
        inactivityMonitor = InactivityMonitor(onTimeout = {
            enterPowerSaveMode()
        })
        
        if (!tvInputDetector.hasSignal()) {
            inactivityMonitor.start()
        }
        
        tvInputDetector.startMonitoring(
            onSignalLost = { inactivityMonitor.start() },
            onSignalFound = { inactivityMonitor.reset() }
        )
        
        openTvApp()
        finish()
    }
    
    private fun openTvApp() {
        val tvIntent = packageManager.getLaunchIntentForPackage("com.google.android.tv")
            ?: packageManager.getLaunchIntentForPackage("com.android.tv")
        tvIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(tvIntent)
    }
    
    private fun enterPowerSaveMode() {
        Log.d("SSStart", "Entrando em modo economia")
        tvInputDetector.switchToHdmi2()
        // TODO: desligar backlight via Settings.System ou intent Aiwa
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Código secreto D-pad: ↑ ↓ ↓ OK
        inputSequence.add(keyCode)
        
        // Mantém só os últimos 5 cliques
        if (inputSequence.size > secretSequence.size) {
            inputSequence.removeAt(0)
        }
        
        if (inputSequence == secretSequence) {
            Log.d("SSStart", "Código secreto acionado - abrindo Settings")
            inputSequence.clear()
            startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
            return true
        }
        
        return super.onKeyDown(keyCode, event)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        tvInputDetector.stopMonitoring()
        inactivityMonitor.stop()
    }
}
