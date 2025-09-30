package it.vfsfitvnm.vimusic.ui.components.modern

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance

/**
 * Modern circular button with enhanced visual feedback and Material 3 styling
 */
@Composable
fun ModernIconButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Int = 48,
    iconSize: Int = 24,
    useGradient: Boolean = false,
    contentDescription: String? = null
) {
    val (colorPalette, _) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "buttonScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            colorPalette.background1.copy(alpha = 0.8f)
        } else {
            colorPalette.background1
        },
        animationSpec = spring(),
        label = "backgroundColor"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (enabled) {
            colorPalette.text
        } else {
            colorPalette.text.copy(alpha = 0.5f)
        },
        animationSpec = spring(),
        label = "iconColor"
    )
    
    Box(
        modifier = modifier
            .size(size.dp)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 2.dp else 4.dp,
                shape = CircleShape,
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(CircleShape)
            .background(
                if (useGradient) {
                    Brush.radialGradient(
                        listOf(
                            backgroundColor,
                            backgroundColor.copy(alpha = 0.9f)
                        )
                    )
                } else {
                    Brush.linearGradient(listOf(backgroundColor, backgroundColor))
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    radius = (size / 2).dp,
                    color = colorPalette.text.copy(alpha = 0.2f)
                ),
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

/**
 * Compact version of ModernIconButton for smaller UI elements
 */
@Composable
fun ModernCompactIconButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    ModernIconButton(
        iconId = iconId,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        size = 40,
        iconSize = 20,
        contentDescription = contentDescription
    )
}

/**
 * Emphasized icon button with gradient background for primary actions
 */
@Composable
fun ModernPrimaryIconButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    ModernIconButton(
        iconId = iconId,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        size = 56,
        iconSize = 28,
        useGradient = true,
        contentDescription = contentDescription
    )
}
