package com.example.agrichain.ui.screens.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class FarmerProductPreview(
    val name: String,
    val cropType: String,
    val quantity: String,
    val status: String
)

private val sampleProducts = listOf(
    FarmerProductPreview(
        name = "Organic Tomatoes",
        cropType = "Tomato",
        quantity = "250 kg",
        status = "Harvested"
    ),
    FarmerProductPreview(
        name = "Fresh Spinach",
        cropType = "Spinach",
        quantity = "120 kg",
        status = "Ready"
    ),
    FarmerProductPreview(
        name = "Alphonso Mangoes",
        cropType = "Mango",
        quantity = "500 kg",
        status = "In preparation"
    )
)

@Composable
fun FarmerDashboardScreen(
    farmerName: String = "Farmer",
    onAddProduct: () -> Unit = {},
    onProductSelected: (String) -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                DashboardHeader(
                    farmerName = farmerName
                )
            }

            item {
                DashboardSummaryCard()
            }

            item {
                DashboardSectionTitle(
                    title = "Quick Actions"
                )
            }

            item {
                Button(
                    onClick = onAddProduct,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Text(
                        text = "+  Add Agricultural Product",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            item {
                DashboardSectionTitle(
                    title = "Recent Products"
                )
            }

            items(
                items = sampleProducts,
                key = { it.name }
            ) { product ->

                ProductPreviewCard(
                    product = product,
                    onClick = {
                        onProductSelected(product.name)
                    }
                )
            }

            item {
                BlockchainStatusCard()
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    farmerName: String
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Good day,",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onBackground.copy(alpha = 0.65f)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = farmerName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Manage your farm and track your products.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground.copy(alpha = 0.65f)
        )
    }
}

@Composable
private fun DashboardSummaryCard() {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = colors.surface,
        tonalElevation = 5.dp,
        shadowElevation = 7.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryItem(
                value = "3",
                label = "Products"
            )

            SummaryItem(
                value = "870 kg",
                label = "Total Harvest"
            )

            SummaryItem(
                value = "100%",
                label = "Verified"
            )
        }
    }
}

@Composable
private fun SummaryItem(
    value: String,
    label: String
) {
    val colors = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.onSurface.copy(alpha = 0.60f)
        )
    }
}

@Composable
private fun DashboardSectionTitle(
    title: String
) {
    val colors = MaterialTheme.colorScheme

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = colors.onBackground
    )
}

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
        tonalElevation = 3.dp,
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.10f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🌱",
                        fontSize = 25.sp
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "${product.cropType} • ${product.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurface.copy(alpha = 0.60f)
                )
            }

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = colors.primary.copy(alpha = 0.10f)
            ) {
                Text(
                    text = product.status,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary
                )
            }
        }
    }
}

@Composable
private fun BlockchainStatusCard() {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = colors.primary.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⬡",
                        color = colors.primary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column {
                Text(
                    text = "Blockchain Ready",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Polygon integration will verify your product records.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onBackground.copy(alpha = 0.65f)
                )
            }
        }
    }
}