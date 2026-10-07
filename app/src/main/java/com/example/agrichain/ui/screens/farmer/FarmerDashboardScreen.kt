package com.example.agrichain.ui.screens.farmer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * UI model used by the Farmer Dashboard.
 *
 * This is deliberately separate from the Room/Firebase Product
 * model. Later, the repository/ViewModel will map real products
 * into this UI model.
 */
data class FarmerProductPreview(
    val productId: String,
    val name: String,
    val cropType: String,
    val quantity: String,
    val status: String
)

@Composable
fun FarmerDashboardScreen(
    farmerName: String = "Farmer",
    farmLocation: String = "Thane, Maharashtra",
    activeLots: Int = 0,
    inTransit: Int = 0,
    verifiedLots: Int = 0,
    recentProducts: List<FarmerProductPreview> = emptyList(),
    onAddProduct: () -> Unit = {},
    onProductSelected: (String) -> Unit = {}
) {

    val colors = MaterialTheme.colorScheme

    val initial =
        farmerName
            .trim()
            .firstOrNull()
            ?.uppercase()
            ?: "F"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.primary.copy(alpha = 0.07f),
                        colors.background,
                        colors.background
                    )
                )
            )
    ) {

        LazyColumn(

            modifier = Modifier.fillMaxSize(),

            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 22.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(18.dp)
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "GOOD MORNING",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp,
                            color =
                                colors.onBackground.copy(
                                    alpha = 0.55f
                                )
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = farmerName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.onBackground
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = colors.primary
                            )

                            Spacer(
                                modifier = Modifier.width(4.dp)
                            )

                            Text(
                                text = farmLocation,
                                style =
                                    MaterialTheme.typography.bodySmall,
                                color =
                                    colors.onBackground.copy(
                                        alpha = 0.62f
                                    )
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.size(54.dp),
                        shape = CircleShape,
                        color = colors.primaryContainer,
                        tonalElevation = 3.dp
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text = initial,
                                style =
                                    MaterialTheme.typography.titleLarge,
                                fontWeight =
                                    FontWeight.ExtraBold,
                                color = colors.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // =====================================================
            // HERO / FARM OVERVIEW
            // =====================================================

            item {

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    color = colors.primary,
                    shadowElevation = 10.dp
                ) {

                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column(
                                modifier =
                                    Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "Farm Command Center",
                                    style =
                                        MaterialTheme.typography.titleLarge,
                                    fontWeight =
                                        FontWeight.ExtraBold,
                                    color = colors.onPrimary
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(5.dp)
                                )

                                Text(
                                    text =
                                        "Keep your produce, movement and verification in one place.",
                                    style =
                                        MaterialTheme.typography.bodyMedium,
                                    color =
                                        colors.onPrimary.copy(
                                            alpha = 0.78f
                                        )
                                )
                            }

                            Surface(
                                modifier =
                                    Modifier.size(46.dp),
                                shape = CircleShape,
                                color =
                                    colors.onPrimary.copy(
                                        alpha = 0.12f
                                    )
                            ) {

                                Box(
                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Inventory2,
                                        contentDescription = null,
                                        tint = colors.onPrimary,
                                        modifier =
                                            Modifier.size(23.dp)
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            DashboardMetric(
                                value = activeLots.toString(),
                                label = "Active Lots",
                                modifier =
                                    Modifier.weight(1f)
                            )

                            DashboardMetric(
                                value = inTransit.toString(),
                                label = "In Transit",
                                modifier =
                                    Modifier.weight(1f)
                            )

                            DashboardMetric(
                                value = verifiedLots.toString(),
                                label = "Verified",
                                modifier =
                                    Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // =====================================================
            // PRIMARY ACTION
            // =====================================================

            item {

                Button(
                    onClick = onAddProduct,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    ),
                    elevation =
                        ButtonDefaults.buttonElevation(
                            defaultElevation = 5.dp,
                            pressedElevation = 1.dp
                        )
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Register New Produce",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // =====================================================
            // TRACEABILITY JOURNEY
            // =====================================================

            item {

                SectionHeader(
                    title = "Traceability Journey",
                    subtitle =
                        "Every product follows a visible digital trail."
                )
            }

            item {

                TraceabilityJourneyCard()
            }

            // =====================================================
            // RECENT PRODUCTS
            // =====================================================

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Recent Products",
                            style =
                                MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.onBackground
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                "Your latest registered produce lots",
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                colors.onBackground.copy(
                                    alpha = 0.58f
                                )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = colors.primaryContainer
                    ) {

                        Text(
                            text = recentProducts.size.toString(),
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            style =
                                MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.onPrimaryContainer
                        )
                    }
                }
            }

            // =====================================================
            // PRODUCT LIST / EMPTY STATE
            // =====================================================

            if (recentProducts.isEmpty()) {

                item {

                    EmptyProductsCard(
                        onAddProduct = onAddProduct
                    )
                }

            } else {

                items(
                    items = recentProducts,
                    key = { it.productId }
                ) { product ->

                    ProductPreviewCard(
                        product = product,
                        onClick = {
                            onProductSelected(
                                product.productId
                            )
                        }
                    )
                }
            }

            // =====================================================
            // TRUST / BLOCKCHAIN AREA
            // =====================================================

            item {

                BlockchainReadinessCard()
            }

            item {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }
    }
}

// =============================================================
// DASHBOARD METRIC
// =============================================================

@Composable
private fun DashboardMetric(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = colors.onPrimary.copy(
            alpha = 0.10f
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 13.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = colors.onPrimary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = label,
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    colors.onPrimary.copy(
                        alpha = 0.70f
                    )
            )
        }
    }
}

