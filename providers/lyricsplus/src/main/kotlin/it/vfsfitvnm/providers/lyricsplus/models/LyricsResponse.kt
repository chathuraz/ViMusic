package it.vfsfitvnm.providers.lyricsplus.models

import kotlinx.serialization.Serializable

@Serializable
data class LyricsResponse(
    val type: String,
    val lyrics: List<LyricsLineRaw>
)

@Serializable
data class LyricsLineRaw(
    val time: Long,
    val duration: Long,
    val text: String,
    val syllabus: List<LyricsWordRaw>
)

@Serializable
data class LyricsWordRaw(
    val time: Long,
    val duration: Long,
    val text: String
)
