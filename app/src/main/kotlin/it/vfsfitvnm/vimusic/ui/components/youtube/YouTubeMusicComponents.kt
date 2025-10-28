package it.vfsfitvnm.vimusic.ui.components.youtube

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.utils.semiBold

/**
 * YouTube Music inspired song item with clean, minimal design
 */
@Composable
fun YouTubeMusicSongItem(
    title: String,
    artist: String,
    thumbnailUrl: String?,
    duration: String? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPlaying) {
            colorPalette.accent.copy(alpha = 0.15f)
        } else if (isPressed) {
            colorPalette.background2
        } else {
            Color.Transparent
        },
        animationSpec = spring(),
        label = "songItemBgColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail
        if (!thumbnailUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colorPalette.background2)
            ) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Song info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            BasicText(
                text = title,
                style = typography.m.semiBold.copy(
                    color = if (isPlaying) colorPalette.accent else colorPalette.text
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            BasicText(
                text = artist,
                style = typography.s.copy(
                    color = colorPalette.textSecondary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Trailing content (duration, menu, etc)
        trailingContent?.invoke()
    }
}

/**
 * YouTube Music inspired playlist/album card
 */
@Composable
fun YouTubeMusicPlaylistCard(
    title: String,
    subtitle: String? = null,
    thumbnailUrl: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(),
        label = "cardScale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(colorPalette.background2)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        if (!thumbnailUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorPalette.background1)
            ) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }

        BasicText(
            text = title,
            style = typography.m.semiBold.copy(
                color = colorPalette.text
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            
            BasicText(
                text = subtitle,
                style = typography.s.copy(
                    color = colorPalette.textSecondary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * YouTube Music inspired section header
 */
@Composable
fun YouTubeMusicSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = title,
                style = typography.l.semiBold.copy(
                    color = colorPalette.text
                )
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                
                BasicText(
                    text = subtitle,
                    style = typography.s.copy(
                        color = colorPalette.textSecondary
                    )
                )
            }
        }

        if (actionText != null && onActionClick != null) {
            BasicText(
                text = actionText,
                style = typography.s.semiBold.copy(
                    color = colorPalette.accent
                ),
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}
