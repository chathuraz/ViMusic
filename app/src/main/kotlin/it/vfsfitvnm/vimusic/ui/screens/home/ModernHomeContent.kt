package it.vfsfitvnm.vimusic.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.ui.components.modern.ModernCard
import it.vfsfitvnm.vimusic.ui.components.modern.ModernEmptyState
import it.vfsfitvnm.vimusic.ui.components.modern.ModernIconButton
import it.vfsfitvnm.vimusic.ui.components.modern.ModernSectionHeader

/**
 * Modern enhanced home screen with improved visual design and user experience
 * This is a simplified version that can be integrated gradually
 */
@Composable
fun ModernHomeContent(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome header with search
        item {
            ModernCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                useGradient = true,
                elevation = 6
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ModernSectionHeader(
                        title = "Welcome to ViMusic",
                        subtitle = "Discover and enjoy your favorite music"
                    )
                    
                    ModernIconButton(
                        iconId = R.drawable.search,
                        onClick = onSearchClick,
                        size = 56,
                        iconSize = 28,
                        useGradient = true,
                        contentDescription = "Search"
                    )
                }
            }
        }
        
        // Quick Access Section
        item {
            ModernSectionHeader(
                title = "Quick Access",
                subtitle = "Jump back into your music",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        
        // Discover New Music Section
        item {
            ModernCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = 4
            ) {
                ModernSectionHeader(
                    title = "Discover New Music",
                    subtitle = "Explore trending songs and artists"
                )
            }
        }
        
        // Empty state for demonstration
        item {
            ModernEmptyState(
                title = "Start Your Musical Journey",
                description = "Search for your favorite songs and artists to get started",
                actionText = "Search Music",
                onActionClick = onSearchClick,
                icon = {
                    ModernIconButton(
                        iconId = R.drawable.musical_notes,
                        onClick = { },
                        size = 48,
                        iconSize = 24,
                        enabled = false
                    )
                }
            )
        }
    }
}