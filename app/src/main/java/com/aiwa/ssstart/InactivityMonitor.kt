package com.aiwa.ssstart

import android.os.Handler
import android.os.Looper

class InactivityMonitor(
    private val onTimeout: () -> Unit,
    private val onActivityDetected: () -> Unit,
    private val timeoutMs: Long = 30 * 60 * 1000L // 30min
) {
    private val handler = Handler(Looper.getMainLooper())
    private val timeoutRunnable = Runnable { onTimeout() }
    private var isRunning = false

    fun start() {
        if (isRunning) return
        isRunning = true
        reset()
    }

    fun reset() {
        handler.removeCallbacks(timeoutRunnable)
        if (isRunning) handler.postDelayed(timeoutRunnable, timeoutMs)
        onActivityDetected()
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(timeoutRunnable)
    }
}
