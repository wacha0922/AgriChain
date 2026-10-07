package com.example.agrichain.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/*
 * ============================================================
 * AGRICHAIN THEME
 * ============================================================
 *
 * Centralized application theme.
 *
 * Color.kt remains the source of truth for the AgriChain
 * brand palette.
 *
 * This file additionally establishes a consistent shape
 * language so the application feels like one cohesive product.
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
// AGRICHAIN SHAPE SYSTEM
// ------------------------------------------------------------

private val AgriChainShapes = Shapes(

    small = RoundedCornerShape(14.dp),

    medium = RoundedCornerShape(20.dp),

    large = RoundedCornerShape(28.dp)
)

// ------------------------------------------------------------
// AGRICHAIN THEME
// ------------------------------------------------------------

@Composable
fun AgriChainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(

        colorScheme = colorScheme,

        typography = Typography,

        shapes = AgriChainShapes,

        content = content
    )
}