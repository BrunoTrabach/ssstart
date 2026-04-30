package com.aiwa.ssstart
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent

class MainActivity: Activity() {
    private val secret = SecretCodeDetector()
    override fun onCreate(b: Bundle?) { super.onCreate(b); finish() }
    override fun onKeyDown(k: Int, e: KeyEvent): Boolean {
        if (secret.onKey(k)) { /* abrir configurações */ }
        return super.onKeyDown(k, e)
    }
}
