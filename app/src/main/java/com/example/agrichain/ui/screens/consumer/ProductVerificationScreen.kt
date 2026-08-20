package com.example.agrichain.ui.screens.consumer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrichain.data.model.BlockchainStatus
import com.example.agrichain.data.model.Product

@Composable
fun ProductVerificationScreen(
    product: Product,
    onScanAnother: () -> Unit = {},
    onDone: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -------------------------------------------------
            // Verification status
            // -------------------------------------------------

            Surface(
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = colors.primary,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Product Verified",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "This product was found in the AgriChain traceability records.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // -------------------------------------------------
            // Product ID
            // -------------------------------------------------

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colors.primary.copy(alpha = 0.08f)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "PRODUCT ID",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = product.productId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // -------------------------------------------------
            // Product details
            // -------------------------------------------------

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = colors.surface,
                tonalElevation = 5.dp,
                shadowElevation = 7.dp
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Traceability Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    ProductDetailRow(
                        label = "Product",
                        value = product.productName
                    )

                    ProductDetailRow(
                        label = "Crop Type",
                        value = product.cropType
                    )

                    ProductDetailRow(
                        label = "Farm Location",
                        value = product.farmLocation
                    )

                    ProductDetailRow(
                        label = "Harvest Date",
                        value = product.harvestDate
                    )

                    ProductDetailRow(
                        label = "Quantity",
                        value = product.quantity
                    )

                    ProductDetailRow(
                        label = "Quality",
                        value = product.quality
                    )

                    ProductDetailRow(
                        label = "Cultivation",
                        value = product.cultivationMethod
                    )

                    ProductDetailRow(
                        label = "Product Status",
                        value = product.status.name
                            .replace("_", " ")
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // -------------------------------------------------
            // Blockchain status
            // -------------------------------------------------

            BlockchainVerificationCard(
                status = product.blockchainStatus
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // -------------------------------------------------
            // Scan another
            // -------------------------------------------------

            Button(
                onClick = onScanAnother,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text(
                    text = "Scan Another Product",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "Done",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
private fun ProductDetailRow(
    label: String,
    value: String
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurface.copy(alpha = 0.60f)
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.onSurface,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun BlockchainVerificationCard(
    status: BlockchainStatus
) {
    val colors = MaterialTheme.colorScheme

    val statusText = when (status) {
        BlockchainStatus.PENDING ->
            "Polygon verification pending"

        BlockchainStatus.SUBMITTED ->
            "Polygon transaction submitted"

        BlockchainStatus.CONFIRMED ->
            "Polygon transaction confirmed"

        BlockchainStatus.FAILED ->
            "Polygon verification failed"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.primary.copy(alpha = 0.08f)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Blockchain Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "⬡  $statusText",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.primary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Blockchain verification will be connected to Polygon in the next phase.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onBackground.copy(alpha = 0.60f)
            )
        }
    }
}