// =============================================================
// SECTION HEADER
// =============================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            style =
                MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = colors.onBackground
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            style =
                MaterialTheme.typography.bodySmall,
            color =
                colors.onBackground.copy(
                    alpha = 0.58f
                )
        )
    }
}

// =============================================================
// TRACEABILITY JOURNEY
// =============================================================

@Composable
private fun TraceabilityJourneyCard() {

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = colors.surface,
        tonalElevation = 3.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(
            width = 1.dp,
            color = colors.outline.copy(alpha = 0.10f)
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                JourneyNode(
                    icon = Icons.Default.Inventory2,
                    label = "Farm",
                    active = true
                )

                JourneyConnector(
                    modifier = Modifier.weight(1f),
                    active = false
                )

                JourneyNode(
                    icon = Icons.Default.LocalShipping,
                    label = "Transit",
                    active = false
                )

                JourneyConnector(
                    modifier = Modifier.weight(1f),
                    active = false
                )

                JourneyNode(
                    icon = Icons.Default.CheckCircle,
                    label = "Process",
                    active = false
                )

                JourneyConnector(
                    modifier = Modifier.weight(1f),
                    active = false
                )

                JourneyNode(
                    icon = Icons.Default.Inventory2,
                    label = "Retail",
                    active = false
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colors.primaryContainer.copy(
                    alpha = 0.65f
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text =
                            "Your product journey will be recorded at every handoff.",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color = colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================
// JOURNEY NODE
// =============================================================

@Composable
private fun JourneyNode(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    active: Boolean
) {

    val colors = MaterialTheme.colorScheme

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color =
                if (active) {
                    colors.primary
                } else {
                    colors.surfaceVariant
                }
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint =
                        if (active) {
                            colors.onPrimary
                        } else {
                            colors.onSurfaceVariant
                        },
                    modifier =
                        Modifier.size(20.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color =
                colors.onSurface.copy(
                    alpha =
                        if (active) 0.90f else 0.55f
                )
        )
    }
}

// =============================================================
// JOURNEY CONNECTOR
// =============================================================

@Composable
private fun JourneyConnector(
    modifier: Modifier,
    active: Boolean
) {

    val colors = MaterialTheme.colorScheme

    Canvas(
        modifier = modifier
            .padding(
                horizontal = 6.dp,
                vertical = 20.dp
            )
            .height(2.dp)
    ) {

        drawLine(
            color =
                if (active) {
                    colors.primary
                } else {
                    colors.outline.copy(alpha = 0.30f)
                },

            start = androidx.compose.ui.geometry.Offset(
                0f,
                size.height / 2
            ),

            end = androidx.compose.ui.geometry.Offset(
                size.width,
                size.height / 2
            ),

            strokeWidth = 3f,

            pathEffect =
                if (!active) {
                    PathEffect.dashPathEffect(
                        floatArrayOf(6f, 6f)
                    )
                } else {
                    null
                }
        )
    }
}

// =============================================================
// PRODUCT CARD
// =============================================================

@Composable
private fun ProductPreviewCard(
    product: FarmerProductPreview,
    onClick: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        tonalElevation = 2.dp,
        shadowElevation = 3.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(17.dp),
                color = colors.primaryContainer
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = colors.onPrimaryContainer,
                        modifier =
                            Modifier.size(23.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = product.name,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text =
                        "${product.cropType} • ${product.quantity}",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        colors.onSurface.copy(
                            alpha = 0.58f
                        )
                )
            }

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = colors.primaryContainer
            ) {

                Text(
                    text = product.status,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    ),
                    style =
                        MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimaryContainer
                )
            }

            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Icon(
                imageVector =
                    Icons.Default.ArrowForward,
                contentDescription = null,
                tint = colors.onSurface.copy(
                    alpha = 0.45f
                ),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// =============================================================
// EMPTY PRODUCTS CARD
// =============================================================

@Composable
private fun EmptyProductsCard(
    onAddProduct: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = BorderStroke(
            width = 1.dp,
            color = colors.outline.copy(
                alpha = 0.12f
            )
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = colors.primaryContainer
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = colors.onPrimaryContainer,
                        modifier =
                            Modifier.size(28.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "No products registered yet",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text =
                    "Create your first traceable produce lot to begin your digital supply-chain journey.",
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    colors.onSurface.copy(
                        alpha = 0.60f
                    )
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onAddProduct,
                shape = RoundedCornerShape(16.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(7.dp)
                )

                Text(
                    text = "Register Produce",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// =============================================================
// BLOCKCHAIN READINESS CARD
// =============================================================

@Composable
private fun BlockchainReadinessCard() {

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.primary.copy(
            alpha = 0.07f
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colors.primary.copy(
                alpha = 0.12f
            )
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                color = colors.primary.copy(
                    alpha = 0.12f
                )
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier =
                            Modifier.size(23.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Traceability Foundation",
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text =
                        "Your records are being structured for future blockchain-backed verification.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        colors.onBackground.copy(
                            alpha = 0.62f
                        )
                )
            }
        }
    }
}