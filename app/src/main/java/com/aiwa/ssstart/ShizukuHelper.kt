package com.aiwa.ssstart

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuHelper {
    
    fun hasPermission(): Boolean {
        return Shizuku.pingBinder() && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    
    fun runShellCommand(cmd: String): String {
        if (!hasPermission()) return "Sem permissão Shizuku"
        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", cmd), null, null)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.readText()
        } catch (e: Exception) {
            "Erro: ${e.message}"
        }
    }
    
    fun setDeviceOwner(): String {
        return runShellCommand("dpm set-device-owner com.aiwa.ssstart/.AdminReceiver")
    }
}
