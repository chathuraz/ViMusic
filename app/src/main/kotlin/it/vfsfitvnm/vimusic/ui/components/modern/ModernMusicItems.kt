package it.vfsfitvnm.vimusic.ui.components.modern

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import it.vfsfitvnm.vimusic.utils.medium
import it.vfsfitvnm.vimusic.utils.semiBold

/**
 * Modern song item card with enhanced visuals and Material 3 styling
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernSongItem(
    title: String,
    artist: String,
    thumbnailUrl: String?,
    duration: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    isPlaying: Boolean = false,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "songItemScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPlaying) {
            colorPalette.accent.copy(alpha = 0.1f)
        } else if (isPressed) {
            colorPalette.background1.copy(alpha = 0.8f)
        } else {
            colorPalette.background1
        },
        animationSpec = spring(),
        label = "songItemBackground"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isPlaying) {
            colorPalette.accent.copy(alpha = 0.3f)
        } else {
            colorPalette.text.copy(alpha = 0.05f)
        },
        animationSpec = spring(),
        label = "songItemBorder"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isPlaying) 4.dp else 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = if (isPlaying) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thumbnail
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorPalette.background1.copy(alpha = 0.5f)),
                contentScale = ContentScale.Crop
            )
            
            // Song info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BasicText(
                    text = title,
                    style = typography.m.semiBold.copy(
                        color = if (isPlaying) colorPalette.accent else colorPalette.text
                    ),
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
            
            // Duration and trailing content
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (duration != null) {
                    BasicText(
                        text = duration,
                        style = typography.xs.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.6f)
                        )
                    )
                }
                
                trailingContent?.invoke()
            }
        }
    }
}

/**
 * Modern album item card with enhanced visuals
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernAlbumItem(
    title: String,
    artist: String,
    thumbnailUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    year: String? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "albumItemScale"
    )
    
    Box(
        modifier = modifier
            .width(160.dp)
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        colorPalette.background1,
                        colorPalette.background1.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colorPalette.text.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Album artwork
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorPalette.background1.copy(alpha = 0.5f)),
                contentScale = ContentScale.Crop
            )
            
            // Album info
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                BasicText(
                    text = title,
                    style = typography.m.semiBold.copy(color = colorPalette.text),
                    maxLines = 2,
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
                
                if (year != null) {
                    BasicText(
                        text = year,
                        style = typography.xs.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.5f)
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Modern artist item card
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernArtistItem(
    name: String,
    thumbnailUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    subscriberCount: String? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "artistItemScale"
    )
    
    Box(
        modifier = modifier
            .width(140.dp)
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        colorPalette.background1,
                        colorPalette.background1.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colorPalette.text.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Artist image (circular)
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(colorPalette.background1.copy(alpha = 0.5f)),
                contentScale = ContentScale.Crop
            )
            
            // Artist info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                BasicText(
                    text = name,
                    style = typography.m.semiBold.copy(color = colorPalette.text),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (subscriberCount != null) {
                    BasicText(
                        text = subscriberCount,
                        style = typography.xs.medium.copy(
                            color = colorPalette.text.copy(alpha = 0.6f)
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
