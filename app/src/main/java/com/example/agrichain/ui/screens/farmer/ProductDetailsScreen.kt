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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrichain.data.model.Product

@Composable
fun ProductDetailsScreen(
    product: Product,
    onBack: () -> Unit = {},
    onGenerateQr: () -> Unit = {},
    onVerifyProduct: () -> Unit = {}
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
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp)
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text(
                    text = "← Back",
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = product.productName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = product.cropType,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            DetailCard(
                title = "Product Overview"
            ) {

                DetailRow(
                    label = "Product ID",
                    value = product.productId
                )

                DetailRow(
                    label = "Crop Type",
                    value = product.cropType
                )

                DetailRow(
                    label = "Farm Location",
                    value = product.farmLocation
                )

                DetailRow(
                    label = "Harvest Date",
                    value = product.harvestDate
                )

                DetailRow(
                    label = "Quantity",
                    value = product.quantity
                )

                DetailRow(
                    label = "Quality",
                    value = product.quality
                )

                DetailRow(
                    label = "Cultivation",
                    value = product.cultivationMethod
                )

                DetailRow(
                    label = "Status",
                    value = product.status.name
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            DetailCard(
                title = "Traceability Journey"
            ) {

                TraceabilityStep(
                    number = "1",
                    title = "Product Created",
                    active = true
                )

                TraceabilityStep(
                    number = "2",
                    title = "Harvested",
                    active = product.status.name != "CREATED"
                )

                TraceabilityStep(
                    number = "3",
                    title = "In Transit",
                    active =
                        product.status.name == "IN_TRANSIT" ||
                                product.status.name == "PROCESSED" ||
                                product.status.name == "AT_RETAILER" ||
                                product.status.name == "SOLD"
                )

                TraceabilityStep(
                    number = "4",
                    title = "Processed",
                    active =
                        product.status.name == "PROCESSED" ||
                                product.status.name == "AT_RETAILER" ||
                                product.status.name == "SOLD"
                )

                TraceabilityStep(
                    number = "5",
                    title = "Retailer",
                    active =
                        product.status.name == "AT_RETAILER" ||
                                product.status.name == "SOLD"
                )

                TraceabilityStep(
                    number = "6",
                    title = "Sold",
                    active = product.status.name == "SOLD"
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            DetailCard(
                title = "Blockchain Status"
            ) {

                DetailRow(
                    label = "Status",
                    value = product.blockchainStatus.name
                )

                DetailRow(
                    label = "Product ID",
                    value = product.productId
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = onGenerateQr,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Generate Product QR",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onVerifyProduct,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Open Verification View",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Composable
private fun DetailCard(
    title: String,
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        content()
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            modifier = Modifier.weight(0.42f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.60f
            )
        )

        Text(
            text = value,
            modifier = Modifier.weight(0.58f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TraceabilityStep(
    number: String,
    title: String,
    active: Boolean
) {

    Text(
        text = "$number. $title",
        modifier = Modifier.padding(
            vertical = 5.dp
        ),
        fontSize = 14.sp,
        fontWeight = if (active) {
            FontWeight.Bold
        } else {
            FontWeight.Normal
        },
        color = if (active) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.45f
            )
        }
    )
}