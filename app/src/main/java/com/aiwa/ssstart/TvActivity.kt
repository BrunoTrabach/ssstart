package com.aiwa.ssstart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent

class TvActivity : Activity() {
    
    private lateinit var tvInputDetector: TvInputDetector
    private lateinit var inactivityMonitor: InactivityMonitor
    private var secretCode = ""
    private val targetCode = "5858"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SSStart", "TvActivity iniciada na TV")
        
        tvInputDetector = TvInputDetector(this)
        
        // Se HDMI1 não tem sinal, já inicia o monitor de 30min
        inactivityMonitor = InactivityMonitor(onTimeout = {
            enterPowerSaveMode()
        })
        
        if (!tvInputDetector.hasSignal()) {
            inactivityMonitor.start()
        }
        
        // Ouve mudanças no HDMI1
        tvInputDetector.startMonitoring(
            onSignalLost = { inactivityMonitor.start() },
            onSignalFound = { inactivityMonitor.reset() }
        )
        
        // Tenta abrir app da TV/Antena
        openTvApp()
        finish() // fecha a activity, só roda em background
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
        // Código secreto 5858 abre Settings
        when (keyCode) {
            KeyEvent.KEYCODE_5 -> secretCode += "5"
            KeyEvent.KEYCODE_8 -> secretCode += "8"
            else -> secretCode = ""
        }
        
        if (secretCode.contains(targetCode)) {
            secretCode = ""
            startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
            return true
        }
        
        if (secretCode.length > 4) secretCode = secretCode.takeLast(4)
        return super.onKeyDown(keyCode, event)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        tvInputDetector.stopMonitoring()
        inactivityMonitor.stop()
    }
}
