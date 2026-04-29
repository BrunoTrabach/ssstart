package com.alphaone.synapseshade.ssstart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_LOCKED_BOOT_COMPLETED) return

        val prefs = context.getSharedPreferences("ssstart_cfg", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("enabled", true)) return

        val targetPkg = prefs.getString("target_pkg", null) ?: return

        // Aiwa PT-BR precisa de delay maior
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                val launch = context.packageManager.getLaunchIntentForPackage(targetPkg)
                launch?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launch)
            } catch (_: Exception) {}
        }, 12000)
    }
}
