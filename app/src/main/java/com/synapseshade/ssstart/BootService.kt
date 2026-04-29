package com.synapseshade.ssstart

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat

class BootService : Service() {
    private val CHANNEL_ID = "ssstart_boot"
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SSStart")
            .setContentText("Preparando início automático...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        startForeground(1, notification)

        val prefs = getSharedPreferences("ssstart_prefs", Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("autostart_enabled", true)
        val pkg = prefs.getString("autostart_package", null)
        val delay = prefs.getString("autostart_delay", "25")?.toIntOrNull() ?: 25

        Log.d("SSStart", "BootService iniciado - enabled=$enabled pkg=$pkg delay=$delay")

        if (enabled && !pkg.isNullOrEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                launchAppWithRetry(pkg, 3)
            }, delay * 1000L)
        } else {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun launchAppWithRetry(packageName: String, attempts: Int) {
        var tries = 0
        val handler = Handler(Looper.getMainLooper())
        
        val runnable = object : Runnable {
            override fun run() {
                tries++
                try {
                    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(launchIntent)
                        Log.d("SSStart", "App lançado com sucesso: $packageName")
                        stopSelf()
                        return
                    }
                } catch (e: Exception) {
                    Log.e("SSStart", "Erro ao lançar", e)
                }
                
                if (tries < attempts) {
                    Log.d("SSStart", "Tentativa $tries falhou, tentando novamente em 5s")
                    handler.postDelayed(this, 5000)
                } else {
                    Log.e("SSStart", "Falhou após $attempts tentativas")
                    stopSelf()
                }
            }
        }
        handler.post(runnable)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "SSStart Boot", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
