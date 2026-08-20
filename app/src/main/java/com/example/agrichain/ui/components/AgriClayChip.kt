package com.example.agrichain.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.agrichain.ui.theme.AgriError
import com.example.agrichain.ui.theme.AgriGreen
import com.example.agrichain.ui.theme.AgriInfo
import com.example.agrichain.ui.theme.AgriSuccess
import com.example.agrichain.ui.theme.AgriWarning
import com.example.agrichain.ui.theme.PolygonPurple

/**
 * Types of status chips used throughout AgriChain.
 */
enum class AgriChipType {
    VERIFIED,
    SUCCESS,
    WARNING,
    ERROR,
    INFO,
    POLYGON
}

/**
 * Reusable status chip for AgriChain.
 *
 * Examples:
 *
 * AgriClayChip(
 *     text = "Verified",
 *     type = AgriChipType.VERIFIED
 * )
 *
 * AgriClayChip(
 *     text = "On-chain",
 *     type = AgriChipType.POLYGON
 * )
 */
@Composable
fun AgriClayChip(
    text: String,
    type: AgriChipType = AgriChipType.INFO,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val accentColor = when (type) {
        AgriChipType.VERIFIED -> AgriSuccess
        AgriChipType.SUCCESS -> AgriGreen
        AgriChipType.WARNING -> AgriWarning
        AgriChipType.ERROR -> AgriError
        AgriChipType.INFO -> AgriInfo
        AgriChipType.POLYGON -> PolygonPurple
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(100.dp),
        color = accentColor.copy(alpha = 0.14f),
        contentColor = accentColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}