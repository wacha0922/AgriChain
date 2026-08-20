package com.example.agrichain.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * ============================================================
 * AGRICHAIN DESIGN TOKENS
 * ============================================================
 *
 * Centralized visual constants for the AgriChain application.
 *
 * These values are intentionally kept separate from individual
 * screens so that the visual language remains consistent.
 */

// ------------------------------------------------------------
// SPACING
// ------------------------------------------------------------

object AgriSpacing {

    val ExtraSmall: Dp = 4.dp

    val Small: Dp = 8.dp

    val Medium: Dp = 12.dp

    val Large: Dp = 16.dp

    val ExtraLarge: Dp = 24.dp

    val Huge: Dp = 32.dp

    val Section: Dp = 40.dp
}

// ------------------------------------------------------------
// CORNER RADII
// ------------------------------------------------------------

object AgriCorners {

    val Small = RoundedCornerShape(10.dp)

    val Medium = RoundedCornerShape(16.dp)

    val Large = RoundedCornerShape(24.dp)

    val ExtraLarge = RoundedCornerShape(32.dp)

    val Pill = RoundedCornerShape(100.dp)
}

// ------------------------------------------------------------
// COMPONENT HEIGHTS
// ------------------------------------------------------------

object AgriComponentHeight {

    val Button: Dp = 52.dp

    val TextField: Dp = 56.dp

    val SmallButton: Dp = 44.dp

    val BottomNavigation: Dp = 72.dp
}

// ------------------------------------------------------------
// CARD DIMENSIONS
// ------------------------------------------------------------

object AgriCardDimensions {

    val StandardPadding: Dp = 20.dp

    val CompactPadding: Dp = 16.dp

    val LargePadding: Dp = 24.dp

    val MinimumHeight: Dp = 100.dp
}