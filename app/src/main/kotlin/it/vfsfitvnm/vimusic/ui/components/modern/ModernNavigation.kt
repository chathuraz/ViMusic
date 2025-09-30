package it.vfsfitvnm.vimusic.ui.components.modern

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.utils.medium
import it.vfsfitvnm.vimusic.utils.semiBold

/**
 * Modern navigation rail with enhanced Material 3 styling
 */
@Composable
fun ModernNavigationRail(
    selectedIndex: Int,
    onTabSelect: (Int) -> Unit,
    tabs: List<NavigationTab>,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    Box(
        modifier = modifier
            .width(80.dp)
            .fillMaxHeight()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                ambientColor = colorPalette.text.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorPalette.background1,
                        colorPalette.background1.copy(alpha = 0.95f),
                        colorPalette.background1.copy(alpha = 0.9f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colorPalette.text.copy(alpha = 0.05f),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header section (logo/brand)
            if (header != null) {
                header()
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Navigation tabs
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    ModernNavigationRailItem(
                        tab = tab,
                        isSelected = index == selectedIndex,
                        onClick = { onTabSelect(index) }
                    )
                }
            }
        }
    }
}

/**
 * Individual navigation rail item
 */
@Composable
private fun ModernNavigationRailItem(
    tab: NavigationTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "navItemScale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> colorPalette.accent.copy(alpha = 0.15f)
            isPressed -> colorPalette.background1.copy(alpha = 0.8f)
            else -> colorPalette.background1.copy(alpha = 0.0f)
        },
        animationSpec = spring(),
        label = "navItemBackground"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) {
            colorPalette.accent
        } else {
            colorPalette.text.copy(alpha = 0.7f)
        },
        animationSpec = spring(),
        label = "navItemIconColor"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            colorPalette.accent.copy(alpha = 0.3f)
        } else {
            colorPalette.text.copy(alpha = 0.0f)
        },
        animationSpec = spring(),
        label = "navItemBorder"
    )
    
    Column(
        modifier = Modifier
            .width(64.dp)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(tab.iconId),
            contentDescription = tab.label,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(24.dp)
        )
        
        if (isSelected && tab.showLabelWhenSelected) {
            BasicText(
                text = tab.label,
                style = typography.xs.medium.copy(
                    color = iconColor,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )
        }
    }
}

/**
 * Data class for navigation tabs
 */
data class NavigationTab(
    @DrawableRes val iconId: Int,
    val label: String,
    val showLabelWhenSelected: Boolean = false
)

/**
 * Modern bottom navigation bar with enhanced styling
 */
@Composable
fun ModernBottomNavigation(
    selectedIndex: Int,
    onTabSelect: (Int) -> Unit,
    tabs: List<NavigationTab>,
    modifier: Modifier = Modifier
) {
    val (colorPalette, _) = LocalAppearance.current
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(
                elevation = 8.dp,
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                ModernBottomNavigationItem(
                    tab = tab,
                    isSelected = index == selectedIndex,
                    onClick = { onTabSelect(index) }
                )
            }
        }
    }
}

/**
 * Individual bottom navigation item
 */
@Composable
private fun ModernBottomNavigationItem(
    tab: NavigationTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "bottomNavItemScale"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) {
            colorPalette.accent
        } else {
            colorPalette.text.copy(alpha = 0.6f)
        },
        animationSpec = spring(),
        label = "bottomNavIconColor"
    )
    
    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(tab.iconId),
            contentDescription = tab.label,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(24.dp)
        )
        
        if (isSelected) {
            BasicText(
                text = tab.label,
                style = typography.xs.medium.copy(
                    color = iconColor,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )
        }
    }
}
