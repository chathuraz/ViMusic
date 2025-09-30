package it.vfsfitvnm.vimusic.ui.components.modern

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.utils.medium

/**
 * Modern text button with Material 3 styling and enhanced visual feedback
 */
@Composable
fun ModernTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = false,
    isOutlined: Boolean = false
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "buttonScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colorPalette.background1.copy(alpha = 0.3f)
            isPrimary -> if (isPressed) {
                colorPalette.accent.copy(alpha = 0.8f)
            } else {
                colorPalette.accent
            }
            isOutlined -> if (isPressed) {
                colorPalette.background1.copy(alpha = 0.1f)
            } else {
                colorPalette.background1.copy(alpha = 0.05f)
            }
            else -> if (isPressed) {
                colorPalette.background1.copy(alpha = 0.8f)
            } else {
                colorPalette.background1
            }
        },
        animationSpec = spring(),
        label = "backgroundColor"
    )
    
    val textColor by animateColorAsState(
        targetValue = when {
            !enabled -> colorPalette.text.copy(alpha = 0.5f)
            isPrimary -> colorPalette.onAccent
            else -> colorPalette.text
        },
        animationSpec = spring(),
        label = "textColor"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isPrimary) 6.dp else 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isPrimary) {
                    Brush.linearGradient(
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
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = typography.m.medium.copy(color = textColor)
        )
    }
}

/**
 * Compact version for smaller buttons
 */
@Composable
fun ModernCompactTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = false
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "buttonScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colorPalette.background1.copy(alpha = 0.3f)
            isPrimary -> if (isPressed) {
                colorPalette.accent.copy(alpha = 0.8f)
            } else {
                colorPalette.accent
            }
            else -> if (isPressed) {
                colorPalette.background1.copy(alpha = 0.8f)
            } else {
                colorPalette.background1
            }
        },
        animationSpec = spring(),
        label = "backgroundColor"
    )
    
    val textColor by animateColorAsState(
        targetValue = when {
            !enabled -> colorPalette.text.copy(alpha = 0.5f)
            isPrimary -> colorPalette.onAccent
            else -> colorPalette.text
        },
        animationSpec = spring(),
        label = "textColor"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = typography.s.medium.copy(color = textColor)
        )
    }
}
