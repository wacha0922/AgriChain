package com.example.agrichain.ui.screens.farmer

import android.graphics.Bitmap
import android.graphics.Color
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.EnumMap

@Composable
fun ProductQrScreen(
    productId: String,
    productName: String = "Agricultural Product",
    onBack: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    var qrBitmap by remember(productId) {
        mutableStateOf<Bitmap?>(null)
    }

    var generationError by remember(productId) {
        mutableStateOf<String?>(null)
    }

    var copied by remember {
        mutableStateOf(false)
    }

    /*
     * Generate the QR code from the Product ID.
     *
     * Only the Product ID is encoded.
     * Product details remain in Firebase/Firestore and can
     * later be verified against blockchain data.
     */
    LaunchedEffect(productId) {

        if (productId.isBlank()) {
            generationError = "Invalid product ID."
            qrBitmap = null
            return@LaunchedEffect
        }

        try {

            val hints =
                EnumMap<EncodeHintType, Any>(
                    EncodeHintType::class.java
                ).apply {
                    this[EncodeHintType.MARGIN] = 1
                    this[EncodeHintType.CHARACTER_SET] = "UTF-8"
                }

            val matrix =
                QRCodeWriter().encode(
                    productId,
                    BarcodeFormat.QR_CODE,
                    900,
                    900,
                    hints
                )

            val bitmap =
                Bitmap.createBitmap(
                    matrix.width,
                    matrix.height,
                    Bitmap.Config.ARGB_8888
                )

            for (x in 0 until matrix.width) {
                for (y in 0 until matrix.height) {

                    bitmap.setPixel(
                        x,
                        y,
                        if (matrix[x, y]) {
                            Color.BLACK
                        } else {
                            Color.WHITE
                        }
                    )
                }
            }

            qrBitmap = bitmap
            generationError = null

        } catch (exception: Exception) {

            qrBitmap = null

            generationError =
                exception.message
                    ?: "Unable to generate the QR code."
        }
    }

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
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBack
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Product QR",
                        style =
                            MaterialTheme.typography.headlineSmall,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color = colors.onBackground
                    )

                    Text(
                        text =
                            "Digital identity for traceability",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color =
                            colors.onBackground.copy(
                                alpha = 0.58f
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =====================================================
            // PRODUCT SUMMARY
            // =====================================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.primaryContainer.copy(
                        alpha = 0.45f
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
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(15.dp),
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
                                    Icons.Default.QrCode2,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier =
                                    Modifier.size(26.dp)
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
                            text = productName,
                            style =
                                MaterialTheme.typography.titleMedium,
                            fontWeight =
                                FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Traceability ID",
                            style =
                                MaterialTheme.typography.labelSmall,
                            color =
                                colors.onSurface.copy(
                                    alpha = 0.55f
                                )
                        )

                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )

                        Text(
                            text = productId,
                            style =
                                MaterialTheme.typography.bodyMedium,
                            fontWeight =
                                FontWeight.SemiBold,
                            color = colors.primary
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =====================================================
            // QR CARD
            // =====================================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Scan to Trace",
                        style =
                            MaterialTheme.typography.titleLarge,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color = colors.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "This QR identifies the registered product.",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color =
                            colors.onSurface.copy(
                                alpha = 0.58f
                            ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    if (qrBitmap != null) {

                        Surface(
                            modifier = Modifier.size(300.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = androidx.compose.ui.graphics.Color.White
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Image(
                                    bitmap =
                                        qrBitmap!!.asImageBitmap(),
                                    contentDescription =
                                        "Product QR code",
                                    modifier =
                                        Modifier.fillMaxSize()
                                )
                            }
                        }

                    } else {

                        Surface(
                            modifier = Modifier.size(300.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = colors.surfaceVariant
                        ) {

                            Box(
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Column(
                                    horizontalAlignment =
                                        Alignment.CenterHorizontally
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.QrCode2,
                                        contentDescription =
                                            null,
                                        modifier =
                                            Modifier.size(56.dp),
                                        tint =
                                            colors.onSurfaceVariant
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(10.dp)
                                    )

                                    Text(
                                        text =
                                            generationError
                                                ?: "Generating QR...",
                                        style =
                                            MaterialTheme.typography.bodyMedium,
                                        color =
                                            colors.onSurfaceVariant,
                                        textAlign =
                                            TextAlign.Center,
                                        modifier =
                                            Modifier.padding(
                                                horizontal = 24.dp
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Surface(
                        shape =
                            RoundedCornerShape(50.dp),
                        color =
                            colors.primary.copy(
                                alpha = 0.09f
                            )
                    ) {

                        Row(
                            modifier =
                                Modifier.padding(
                                    horizontal = 13.dp,
                                    vertical = 7.dp
                                ),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Verified,
                                contentDescription =
                                    null,
                                tint = colors.primary,
                                modifier =
                                    Modifier.size(17.dp)
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(7.dp)
                            )

                            Text(
                                text =
                                    "AgriChain Traceability Identity",
                                style =
                                    MaterialTheme.typography.labelMedium,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    colors.primary
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =====================================================
            // PRODUCT ID CARD
            // =====================================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "Product Identifier",
                        style =
                            MaterialTheme.typography.titleMedium,
                        fontWeight =
                            FontWeight.Bold,
                        color = colors.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = productId,
                        style =
                            MaterialTheme.typography.bodyMedium,
                        fontWeight =
                            FontWeight.SemiBold,
                        color = colors.primary
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Only this identifier is encoded in the QR. Product information stays in the AgriChain data layer.",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color =
                            colors.onSurface.copy(
                                alpha = 0.55f
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =====================================================
            // STATUS
            // =====================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Surface(
                    modifier =
                        Modifier.weight(1f),
                    shape =
                        RoundedCornerShape(16.dp),
                    color =
                        colors.primary.copy(
                            alpha = 0.08f
                        )
                ) {

                    Row(
                        modifier =
                            Modifier.padding(13.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.CheckCircle,
                            contentDescription =
                                null,
                            tint =
                                colors.primary,
                            modifier =
                                Modifier.size(20.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Ready to scan",
                            style =
                                MaterialTheme.typography.labelMedium,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                colors.primary
                        )
                    }
                }

                Surface(
                    modifier =
                        Modifier.weight(1f),
                    shape =
                        RoundedCornerShape(16.dp),
                    color =
                        colors.secondary.copy(
                            alpha = 0.08f
                        )
                ) {

                    Row(
                        modifier =
                            Modifier.padding(13.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Verified,
                            contentDescription =
                                null,
                            tint =
                                colors.secondary,
                            modifier =
                                Modifier.size(20.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Traceable",
                            style =
                                MaterialTheme.typography.labelMedium,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                colors.secondary
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =====================================================
            // ACTIONS
            // =====================================================

            Button(
                onClick = {
                    copied = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                enabled = productId.isNotBlank()
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ContentCopy,
                    contentDescription =
                        null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text =
                        if (copied) {
                            "Product ID Ready"
                        } else {
                            "Use Product ID"
                        },
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Back to Product Details",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text =
                    "QR verification will later connect this identity to blockchain-backed product integrity.",
                modifier =
                    Modifier.fillMaxWidth(),
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    colors.onBackground.copy(
                        alpha = 0.42f
                    ),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}