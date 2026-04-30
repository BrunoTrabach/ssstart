package com.aiwa.ssstart
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UsbReceiver: BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        if (Intent.ACTION_MEDIA_MOUNTED == i.action) {
            val path = i.data?.path ?: return
            c.startService(Intent(c, UsbSyncService::class.java).putExtra("path", path))
        }
    }
}
