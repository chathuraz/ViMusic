package it.vfsfitvnm.vimusic.ui.theme

import androidx.compose.ui.graphics.Color
import it.vfsfitvnm.core.ui.ColorPalette

/**
 * YouTube Music inspired color palette and theme
 * Features clean, modern design with subtle animations and Material 3 principles
 */

// YouTube Music Dark Theme Colors
val YouTubeMusicDarkBackground0 = Color(0xFF0F0F0F)  // Pure black background
val YouTubeMusicDarkBackground1 = Color(0xFF181818)  // Slightly lighter background
val YouTubeMusicDarkBackground2 = Color(0xFF282828)  // Card/Surface background
val YouTubeMusicDarkText = Color(0xFFFFFFFF)         // White text
val YouTubeMusicDarkTextSecondary = Color(0xFFB3B3B3) // Light gray secondary text
val YouTubeMusicDarkTextDisabled = Color(0xFF727272)  // Darker gray for disabled
val YouTubeMusicDarkAccent = Color(0xFF1DB954)       // YouTube Music Green

// YouTube Music Light Theme Colors
val YouTubeMusicLightBackground0 = Color(0xFFFAFAFA)  // Off-white background
val YouTubeMusicLightBackground1 = Color(0xFFFFFFFF)  // White background
val YouTubeMusicLightBackground2 = Color(0xFFF0F0F0)  // Card background
val YouTubeMusicLightText = Color(0xFF030303)         // Almost black text
val YouTubeMusicLightTextSecondary = Color(0xFF606060) // Gray text
val YouTubeMusicLightTextDisabled = Color(0xFFCCCCCC)  // Light gray disabled
val YouTubeMusicLightAccent = Color(0xFF1DB954)       // YouTube Music Green

/**
 * Dark palette matching YouTube Music aesthetic
 */
val youtubeMusicDarkPalette = ColorPalette(
    background0 = YouTubeMusicDarkBackground0,
    background1 = YouTubeMusicDarkBackground1,
    background2 = YouTubeMusicDarkBackground2,
    text = YouTubeMusicDarkText,
    textSecondary = YouTubeMusicDarkTextSecondary,
    textDisabled = YouTubeMusicDarkTextDisabled,
    accent = YouTubeMusicDarkAccent,
    onAccent = YouTubeMusicDarkBackground0,
    red = Color(0xFFFF4444),
    blue = Color(0xFF1E88E5),
    yellow = Color(0xFFFBC02D),
    isDefault = false,
    isDark = true
)

/**
 * Light palette matching YouTube Music aesthetic
 */
val youtubeMusicLightPalette = ColorPalette(
    background0 = YouTubeMusicLightBackground0,
    background1 = YouTubeMusicLightBackground1,
    background2 = YouTubeMusicLightBackground2,
    text = YouTubeMusicLightText,
    textSecondary = YouTubeMusicLightTextSecondary,
    textDisabled = YouTubeMusicLightTextDisabled,
    accent = YouTubeMusicLightAccent,
    onAccent = YouTubeMusicLightBackground1,
    red = Color(0xFFD32F2F),
    blue = Color(0xFF1E88E5),
    yellow = Color(0xFFFBC02D),
    isDefault = false,
    isDark = false
)
