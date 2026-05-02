package com.aiwa.ssstart

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(context, "SSStart ativado como admin", Toast.LENGTH_SHORT).show()
    }
    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(context, "SSStart admin desativado", Toast.LENGTH_SHORT).show()
    }
}
