package com.aiwa.ssstart
import android.content.Context
import android.os.Build
import kotlin.math.sqrt

object TvSizeDetector {
    fun getSize(context: Context): Int {
        // 1. Pelo modelo Aiwa: AWS-TV-32-BL
        Regex("(\d{2})").find(Build.MODEL)?.value?.toIntOrNull()?.let { return it }
        // 2. Pelo cálculo físico
        val m = context.resources.displayMetrics
        val w = m.widthPixels / m.xdpi
        val h = m.heightPixels / m.ydpi
        val diag = sqrt((w*w + h*h).toDouble())
        return listOf(32,43,50,55,65).minByOrNull { kotlin.math.abs(it - diag) } ?: 32
    }
}
