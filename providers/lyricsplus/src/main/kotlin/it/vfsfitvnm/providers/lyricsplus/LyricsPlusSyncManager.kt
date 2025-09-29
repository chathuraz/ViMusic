package it.vfsfitvnm.providers.lyricsplus

import it.vfsfitvnm.providers.lyricsplus.models.LyricLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LyricsPlusSyncManager(
    private val lyrics: List<LyricLine>,
    private val positionProvider: () -> Long
) {
    private val _currentLineIndex = MutableStateFlow(-1)
    val currentLineIndex: StateFlow<Int> = _currentLineIndex.asStateFlow()

    private val _currentWordIndex = MutableStateFlow(-1)
    val currentWordIndex: StateFlow<Int> = _currentWordIndex.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    fun updatePosition() {
        val position = positionProvider()
        _currentPosition.value = position

        val lineIndex = lyrics.indexOfLast { position >= it.startTimeMs }
        _currentLineIndex.value = lineIndex

        if (lineIndex != -1) {
            val words = lyrics[lineIndex].words
            val wordIndex = words.indexOfLast { position >= it.startTimeMs }
            _currentWordIndex.value = wordIndex
        } else {
            _currentWordIndex.value = -1
        }
    }

    fun getLyrics(): List<LyricLine> = lyrics
}
