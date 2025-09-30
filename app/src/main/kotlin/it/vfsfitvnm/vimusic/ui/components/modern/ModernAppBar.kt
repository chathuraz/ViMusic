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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.utils.medium

/**
 * Modern Material 3 inspired top app bar with gradient support and enhanced styling
 */
@Composable
fun ModernTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    useGradient: Boolean = true,
    elevation: Int = 4
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    val backgroundColor = if (useGradient) {
        Brush.linearGradient(
            listOf(
                colorPalette.background1,
                colorPalette.background1.copy(alpha = 0.95f),
                colorPalette.background1.copy(alpha = 0.9f)
            )
        )
    } else {
        Brush.linearGradient(listOf(colorPalette.background1, colorPalette.background1))
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = colorPalette.text.copy(alpha = 0.05f),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                navigationIcon?.invoke()
                
                BasicText(
                    text = title,
                    style = typography.l.medium.copy(color = colorPalette.text),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(start = if (navigationIcon != null) 12.dp else 0.dp)
                )
            }
            
            if (actions != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        }
    }
}

/**
 * Modern floating action button with enhanced styling
 */
@Composable
fun ModernFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val (colorPalette, _) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "fabScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) {
            if (isPressed) {
                colorPalette.accent.copy(alpha = 0.8f)
            } else {
                colorPalette.accent
            }
        } else {
            colorPalette.background1.copy(alpha = 0.5f)
        },
        animationSpec = spring(),
        label = "fabBackgroundColor"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 4.dp else 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = colorPalette.accent.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.radialGradient(
                    listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.9f)
                    )
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Modern bottom navigation bar with enhanced styling
 */
@Composable
fun ModernBottomNavigationBar(
    modifier: Modifier = Modifier,
    elevation: Int = 8,
    content: @Composable RowScope.() -> Unit
) {
    val (colorPalette, _) = LocalAppearance.current
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
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
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}
