package it.vfsfitvnm.vimusic.ui.components.modern

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance

/**
 * Modern Material 3 inspired card component with elevated design,
 * subtle animations, and gradient support
 */
@Composable
fun ModernCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    elevation: Int = 4,
    cornerRadius: Int = 16,
    useGradient: Boolean = false,
    gradientColors: List<Color> = emptyList(),
    content: @Composable ColumnScope.() -> Unit
) {
    val (colorPalette, _) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "cardScale"
    )
    
    val backgroundColor = if (useGradient && gradientColors.isNotEmpty()) {
        Brush.linearGradient(gradientColors)
    } else {
        Brush.linearGradient(
            listOf(
                colorPalette.background1,
                colorPalette.background1.copy(alpha = 0.95f)
            )
        )
    }
    
    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(cornerRadius.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f),
                spotColor = colorPalette.text.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = colorPalette.text.copy(alpha = 0.05f),
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        onClick = onClick
                    )
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}

/**
 * Compact version of ModernCard for smaller UI elements
 */
@Composable
fun ModernCompactCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        elevation = 2,
        cornerRadius = 12,
        content = content
    )
}

/**
 * Emphasized card with gradient background for important content
 */
@Composable
fun ModernEmphasizedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val (colorPalette, _) = LocalAppearance.current
    
    ModernCard(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        elevation = 8,
        cornerRadius = 20,
        useGradient = true,
        gradientColors = listOf(
            colorPalette.background1,
            colorPalette.background1.copy(alpha = 0.9f),
            colorPalette.background1.copy(alpha = 0.95f)
        ),
        content = content
    )
}
