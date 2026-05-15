package com.nammashale.shalepride.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Helper for Text-to-Speech functionality.
 * Manages TTS engine lifecycle — init once, speak many times.
 */
class VoiceHelper(context: Context) {

    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isReady = true
                // Default to English; Kannada TTS availability varies per device
                tts?.language = Locale.ENGLISH
            } else {
                Log.w("VoiceHelper", "TTS initialization failed: $status")
            }
        }
    }

    /**
     * Speaks the given text. Call after TTS is ready.
     * @param text The text to speak aloud
     * @param languageCode "en" for English, "kn" for Kannada (if device supports it)
     */
    fun speak(text: String, languageCode: String = "en") {
        if (!isReady) return
        val locale = if (languageCode == Constants.LANG_KANNADA) {
            Locale("kn", "IN")
        } else {
            Locale.ENGLISH
        }
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.language = Locale.ENGLISH // fallback
        }
        tts?.stop()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "shale_tts_${System.currentTimeMillis()}")
    }

    /** Stop currently playing speech */
    fun stop() {
        tts?.stop()
    }

    /** Release resources — call in onDestroyView() */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }

    fun isReady() = isReady
}
