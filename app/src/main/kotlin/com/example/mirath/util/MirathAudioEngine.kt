package com.example.mirath.util

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MirathAudioEngine {
    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val toneGen = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    } catch (e: Exception) {
        null
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
    }

    fun playDiceRoll() {
        if (_isMuted.value) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        } catch (_: Exception) {}
    }

    fun playMove() {
        if (_isMuted.value) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 80)
        } catch (_: Exception) {}
    }

    fun playCapture() {
        if (_isMuted.value) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_DTMF_S, 150)
        } catch (_: Exception) {}
    }

    fun playWin() {
        if (_isMuted.value) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
        } catch (_: Exception) {}
    }
}
