package com.aiwa.ssstart
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        Handler(Looper.getMainLooper()).postDelayed({
            c.startActivity(c.packageManager.getLaunchIntentForPackage("com.fully.kiosk")?.apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        }, 25000)
    }
}
