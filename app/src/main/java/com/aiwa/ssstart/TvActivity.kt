package com.aiwa.ssstart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent

class TvActivity : Activity() {
    
    private lateinit var inputDetector: TvInputDetector
    private lateinit var inactivityMonitor: InactivityMonitor
    private val keySequence = mutableListOf<Int>()
    private val secretSequence = listOf(KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_UP, 
                                       KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_DOWN, 
                                       KeyEvent.KEYCODE_DPAD_CENTER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        inputDetector = TvInputDetector(this)
        inactivityMonitor = InactivityMonitor(
            onTimeout = { enterPowerSaveMode() },
            onActivityDetected = { openTvApp() }
        )
        
        if (inputDetector.hasSignal()) {
            openTvApp()
        } else {
            inactivityMonitor.start()
            inputDetector.startMonitoring(
                onSignalLost = { inactivityMonitor.start() },
                onSignalFound = { inactivityMonitor.stop(); openTvApp() }
            )
        }
    }

    private fun openTvApp() {
        val intent = packageManager.getLaunchIntentForPackage("com.google.android.tv")
            ?: packageManager.getLaunchIntentForPackage("com.android.tv")
        intent?.let { startActivity(it) }
        finish()
    }

    private fun enterPowerSaveMode() {
        // 1. Tenta HDMI2 via Shizuku
        ShizukuHelper.runShellCommand("am start -a android.intent.action.VIEW -d hdmi://2")
        // 2. Backlight off via Shizuku  
        ShizukuHelper.runShellCommand("settings put system screen_brightness 0")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        keySequence.add(keyCode)
        if (keySequence.size > 5) keySequence.removeAt(0)
        if (keySequence == secretSequence) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
            keySequence.clear()
            return true
        }
        inactivityMonitor.reset()
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        inputDetector.stopMonitoring()
        inactivityMonitor.stop()
    }
}
