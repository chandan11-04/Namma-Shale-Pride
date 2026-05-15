package com.nammashale.shalepride.util

/**
 * Simple keyword-based sentiment analyzer for parent feedback.
 * No external API or library needed — runs entirely on-device.
 */
object SentimentAnalyzer {

    private val positiveWords = setOf(
        "good", "great", "excellent", "amazing", "wonderful", "fantastic",
        "happy", "love", "like", "best", "nice", "awesome", "perfect",
        "thanks", "thank", "appreciate", "helpful", "clean", "improve",
        "improved", "better", "well", "proud", "brilliant", "outstanding",
        // Kannada positive words (transliterated)
        "chennaagide", "olleya", "thumba", "santhoshha", "dhanyavada"
    )

    private val negativeWords = setOf(
        "bad", "poor", "terrible", "horrible", "worst", "hate", "dislike",
        "dirty", "unhappy", "problem", "issue", "complaint", "wrong",
        "broken", "fail", "failed", "upset", "angry", "disappointed",
        "not good", "not clean", "no water", "no food", "missing",
        "absent", "late", "delay", "delayed", "never", "always wrong",
        // Kannada negative words (transliterated)
        "ketta", "samasyeide", "beda", "thumba ketta"
    )

    const val POSITIVE = "POSITIVE"
    const val NEUTRAL = "NEUTRAL"
    const val NEGATIVE = "NEGATIVE"

    fun analyze(text: String): String {
        val lower = text.lowercase()
        val words = lower.split(" ", ",", ".", "!", "?", "\n")

        var positiveScore = 0
        var negativeScore = 0

        for (word in words) {
            if (word in positiveWords) positiveScore++
            if (word in negativeWords) negativeScore++
        }

        // Also check full phrases
        for (phrase in negativeWords) {
            if (lower.contains(phrase)) negativeScore++
        }
        for (phrase in positiveWords) {
            if (lower.contains(phrase)) positiveScore++
        }

        return when {
            negativeScore > positiveScore -> NEGATIVE
            positiveScore > negativeScore -> POSITIVE
            else -> NEUTRAL
        }
    }

    fun getEmoji(sentiment: String): String = when (sentiment) {
        POSITIVE -> "🟢"
        NEGATIVE -> "🔴"
        else -> "🟡"
    }

    fun getLabel(sentiment: String): String = when (sentiment) {
        POSITIVE -> "Positive"
        NEGATIVE -> "Negative"
        else -> "Neutral"
    }
}
