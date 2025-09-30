package it.vfsfitvnm.vimusic.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment as ComposeAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.vfsfitvnm.vimusic.Database
import it.vfsfitvnm.vimusic.LocalPlayerAwareWindowInsets
import it.vfsfitvnm.vimusic.LocalPlayerServiceBinder
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.models.Song
import it.vfsfitvnm.vimusic.preferences.DataPreferences
import it.vfsfitvnm.vimusic.query
import it.vfsfitvnm.vimusic.ui.components.LocalMenuState
import it.vfsfitvnm.vimusic.ui.components.ShimmerHost
import it.vfsfitvnm.vimusic.ui.components.themed.FloatingActionsContainerWithScrollToTop
import it.vfsfitvnm.vimusic.ui.components.themed.Header
import it.vfsfitvnm.vimusic.ui.components.themed.NonQueuedMediaItemMenu
import it.vfsfitvnm.vimusic.ui.components.themed.TextPlaceholder
import it.vfsfitvnm.vimusic.ui.components.modern.ModernCard
import it.vfsfitvnm.vimusic.ui.components.modern.ModernSongItem
import it.vfsfitvnm.vimusic.ui.components.modern.ModernAlbumItem
import it.vfsfitvnm.vimusic.ui.components.modern.ModernArtistItem
import it.vfsfitvnm.vimusic.ui.components.modern.ModernSectionHeader
import it.vfsfitvnm.vimusic.ui.components.modern.ModernIconButton
import it.vfsfitvnm.vimusic.ui.items.AlbumItem
import it.vfsfitvnm.vimusic.ui.items.AlbumItemPlaceholder
import it.vfsfitvnm.vimusic.ui.items.ArtistItem
import it.vfsfitvnm.vimusic.ui.items.ArtistItemPlaceholder
import it.vfsfitvnm.vimusic.ui.items.PlaylistItem
import it.vfsfitvnm.vimusic.ui.items.PlaylistItem
import it.vfsfitvnm.vimusic.ui.items.PlaylistItemPlaceholder
import it.vfsfitvnm.vimusic.ui.items.SongItem
import it.vfsfitvnm.vimusic.ui.items.SongItemPlaceholder
import it.vfsfitvnm.vimusic.ui.screens.Route
import it.vfsfitvnm.vimusic.utils.asMediaItem
import it.vfsfitvnm.vimusic.utils.center
import it.vfsfitvnm.vimusic.utils.forcePlay
import it.vfsfitvnm.vimusic.utils.playingSong
import it.vfsfitvnm.vimusic.utils.rememberSnapLayoutInfo
import it.vfsfitvnm.vimusic.utils.secondary
import it.vfsfitvnm.vimusic.utils.semiBold
import it.vfsfitvnm.compose.persist.persist
import it.vfsfitvnm.core.ui.Dimensions
import it.vfsfitvnm.core.ui.LocalAppearance
import it.vfsfitvnm.core.ui.utils.isLandscape
import it.vfsfitvnm.providers.innertube.Innertube
import it.vfsfitvnm.providers.innertube.models.NavigationEndpoint
import it.vfsfitvnm.providers.innertube.models.bodies.NextBody
import it.vfsfitvnm.providers.innertube.requests.relatedPage
import kotlinx.coroutines.flow.distinctUntilChanged
import coil3.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Route
@Composable
fun QuickPicks(
    onAlbumClick: (Innertube.AlbumItem) -> Unit,
    onArtistClick: (Innertube.ArtistItem) -> Unit,
    onPlaylistClick: (Innertube.PlaylistItem) -> Unit,
    onSearchClick: () -> Unit
) {
    val (colorPalette, typography) = LocalAppearance.current
    val binder = LocalPlayerServiceBinder.current
    val menuState = LocalMenuState.current
    val windowInsets = LocalPlayerAwareWindowInsets.current

    var trending by persist<Song?>("home/trending")

    var relatedPageResult by persist<Result<Innertube.RelatedPage?>?>(tag = "home/relatedPageResult")

    LaunchedEffect(relatedPageResult, DataPreferences.shouldCacheQuickPicks) {
        if (DataPreferences.shouldCacheQuickPicks)
            relatedPageResult?.getOrNull()?.let { DataPreferences.cachedQuickPicks = it }
        else DataPreferences.cachedQuickPicks = Innertube.RelatedPage()
    }

    LaunchedEffect(DataPreferences.quickPicksSource) {
        if (
            DataPreferences.shouldCacheQuickPicks && !DataPreferences.cachedQuickPicks.let {
                it.albums.isNullOrEmpty() &&
                    it.artists.isNullOrEmpty() &&
                    it.playlists.isNullOrEmpty() &&
                    it.songs.isNullOrEmpty()
            }
        ) relatedPageResult = Result.success(DataPreferences.cachedQuickPicks)

        suspend fun handleSong(song: Song?) {
            if (relatedPageResult == null || trending?.id != song?.id) relatedPageResult =
                Innertube.relatedPage(
                    body = NextBody(videoId = (song?.id ?: "J7p4bzqLvCw"))
                )
            trending = song
        }

        when (DataPreferences.quickPicksSource) {
            DataPreferences.QuickPicksSource.Trending ->
                Database.instance
                    .trending()
                    .distinctUntilChanged()
                    .collect { handleSong(it.firstOrNull()) }

            DataPreferences.QuickPicksSource.LastInteraction ->
                Database.instance
                    .events()
                    .distinctUntilChanged()
                    .collect { handleSong(it.firstOrNull()?.song) }
        }
    }

    val scrollState = rememberScrollState()
    val quickPicksLazyGridState = rememberLazyGridState()

    val endPaddingValues = windowInsets.only(WindowInsetsSides.End).asPaddingValues()

    val sectionTextModifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 24.dp, bottom = 8.dp)
        .padding(endPaddingValues)

    val (currentMediaId, playing) = playingSong(binder)

    BoxWithConstraints {
        val quickPicksLazyGridItemWidthFactor =
            if (isLandscape && maxWidth * 0.475f >= 320.dp) 0.475f else 0.75f

        val snapLayoutInfoProvider = rememberSnapLayoutInfo(
            lazyGridState = quickPicksLazyGridState,
            positionInLayout = { layoutSize, itemSize ->
                (layoutSize * quickPicksLazyGridItemWidthFactor / 2f - itemSize / 2f)
            }
        )

        val itemInHorizontalGridWidth = maxWidth * quickPicksLazyGridItemWidthFactor

        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorPalette.background0,
                            colorPalette.background0.copy(alpha = 0.95f),
                            colorPalette.background1.copy(alpha = 0.9f)
                        )
                    )
                )
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    windowInsets
                        .only(WindowInsetsSides.Vertical)
                        .asPaddingValues()
                )
        ) {
            // Spotify-style professional header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = ComposeAlignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    BasicText(
                        text = stringResource(R.string.quick_picks),
                        style = typography.xxl.semiBold.copy(
                            color = colorPalette.text
                        )
                    )
                    BasicText(
                        text = "Made for you",
                        style = typography.s.semiBold.copy(
                            color = colorPalette.text.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Professional more options button
                ModernIconButton(
                    iconId = R.drawable.ellipsis_horizontal,
                    size = 40,
                    onClick = { /* More options */ },
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Spotify-style professional content
            relatedPageResult?.getOrNull()?.let { related ->
                // Hero/Featured song section - Spotify style
                trending?.let { song ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        ModernCard(
                            cornerRadius = 12,
                            elevation = 3,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = ComposeAlignment.CenterVertically
                            ) {
                                // Professional album artwork
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colorPalette.background1)
                                ) {
                                    AsyncImage(
                                        model = song.thumbnailUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    
                                    // Spotify-style play overlay
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                if (playing && currentMediaId == song.id) 
                                                    colorPalette.accent.copy(alpha = 0.3f)
                                                else Color.Transparent
                                            ),
                                        contentAlignment = ComposeAlignment.Center
                                    ) {
                                        if (playing && currentMediaId == song.id) {
                                            ModernIconButton(
                                                iconId = R.drawable.pause,
                                                size = 32,
                                                onClick = { /* Pause */ }
                                            )
                                        }
                                    }
                                }
                                
                                // Professional song info
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 16.dp)
                                ) {
                                    BasicText(
                                        text = song.title,
                                        style = typography.m.semiBold.copy(
                                            color = colorPalette.text
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    BasicText(
                                        text = song.artistsText ?: "Unknown Artist",
                                        style = typography.s.semiBold.copy(
                                            color = colorPalette.text.copy(alpha = 0.7f)
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    
                                    // Duration like Spotify
                                    song.durationText?.let { duration ->
                                        BasicText(
                                            text = duration,
                                            style = typography.xs.semiBold.copy(
                                                color = colorPalette.text.copy(alpha = 0.5f)
                                            ),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                                
                                // Professional action buttons
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ModernIconButton(
                                        iconId = R.drawable.heart,
                                        size = 40,
                                        onClick = { /* Like song */ }
                                    )
                                    
                                    ModernIconButton(
                                        iconId = if (playing && currentMediaId == song.id) R.drawable.pause else R.drawable.play,
                                        size = 48,
                                        onClick = {
                                            val mediaItem = song.asMediaItem
                                            binder?.stopRadio()
                                            binder?.player?.forcePlay(mediaItem)
                                            binder?.setupRadio(
                                                NavigationEndpoint.Endpoint.Watch(videoId = mediaItem.mediaId)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Spotify-style recommendations section
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = ComposeAlignment.CenterVertically
                        ) {
                            BasicText(
                                text = "Your Daily Mix",
                                style = typography.l.semiBold.copy(
                                    color = colorPalette.text
                                )
                            )
                            
                            BasicText(
                                text = "Show all",
                                style = typography.s.semiBold.copy(
                                    color = colorPalette.text.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.clickable { /* Show all */ }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Professional song list - Spotify style
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            related.songs?.take(8)?.forEachIndexed { index, song ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (playing && currentMediaId == song.key)
                                                colorPalette.accent.copy(alpha = 0.1f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            val mediaItem = song.asMediaItem
                                            binder?.stopRadio()
                                            binder?.player?.forcePlay(mediaItem)
                                            binder?.setupRadio(
                                                NavigationEndpoint.Endpoint.Watch(videoId = mediaItem.mediaId)
                                            )
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = ComposeAlignment.CenterVertically
                                ) {
                                    // Professional track number
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(colorPalette.background1),
                                        contentAlignment = ComposeAlignment.Center
                                    ) {
                                        if (playing && currentMediaId == song.key) {
                                            ModernIconButton(
                                                iconId = R.drawable.pause,
                                                size = 32,
                                                onClick = { /* Pause */ }
                                            )
                                        } else {
                                            AsyncImage(
                                                model = song.thumbnail?.url,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                    
                                    // Song details - Spotify layout
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 12.dp)
                                    ) {
                                        BasicText(
                                            text = song.info?.name ?: "Unknown Title",
                                            style = typography.s.semiBold.copy(
                                                color = if (playing && currentMediaId == song.key)
                                                    colorPalette.accent
                                                else colorPalette.text
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        BasicText(
                                            text = song.authors?.joinToString("") { it.name.orEmpty() } ?: "Unknown Artist",
                                            style = typography.xs.semiBold.copy(
                                                color = colorPalette.text.copy(alpha = 0.7f)
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                    
                                    // Professional duration and menu
                                    Row(
                                        verticalAlignment = ComposeAlignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        song.durationText?.let { duration ->
                                            BasicText(
                                                text = duration,
                                                style = typography.xs.semiBold.copy(
                                                    color = colorPalette.text.copy(alpha = 0.5f)
                                                )
                                            )
                                        }
                                        
                                        ModernIconButton(
                                            iconId = R.drawable.ellipsis_horizontal,
                                            size = 32,
                                            onClick = {
                                                menuState.display {
                                                    NonQueuedMediaItemMenu(
                                                        onDismiss = menuState::hide,
                                                        mediaItem = song.asMediaItem
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Enhanced Albums Section
                related.albums?.let { albums ->
                    ModernCard(
                        useGradient = true,
                        gradientColors = listOf(
                            colorPalette.background1,
                            colorPalette.background2.copy(alpha = 0.8f)
                        ),
                        elevation = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ModernSectionHeader(
                            title = stringResource(R.string.related_albums),
                            subtitle = "${albums.size} albums to explore",
                            modifier = Modifier.padding(16.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = albums,
                                key = Innertube.AlbumItem::key
                            ) { album ->
                                ModernCard(
                                    elevation = 4,
                                    cornerRadius = 20,
                                    modifier = Modifier.width(160.dp)
                                ) {
                                    ModernAlbumItem(
                                        title = album.info?.name ?: "Unknown Album",
                                        artist = album.authors?.joinToString("") { it.name.orEmpty() } ?: "Unknown Artist",
                                        thumbnailUrl = album.thumbnail?.url,
                                        year = album.year,
                                        onClick = { onAlbumClick(album) },
                                        onLongClick = { /* TODO: Add album menu */ },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }

                related.artists?.let { artists ->
                    ModernSectionHeader(
                        title = stringResource(R.string.similar_artists),
                        modifier = sectionTextModifier
                    )

                    LazyRow(contentPadding = endPaddingValues) {
                        items(
                            items = artists,
                            key = Innertube.ArtistItem::key
                        ) { artist ->
                            ModernCard(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                onClick = { onArtistClick(artist) }
                            ) {
                                ModernArtistItem(
                                    name = artist.info?.name ?: "Unknown Artist",
                                    thumbnailUrl = artist.thumbnail?.url,
                                    subscriberCount = artist.subscribersCountText ?: "",
                                    onClick = { onArtistClick(artist) },
                                    onLongClick = { /* TODO: Add artist menu */ },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                related.playlists?.let { playlists ->
                    ModernSectionHeader(
                        title = stringResource(R.string.recommended_playlists),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp, bottom = 8.dp)
                    )

                    LazyRow(contentPadding = endPaddingValues) {
                        items(
                            items = playlists,
                            key = Innertube.PlaylistItem::key
                        ) { playlist ->
                            ModernCard(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                onClick = { onPlaylistClick(playlist) }
                            ) {
                                // Use original PlaylistItem for now since we don't have ModernPlaylistItem
                                PlaylistItem(
                                    playlist = playlist,
                                    thumbnailSize = Dimensions.thumbnails.playlist,
                                    alternative = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Unit
            } ?: relatedPageResult?.exceptionOrNull()?.let {
                BasicText(
                    text = stringResource(R.string.error_message),
                    style = typography.s.secondary.center,
                    modifier = Modifier
                        .align(ComposeAlignment.CenterHorizontally)
                        .padding(all = 16.dp)
                )
            } ?: ShimmerHost {
                repeat(4) {
                    SongItemPlaceholder(thumbnailSize = Dimensions.thumbnails.song)
                }

                TextPlaceholder(modifier = sectionTextModifier)

                Row {
                    repeat(2) {
                        AlbumItemPlaceholder(
                            thumbnailSize = Dimensions.thumbnails.album,
                            alternative = true
                        )
                    }
                }

                TextPlaceholder(modifier = sectionTextModifier)

                Row {
                    repeat(2) {
                        ArtistItemPlaceholder(
                            thumbnailSize = Dimensions.thumbnails.album,
                            alternative = true
                        )
                    }
                }

                TextPlaceholder(modifier = sectionTextModifier)

                Row {
                    repeat(2) {
                        PlaylistItemPlaceholder(
                            thumbnailSize = Dimensions.thumbnails.album,
                            alternative = true
                        )
                    }
                }
            }
        }

        FloatingActionsContainerWithScrollToTop(
            scrollState = scrollState,
            icon = R.drawable.search,
            onClick = onSearchClick
        )
    }
}
