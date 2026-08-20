package com.example.agrichain.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.agrichain.ui.theme.AgriClayLight
import com.example.agrichain.ui.theme.AgriDarkClay
import com.example.agrichain.ui.theme.AgriSpacing
import com.example.agrichain.ui.theme.AgriCorners
import androidx.compose.foundation.isSystemInDarkTheme

/**
 * Reusable Claymorphism-style card for AgriChain.
 *
 * The card automatically adapts to light and dark mode.
 */
@Composable
fun AgriClayCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()

    Card(
        modifier = modifier,
        shape = AgriCorners.Large,
        colors = CardDefaults.cardColors(
            containerColor = if (darkTheme) {
                AgriDarkClay
            } else {
                AgriClayLight
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(AgriSpacing.Large)
        ) {
            content()
        }
    }
}