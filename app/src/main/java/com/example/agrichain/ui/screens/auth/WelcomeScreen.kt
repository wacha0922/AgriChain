package com.example.agrichain.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private data class UserRole(
    val title: String,
    val icon: String,
    val description: String
)

private val agriChainRoles = listOf(
    UserRole(
        title = "Farmer",
        icon = "🌾",
        description = "Manage your crops"
    ),
    UserRole(
        title = "Transporter",
        icon = "🚚",
        description = "Transport produce"
    ),
    UserRole(
        title = "Processor",
        icon = "🏭",
        description = "Process produce"
    ),
    UserRole(
        title = "Retailer",
        icon = "🏪",
        description = "Sell produce"
    ),
    UserRole(
        title = "Consumer",
        icon = "👤",
        description = "Trace products"
    ),
    UserRole(
        title = "Government",
        icon = "🏛️",
        description = "Monitor supply"
    )
)

@Composable
fun WelcomeScreen(
    onRoleSelected: (String) -> Unit
) {
    var contentVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(150)
        contentVisible = true
    }

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { it / 12 }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // AgriChain logo area
                Surface(
                    modifier = Modifier.size(92.dp),
                    shape = RoundedCornerShape(30.dp),
                    color = colors.surface,
                    tonalElevation = 6.dp,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            color = colors.primary.copy(alpha = 0.10f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🌱",
                                    fontSize = 34.sp
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onBackground.copy(alpha = 0.70f)
                )

                Text(
                    text = "AgriChain",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Choose your role to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onBackground.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                // Role selection grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        bottom = 12.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = agriChainRoles,
                        key = { it.title }
                    ) { role ->

                        RoleCard(
                            role = role,
                            onClick = {
                                onRoleSelected(role.title)
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // Polygon branding
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = colors.primary.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "⬡  Powered by Polygon Blockchain",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 9.dp
                        ),
                        color = colors.primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleCard(
    role: UserRole,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(158.dp)
            .clip(RoundedCornerShape(26.dp))
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(26.dp),
        color = colors.surface,
        tonalElevation = 4.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Surface(
                modifier = Modifier.size(62.dp),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.10f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = role.icon,
                        fontSize = 30.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = role.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = role.description,
                style = MaterialTheme.typography.labelSmall,
                color = colors.onSurface.copy(alpha = 0.55f),
                textAlign = TextAlign.Center
            )
        }
    }
}