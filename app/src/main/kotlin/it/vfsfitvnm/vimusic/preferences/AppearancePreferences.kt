package it.vfsfitvnm.vimusic.preferences

import it.vfsfitvnm.vimusic.GlobalPreferencesHolder
import it.vfsfitvnm.vimusic.preferences.OldPreferences.ColorPaletteMode
import it.vfsfitvnm.vimusic.preferences.OldPreferences.ColorPaletteName
import it.vfsfitvnm.core.ui.BuiltInFontFamily
import it.vfsfitvnm.core.ui.ColorMode
import it.vfsfitvnm.core.ui.ColorSource
import it.vfsfitvnm.core.ui.Darkness
import it.vfsfitvnm.core.ui.ThumbnailRoundness

object AppearancePreferences : GlobalPreferencesHolder() {
    var colorSource by enum(
        when (OldPreferences.oldColorPaletteName) {
            ColorPaletteName.Default, ColorPaletteName.PureBlack -> ColorSource.Default
            ColorPaletteName.Dynamic, ColorPaletteName.AMOLED -> ColorSource.Dynamic
            ColorPaletteName.MaterialYou -> ColorSource.MaterialYou
        }
    )
    var colorMode by enum(
        when (OldPreferences.oldColorPaletteMode) {
            ColorPaletteMode.Light -> ColorMode.Light
            ColorPaletteMode.Dark -> ColorMode.Dark
            ColorPaletteMode.System -> ColorMode.System
        }
    )
    var darkness by enum(
        when (OldPreferences.oldColorPaletteName) {
            ColorPaletteName.Default, ColorPaletteName.Dynamic, ColorPaletteName.MaterialYou -> Darkness.Normal
            ColorPaletteName.PureBlack -> Darkness.PureBlack
            ColorPaletteName.AMOLED -> Darkness.AMOLED
        }
    )
    var thumbnailRoundness by enum(ThumbnailRoundness.Medium)
    var fontFamily by enum(BuiltInFontFamily.Poppins)
    var applyFontPadding by boolean(false)
    val isShowingThumbnailInLockscreenProperty = boolean(true)
    var isShowingThumbnailInLockscreen by isShowingThumbnailInLockscreenProperty
    var swipeToHideSong by boolean(false)
    var swipeToHideSongConfirm by boolean(true)
    var maxThumbnailSize by int(1920)
    var hideExplicit by boolean(false)
    var autoPip by boolean(false)
    var openPlayer by boolean(true)
}
