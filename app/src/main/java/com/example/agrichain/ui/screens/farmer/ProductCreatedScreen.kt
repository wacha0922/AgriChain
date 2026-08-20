package com.example.agrichain.ui.screens.farmer

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrichain.data.model.BlockchainStatus
import com.example.agrichain.data.model.Product
import com.example.agrichain.data.qr.QrCodeGenerator

@Composable
fun ProductCreatedScreen(
    product: Product,
    onGenerateQr: () -> Unit = {},
    onDone: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    var qrBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

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
            // Success indicator
            // -------------------------------------------------

            Surface(
                modifier = Modifier.size(88.dp),
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
                text = "Product Created",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Your agricultural product has been registered in AgriChain.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // -------------------------------------------------
            // Product information
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

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    ProductInfoRow(
                        label = "Product",
                        value = product.productName
                    )

                    ProductInfoRow(
                        label = "Crop Type",
                        value = product.cropType
                    )

                    ProductInfoRow(
                        label = "Location",
                        value = product.farmLocation
                    )

                    ProductInfoRow(
                        label = "Harvest Date",
                        value = product.harvestDate
                    )

                    ProductInfoRow(
                        label = "Quantity",
                        value = product.quantity
                    )

                    ProductInfoRow(
                        label = "Quality",
                        value = product.quality
                    )

                    ProductInfoRow(
                        label = "Cultivation",
                        value = product.cultivationMethod
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    BlockchainStatusPill(
                        status = product.blockchainStatus
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // -------------------------------------------------
            // QR code
            // -------------------------------------------------

            if (qrBitmap != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    color = colors.surface,
                    tonalElevation = 5.dp,
                    shadowElevation = 7.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Product QR Code",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        Image(
                            bitmap = qrBitmap!!.asImageBitmap(),
                            contentDescription = "QR code for ${product.productId}",
                            modifier = Modifier.size(260.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = product.productId,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Scan this QR code to verify the product.",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurface.copy(alpha = 0.60f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            // -------------------------------------------------
            // Generate QR
            // -------------------------------------------------

            Button(
                onClick = {
                    qrBitmap = QrCodeGenerator.generate(
                        content = product.productId,
                        size = 800
                    )

                    onGenerateQr()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = if (qrBitmap == null) {
                        "Generate Product QR"
                    } else {
                        "Regenerate Product QR"
                    },
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
        }
    }
}

@Composable
private fun ProductInfoRow(
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
private fun BlockchainStatusPill(
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
        shape = RoundedCornerShape(16.dp),
        color = colors.primary.copy(alpha = 0.08f)
    ) {
        Text(
            text = "⬡  $statusText",
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 10.dp
            ),
            color = colors.primary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}