package it.vfsfitvnm.providers.lyricsplus.models

import kotlinx.serialization.Serializable

@Serializable
data class LyricWord(
    val text: String,
    val startTimeMs: Long,
    val durationMs: Long
)

@Serializable
data class LyricLine(
    val fullText: String,
    val startTimeMs: Long,
    val durationMs: Long,
    val words: List<LyricWord>
)
