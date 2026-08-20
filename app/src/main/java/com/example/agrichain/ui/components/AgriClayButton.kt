package com.example.agrichain.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.agrichain.ui.theme.AgriGreen
import com.example.agrichain.ui.theme.AgriWhite
import com.example.agrichain.ui.theme.AgriComponentHeight
import com.example.agrichain.ui.theme.AgriCorners

/**
 * Primary AgriChain Claymorphism button.
 *
 * Designed for major actions such as:
 * - Sign In
 * - Create Account
 * - Verify
 * - Continue
 * - Add Product
 */
@Composable
fun AgriClayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(AgriComponentHeight.Button),
        enabled = enabled,
        shape = AgriCorners.Large,
        colors = ButtonDefaults.buttonColors(
            containerColor = AgriGreen,
            contentColor = AgriWhite,
            disabledContainerColor = AgriGreen.copy(alpha = 0.4f),
            disabledContentColor = AgriWhite.copy(alpha = 0.7f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(text = text)
    }
}