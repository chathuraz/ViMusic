package it.vfsfitvnm.vimusic.ui.screens.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.ui.components.modern.ModernIconButton
import it.vfsfitvnm.vimusic.utils.medium
import it.vfsfitvnm.vimusic.utils.secondary
import it.vfsfitvnm.vimusic.utils.semiBold
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.remember

/**
 * Modern home screen scaffold with Material 3 inspired design
 * Features enhanced top app bar, modern tab navigation, and smooth animations
 */
@Composable
fun ModernHomeScaffold(
    tabIndex: Int,
    onTabChange: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    content: @Composable (Int) -> Unit
) {
    val (colorPalette, typography) = LocalAppearance.current
    
    val tabs = listOf(
        TabItem("Quick Picks", R.drawable.sparkles),
        TabItem("Discover", R.drawable.globe),
        TabItem("Songs", R.drawable.musical_notes),
        TabItem("Playlists", R.drawable.playlist),
        TabItem("Artists", R.drawable.person),
        TabItem("Albums", R.drawable.disc),
        TabItem("Local", R.drawable.download)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorPalette.background0)
    ) {
        // YouTube Music Style Top App Bar - Minimal and Clean
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorPalette.background0)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = "ViMusic",
                style = typography.l.semiBold.copy(color = colorPalette.text),
                modifier = Modifier.weight(1f)
            )

            ModernIconButton(
                iconId = R.drawable.equalizer,
                onClick = onSettingsClick,
                contentDescription = "Settings",
                size = 40,
                iconSize = 20
            )
        }

        // YouTube Music Style Tab Bar - Horizontal scrolling tabs
        YouTubeMusicTabBar(
            tabs = tabs,
            selectedTabIndex = tabIndex,
            onTabSelected = onTabChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        // Content
        content(tabIndex)
    }
}

data class TabItem(
    val label: String,
    val iconRes: Int
)

@Composable
fun YouTubeMusicTabBar(
    tabs: List<TabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPalette, _) = LocalAppearance.current
    
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(colorPalette.background0)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tabs.size) { index ->
            YouTubeMusicTab(
                item = tabs[index],
                isSelected = index == selectedTabIndex,
                onClick = { onTabSelected(index) },
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@Composable
fun YouTubeMusicTab(
    item: TabItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            colorPalette.text
        } else {
            colorPalette.textSecondary
        },
        animationSpec = spring(),
        label = "tabTextColor"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.label,
            modifier = Modifier.size(18.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(textColor)
        )
        
        Spacer(modifier = Modifier.width(6.dp))
        
        BasicText(
            text = item.label,
            style = typography.m.semiBold.copy(color = textColor),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    // Bottom indicator for selected tab
    if (isSelected) {
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(colorPalette.accent)
        )
    }
}

@Composable
fun ModernTabBar(
    tabs: List<TabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPalette, _) = LocalAppearance.current
    
    LazyRow(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorPalette.background2),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tabs.size) { index ->
            ModernTab(
                item = tabs[index],
                isSelected = index == selectedTabIndex,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(1f / (tabs.size - index))
            )
        }
    }
}

@Composable
fun ModernTab(
    item: TabItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPalette, typography) = LocalAppearance.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            colorPalette.accent
        } else if (isPressed) {
            colorPalette.background1
        } else {
            Color.Transparent
        },
        animationSpec = spring(),
        label = "tabBackgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            colorPalette.background0
        } else {
            colorPalette.text.copy(alpha = 0.7f)
        },
        animationSpec = spring(),
        label = "tabTextColor"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.label,
            modifier = Modifier.size(18.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(textColor)
        )
        
        Spacer(modifier = Modifier.width(6.dp))
        
        BasicText(
            text = item.label,
            style = typography.m.medium.copy(color = textColor),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
