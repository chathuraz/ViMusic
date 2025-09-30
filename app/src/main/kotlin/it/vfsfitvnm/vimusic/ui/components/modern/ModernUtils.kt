package it.vfsfitvnm.vimusic.ui.components.modern

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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.utils.medium
import it.vfsfitvnm.vimusic.utils.semiBold

/**
 * Modern section header with enhanced styling
 */
@Composable
fun ModernSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
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
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            BasicText(
                text = title,
                style = typography.l.semiBold.copy(color = colorPalette.text)
            )
            
            if (subtitle != null) {
                BasicText(
                    text = subtitle,
                    style = typography.s.medium.copy(
                        color = colorPalette.text.copy(alpha = 0.7f)
                    )
                )
            }
        }
        
        if (actionText != null && onActionClick != null) {
            ModernCompactTextButton(
                text = actionText,
                onClick = onActionClick,
                isPrimary = false
            )
        }
    }
}

/**
 * Modern empty state component
 */
@Composable
fun ModernEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .background(
                        colorPalette.background1.copy(alpha = 0.8f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp)
            ) {
                icon()
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicText(
                text = title,
                style = typography.l.semiBold.copy(
                    color = colorPalette.text,
                    textAlign = TextAlign.Center
                )
            )
            
            BasicText(
                text = description,
                style = typography.m.medium.copy(
                    color = colorPalette.text.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            )
        }
        
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(8.dp))
            
            ModernTextButton(
                text = actionText,
                onClick = onActionClick,
                isPrimary = true
            )
        }
    }
}

/**
 * Modern loading indicator
 */
@Composable
fun ModernLoadingIndicator(
    modifier: Modifier = Modifier,
    text: String? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Animated loading dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                val scale by animateFloatAsState(
                    targetValue = 1f, // You can add pulsing animation here
                    animationSpec = spring(),
                    label = "loadingDot$index"
                )
                
                Box(
                    modifier = Modifier
                        .scale(scale)
                        .width(8.dp)
                        .height(8.dp)
                        .background(
                            colorPalette.accent,
                            RoundedCornerShape(4.dp)
                        )
                )
            }
        }
        
        if (text != null) {
            BasicText(
                text = text,
                style = typography.m.medium.copy(
                    color = colorPalette.text.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

/**
 * Modern error state component
 */
@Composable
fun ModernErrorState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    retryText: String = "Retry",
    onRetryClick: (() -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    ModernCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        cornerRadius = 16,
        elevation = 4
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error icon area
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .background(
                        colorPalette.accent.copy(alpha = 0.1f),
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "!",
                    style = typography.xxl.semiBold.copy(
                        color = colorPalette.accent
                    )
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicText(
                    text = title,
                    style = typography.l.semiBold.copy(
                        color = colorPalette.text,
                        textAlign = TextAlign.Center
                    )
                )
                
                BasicText(
                    text = description,
                    style = typography.m.medium.copy(
                        color = colorPalette.text.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                )
            }
            
            if (onRetryClick != null) {
                ModernTextButton(
                    text = retryText,
                    onClick = onRetryClick,
                    isPrimary = true
                )
            }
        }
    }
}

/**
 * Modern info banner
 */
@Composable
fun ModernInfoBanner(
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        colorPalette.accent.copy(alpha = 0.1f),
                        colorPalette.accent.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colorPalette.accent.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = message,
                style = typography.m.medium.copy(color = colorPalette.text),
                modifier = Modifier.weight(1f)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (actionText != null && onActionClick != null) {
                    ModernCompactTextButton(
                        text = actionText,
                        onClick = onActionClick,
                        isPrimary = true
                    )
                }
                
                if (onDismiss != null) {
                    ModernCompactIconButton(
                        iconId = android.R.drawable.ic_menu_close_clear_cancel,
                        onClick = onDismiss,
                        contentDescription = "Dismiss"
                    )
                }
            }
        }
    }
}
