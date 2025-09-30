package it.vfsfitvnm.vimusic.ui.components.modern

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.utils.medium
import it.vfsfitvnm.vimusic.utils.semiBold

/**
 * Modern mini player with enhanced visuals and animations
 */
@Composable
fun ModernMiniPlayer(
    title: String,
    artist: String,
    thumbnailUrl: String?,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    onLikeClick: (() -> Unit)? = null,
    isLiked: Boolean = false
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "miniPlayerScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = colorPalette.background1,
        animationSpec = spring(),
        label = "miniPlayerBackground"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colorPalette.text.copy(alpha = 0.08f),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onExpandClick
            )
    ) {
        Column {
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(colorPalette.text.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(3.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    colorPalette.accent,
                                    colorPalette.accent.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            }
            
            // Player content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Album artwork
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorPalette.background1.copy(alpha = 0.5f)),
                    contentScale = ContentScale.Crop
                )
                
                // Song info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    BasicText(
                        text = title,
                        style = typography.m.semiBold.copy(color = colorPalette.text),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    BasicText(
                        text = artist,
                        style = typography.s.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.7f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onLikeClick != null) {
                        ModernCompactIconButton(
                            iconId = if (isLiked) R.drawable.heart else R.drawable.heart_outline,
                            onClick = onLikeClick,
                            contentDescription = if (isLiked) "Unlike" else "Like"
                        )
                    }
                    
                    ModernCompactIconButton(
                        iconId = if (isPlaying) R.drawable.pause else R.drawable.play,
                        onClick = onPlayPauseClick,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                    
                    ModernCompactIconButton(
                        iconId = R.drawable.play_skip_forward,
                        onClick = onSkipNextClick,
                        contentDescription = "Skip Next"
                    )
                }
            }
        }
    }
}

/**
 * Modern expanded player with rich visuals
 */
@Composable
fun ModernExpandedPlayer(
    title: String,
    artist: String,
    album: String?,
    thumbnailUrl: String?,
    isPlaying: Boolean,
    isShuffling: Boolean,
    repeatMode: Int, // 0: off, 1: all, 2: one
    progress: Float,
    currentTime: String,
    totalTime: String,
    onPlayPauseClick: () -> Unit,
    onSkipPreviousClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onSeek: (Float) -> Unit,
    onCollapseClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLikeClick: (() -> Unit)? = null,
    isLiked: Boolean = false
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorPalette.background0,
                        colorPalette.background1,
                        colorPalette.background2
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Top bar with collapse button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModernIconButton(
                    iconId = R.drawable.chevron_down,
                    onClick = onCollapseClick,
                    contentDescription = "Collapse"
                )
                
                BasicText(
                    text = "Now Playing",
                    style = typography.l.semiBold.copy(color = colorPalette.text)
                )
                
                if (onLikeClick != null) {
                    ModernIconButton(
                        iconId = if (isLiked) R.drawable.heart else R.drawable.heart_outline,
                        onClick = onLikeClick,
                        contentDescription = if (isLiked) "Unlike" else "Like"
                    )
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
            
            // Album artwork
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(280.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = colorPalette.text.copy(alpha = 0.2f)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorPalette.background1.copy(alpha = 0.5f)),
                contentScale = ContentScale.Crop
            )
            
            // Song info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicText(
                    text = title,
                    style = typography.xxl.semiBold.copy(color = colorPalette.text),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                BasicText(
                    text = artist,
                    style = typography.l.medium.copy(
                        color = colorPalette.text.copy(alpha = 0.8f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (album != null) {
                    BasicText(
                        text = album,
                        style = typography.m.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.6f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Progress section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernSeekBar(
                    progress = progress,
                    onSeek = onSeek,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicText(
                        text = currentTime,
                        style = typography.s.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.7f)
                        )
                    )
                    
                    BasicText(
                        text = totalTime,
                        style = typography.s.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.7f)
                        )
                    )
                }
            }
            
            // Control buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Secondary controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModernIconButton(
                        iconId = R.drawable.shuffle,
                        onClick = onShuffleClick,
                        useGradient = isShuffling,
                        contentDescription = "Shuffle"
                    )
                    
                    Spacer(modifier = Modifier.width(48.dp))
                    
                    ModernIconButton(
                        iconId = when (repeatMode) {
                            1 -> R.drawable.repeat
                            2 -> R.drawable.repeat_on
                            else -> R.drawable.repeat
                        },
                        onClick = onRepeatClick,
                        useGradient = repeatMode > 0,
                        contentDescription = "Repeat"
                    )
                }
                
                // Primary controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModernIconButton(
                        iconId = R.drawable.play_skip_back,
                        onClick = onSkipPreviousClick,
                        size = 56,
                        iconSize = 28,
                        contentDescription = "Previous"
                    )
                    
                    ModernPrimaryIconButton(
                        iconId = if (isPlaying) R.drawable.pause else R.drawable.play,
                        onClick = onPlayPauseClick,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                    
                    ModernIconButton(
                        iconId = R.drawable.play_skip_forward,
                        onClick = onSkipNextClick,
                        size = 56,
                        iconSize = 28,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}

/**
 * Modern seek bar component
 */
@Composable
fun ModernSeekBar(
    progress: Float,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val (colorPalette, _) = LocalAppearance.current
    
    Box(
        modifier = modifier
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(colorPalette.text.copy(alpha = 0.2f))
            .clickable(enabled = enabled) { /* Handle seek */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(4.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            colorPalette.accent,
                            colorPalette.accent.copy(alpha = 0.8f)
                        )
                    )
                )
        )
        
        // Thumb
        Box(
            modifier = Modifier
                .size(12.dp)
                .shadow(2.dp, CircleShape)
                .background(
                    colorPalette.accent,
                    CircleShape
                )
                .align(Alignment.CenterStart)
        )
    }
}
