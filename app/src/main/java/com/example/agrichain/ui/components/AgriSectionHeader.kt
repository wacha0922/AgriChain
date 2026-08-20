package com.example.agrichain.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrichain.ui.theme.AgriForest

/**
 * Reusable section heading for AgriChain screens.
 *
 * Examples:
 * - ACTIVE CROPS
 * - RECENT LEDGER ENTRIES
 * - PRODUCT JOURNEY
 * - BLOCKCHAIN DETAILS
 */
@Composable
fun AgriSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = AgriForest,
            letterSpacing = 1.2.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )
    }
}