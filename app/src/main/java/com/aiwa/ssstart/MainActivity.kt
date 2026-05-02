package com.aiwa.ssstart

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "SSStart v2.0-dev\n\nApp de TV rodando no celular para teste.\n\nModelo: ${android.os.Build.MODEL}\nTamanho detectado: ${TvSizeDetector.getSize(this)}\""
        tv.textSize = 18f
        tv.setPadding(64, 64, 64, 64)
        setContentView(tv)
    }
}
