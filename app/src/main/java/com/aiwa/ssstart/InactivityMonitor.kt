package com.aiwa.ssstart
import android.content.Context
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager

class InactivityMonitor(private val ctx: Context) {
    fun shouldPlayDemo(): Boolean {
        val am = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (am.isMusicActive) return false // vídeo com áudio
        val msm = ctx.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        val playing = msm.getActiveSessions(null).any { it.playbackState?.isActive == true }
        if (playing) return false // VLC/Pluto mesmo mudo
        // TODO: checar HDMI via TvInputManager
        return true
    }
}
