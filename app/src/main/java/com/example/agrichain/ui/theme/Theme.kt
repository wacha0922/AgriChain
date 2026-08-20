package com.example.agrichain.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/*
 * ============================================================
 * AGRICHAIN THEME
 * ============================================================
 *
 * Uses the centralized AgriChain color system from Color.kt.
 *
 * Dynamic Android colors are intentionally disabled so that
 * AgriChain keeps the same visual identity on different devices.
 */

// ------------------------------------------------------------
// DARK COLOR SCHEME
// ------------------------------------------------------------

private val DarkColorScheme = darkColorScheme(
    primary = AgriGreenDark,
    onPrimary = AgriDarkBackground,

    primaryContainer = AgriDarkClay,
    onPrimaryContainer = AgriLeafDark,

    secondary = AgriSageDark,
    onSecondary = AgriDarkBackground,

    secondaryContainer = AgriDarkSurface,
    onSecondaryContainer = AgriSageDark,

    tertiary = AgriBeigeDark,
    onTertiary = AgriDarkBackground,

    background = AgriDarkBackground,
    onBackground = AgriTextLight,

    surface = AgriDarkSurface,
    onSurface = AgriTextLight,

    surfaceVariant = AgriDarkClay,
    onSurfaceVariant = AgriTextSecondaryDark,

    error = AgriError,
    onError = AgriWhite
)

// ------------------------------------------------------------
// LIGHT COLOR SCHEME
// ------------------------------------------------------------

private val LightColorScheme = lightColorScheme(
    primary = AgriGreen,
    onPrimary = AgriWhite,

    primaryContainer = AgriSageLight,
    onPrimaryContainer = AgriForest,

    secondary = AgriForest,
    onSecondary = AgriWhite,

    secondaryContainer = AgriSageLight,
    onSecondaryContainer = AgriForest,

    tertiary = AgriEarth,
    onTertiary = AgriWhite,

    background = AgriCream,
    onBackground = AgriTextDark,

    surface = AgriClayLight,
    onSurface = AgriTextDark,

    surfaceVariant = AgriBeige,
    onSurfaceVariant = AgriTextSecondary,

    error = AgriError,
    onError = AgriWhite
)

// ------------------------------------------------------------
// AGRICHAIN THEME
// ------------------------------------------------------------

@Composable
fun AgriChainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}