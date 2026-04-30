package com.aiwa.ssstart
import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.File

class UsbSyncService: Service() {
    override fun onBind(intent: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val usbPath = intent?.getStringExtra("path") ?: return START_NOT_STICKY
        val syncDir = File(usbPath, "AIWA_SYNC")
        if (!syncDir.exists()) return START_NOT_STICKY
        val codeFile = File(syncDir, "codigo.txt")
        if (!codeFile.exists() || codeFile.readText().trim() != "AIWA2026") return START_NOT_STICKY
        
        val size = TvSizeDetector.getSize(this)
        val listFile = File(syncDir, "videos_${size}.txt")
        val videos = if (listFile.exists()) listFile.readLines() else File(syncDir, "videos/${size}").list()?.toList() ?: emptyList()
        
        val retail = File("/sdcard/Movies/Retail")
        if (!retail.exists()) retail.mkdirs()
        
        videos.forEach { name ->
            val src = File(syncDir, "videos/${size}/$name").takeIf { it.exists() } ?: File(syncDir, "videos/$name")
            if (src.exists()) src.copyTo(File(retail, name), overwrite = true)
        }
        return START_NOT_STICKY
    }
}
