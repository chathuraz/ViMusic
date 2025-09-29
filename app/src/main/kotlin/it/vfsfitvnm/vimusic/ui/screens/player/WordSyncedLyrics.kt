package it.vfsfitvnm.vimusic.ui.screens.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.providers.lyricsplus.LyricsPlusSyncManager
import it.vfsfitvnm.vimusic.ui.modifiers.verticalFadingEdge
import it.vfsfitvnm.vimusic.utils.center
import it.vfsfitvnm.vimusic.utils.medium
import kotlinx.collections.immutable.toImmutableList

@Composable
fun WordSyncedLyrics(
    manager: LyricsPlusSyncManager,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val (colorPalette, typography) = LocalAppearance.current

    val lazyListState = rememberLazyListState()
    val lyrics = manager.getLyrics()

    // Collect the current line index and player position as state
    val currentLineIndex by manager.currentLineIndex.collectAsState()
    val currentPosition by manager.currentPosition.collectAsState()

    // Remember the previous line index to prevent unnecessary scrolling
    val previousLineIndex = remember { mutableIntStateOf(-2) }

    // Effect to handle positioning when component becomes visible or current position changes significantly
    LaunchedEffect(isVisible, currentLineIndex) {
        if (isVisible) {
            val targetIndex = if (currentLineIndex == -1) 0 else currentLineIndex

            val viewHeight = lazyListState.layoutInfo.viewportSize.height
            val itemHeight = lazyListState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == targetIndex + 1 }?.size ?: 0
            val centerOffset = (viewHeight - itemHeight) / 2

            // When becoming visible, immediately scroll to current position
            lazyListState.animateScrollToItem(index = targetIndex + 1, scrollOffset = -centerOffset)
            previousLineIndex.intValue = currentLineIndex
        }
    }

    // This effect triggers when the current line changes during normal playback
    LaunchedEffect(currentLineIndex) {
        // Only scroll if the line index has actually changed and component is visible
        if (isVisible && currentLineIndex != previousLineIndex.intValue) {
            previousLineIndex.intValue = currentLineIndex

            // Use 0 as default when currentLineIndex is -1 (before song starts)
            val targetIndex = if (currentLineIndex == -1) 0 else currentLineIndex

            // Calculate the necessary offset to center the item in the viewport
            val viewHeight = lazyListState.layoutInfo.viewportSize.height
            // Find the specific item's height, default to 0 if not visible
            val itemHeight = lazyListState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == targetIndex + 1 }?.size ?: 0
            val centerOffset = (viewHeight - itemHeight) / 2

            // Animate the scroll to the target item, applying the offset
            // We add 1 to the index to account for the initial Spacer item
            lazyListState.animateScrollToItem(index = targetIndex + 1, scrollOffset = -centerOffset)
        }
    }

    LazyColumn(
        state = lazyListState,
        userScrollEnabled = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .verticalFadingEdge()
            .fillMaxWidth()
    ) {
        // A dynamic spacer that takes up half the screen height
        // This allows the first few lines of the lyrics to be centered properly
        item {
            Spacer(modifier = Modifier.height(lazyListState.layoutInfo.viewportSize.height.dp / 2))
        }

        itemsIndexed(lyrics.toImmutableList()) { lineIndex, line ->
            val isActiveLine = lineIndex == currentLineIndex || (currentLineIndex == -1 && lineIndex == 0)
            // A line is considered "singing" if any of its words are currently active.
            val isSingingLine = line.words.any { word -> currentPosition in word.startTimeMs..(word.startTimeMs + word.durationMs) }

            // Build an annotated string for the entire line to allow for text wrapping
            val annotatedString = buildAnnotatedString {
                line.words.forEach { word ->
                    val isWordActive = currentPosition in word.startTimeMs..(word.startTimeMs + word.durationMs)
                    val isWordPast = currentPosition > (word.startTimeMs + word.durationMs)

                    // Determine the target color of each word based on its timing and line status.
                    val targetColor = when {
                        // If the line is currently being sung, apply full karaoke styling.
                        isSingingLine -> {
                            if (isWordActive || isWordPast) Color.White else Color.White.copy(alpha = 0.6f)
                        }
                        // If it's the main centered line (but not yet singing), show it as upcoming.
                        isActiveLine -> Color.White.copy(alpha = 0.6f)

                        // Otherwise, the line is completely inactive.
                        else -> colorPalette.textDisabled
                    }

                    // Animate the color change to prevent flickering
                    val animatedColor by animateColorAsState(
                        targetValue = targetColor,
                        animationSpec = tween(durationMillis = 300), // Increased duration for smoother transition
                        label = "wordColorAnimation"
                    )

                    withStyle(style = SpanStyle(color = animatedColor)) {
                        append(word.text)
                    }
                }
            }

            BasicText(
                text = annotatedString,
                style = typography.xs.center.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 32.dp)
            )
        }

        // A dynamic spacer for the bottom, allowing the last lines to be centered
        item {
            Spacer(modifier = Modifier.height(lazyListState.layoutInfo.viewportSize.height.dp / 2))
        }
    }
}
