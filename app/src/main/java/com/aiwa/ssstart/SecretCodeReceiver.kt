package com.aiwa.ssstart
import android.view.KeyEvent

class SecretCodeDetector {
    private val seq = listOf(KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN)
    private var idx = 0
    fun onKey(code: Int): Boolean {
        if (code == seq.getOrNull(idx)) { idx++; if (idx == seq.size) { idx=0; return true } }
        else if (code == KeyEvent.KEYCODE_ENTER || code == KeyEvent.KEYCODE_BACK) { if (idx==seq.size) { idx=0; return true } }
        else idx = 0
        return false
    }
}
