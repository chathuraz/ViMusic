package it.vfsfitvnm.vimusic.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.ui.components.modern.ModernCard
import it.vfsfitvnm.vimusic.ui.components.modern.ModernEmptyState
import it.vfsfitvnm.vimusic.ui.components.modern.ModernIconButton
import it.vfsfitvnm.vimusic.ui.components.modern.ModernSectionHeader
import it.vfsfitvnm.vimusic.ui.components.modern.ModernTextButton
import it.vfsfitvnm.vimusic.ui.components.modern.ModernTopAppBar

/**
 * Example integration of modern UI components in the existing QuickPicks screen
 * This demonstrates how to gradually upgrade existing screens with modern components
 */
@Composable
fun ModernQuickPicks(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Modern top app bar
        ModernTopAppBar(
            title = "Quick Picks",
            actions = {
                ModernIconButton(
                    iconId = R.drawable.search,
                    onClick = onSearchClick,
                    contentDescription = "Search"
                )
            },
            useGradient = true
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome card with quick actions
            item {
                ModernCard(
                    useGradient = true,
                    elevation = 6
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ModernSectionHeader(
                            title = "Welcome Back!",
                            subtitle = "Your music, ready to play"
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ModernTextButton(
                                text = "Shuffle All",
                                onClick = { /* Shuffle all songs */ },
                                isPrimary = true,
                                modifier = Modifier.weight(1f)
                            )
                            
                            ModernTextButton(
                                text = "Browse",
                                onClick = onSearchClick,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            // Trending section placeholder
            item {
                ModernSectionHeader(
                    title = "Trending Now",
                    subtitle = "Popular songs everyone's listening to"
                )
            }
            
            // Empty state for demonstration
            item {
                ModernEmptyState(
                    title = "No Music Yet",
                    description = "Start exploring to see your quick picks here",
                    actionText = "Discover Music",
                    onActionClick = onSearchClick,
                    icon = {
                        ModernIconButton(
                            iconId = R.drawable.musical_notes,
                            onClick = { },
                            size = 64,
                            iconSize = 32,
                            enabled = false,
                            useGradient = true
                        )
                    }
                )
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * Modern variant of the HomeSongs screen
 */
@Composable
fun ModernHomeSongs(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ModernTopAppBar(
            title = "Your Library",
            actions = {
                ModernIconButton(
                    iconId = R.drawable.search,
                    onClick = onSearchClick,
                    contentDescription = "Search"
                )
                
                ModernIconButton(
                    iconId = R.drawable.shuffle,
                    onClick = { /* Shuffle all songs */ },
                    contentDescription = "Shuffle all"
                )
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Quick stats card
            item {
                ModernCard(
                    elevation = 4
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ModernSectionHeader(
                                title = "0",
                                subtitle = "Songs"
                            )
                        }
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ModernSectionHeader(
                                title = "0",
                                subtitle = "Favorites"
                            )
                        }
                    }
                }
            }
            
            // Empty state
            item {
                ModernEmptyState(
                    title = "No Songs Found",
                    description = "Add music to your device or search online",
                    actionText = "Search Music",
                    onActionClick = onSearchClick
                )
            }
        }
    }
}