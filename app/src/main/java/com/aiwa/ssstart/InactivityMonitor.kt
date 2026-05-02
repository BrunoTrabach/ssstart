package com.aiwa.ssstart

import android.os.Handler
import android.os.Looper
import android.util.Log

class InactivityMonitor(
    private val timeoutMs: Long = 30 * 60 * 1000L, // 30 min
    private val onTimeout: () -> Unit
) {
    private val handler = Handler(Looper.getMainLooper())
    private val timeoutRunnable = Runnable {
        Log.d("Inactivity", "30min sem sinal - acionando modo economia")
        onTimeout()
    }

    fun start() {
        stop() // garante que não tem 2 rodando
        handler.postDelayed(timeoutRunnable, timeoutMs)
        Log.d("Inactivity", "Monitor iniciado: ${timeoutMs}ms")
    }

    fun reset() {
        Log.d("Inactivity", "Sinal voltou - resetando timer")
        start()
    }

    fun stop() {
        handler.removeCallbacks(timeoutRunnable)
        Log.d("Inactivity", "Monitor parado")
    }
}
