package com.example.agrichain.ui.screens.consumer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions

@Composable
fun QrScannerScreen(
    onBack: () -> Unit = {},
    onProductFound: (String) -> Unit = {}
) {

    val context = LocalContext.current

    val colors = MaterialTheme.colorScheme

    var isScanning by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    fun startScanner() {

        val activity = context.findActivity()

        if (activity == null) {
            errorMessage =
                "Unable to open the QR scanner on this device."
            return
        }

        errorMessage = null
        isScanning = true

        val options =
            GmsBarcodeScannerOptions.Builder()
                .enableAutoZoom()
                .build()

        val scanner =
            GmsBarcodeScanning.getClient(
                activity,
                options
            )

        scanner.startScan()
            .addOnSuccessListener { barcode ->

                isScanning = false

                val scannedValue =
                    barcode.rawValue
                        ?.trim()

                if (scannedValue.isNullOrBlank()) {

                    errorMessage =
                        "The QR code did not contain a valid Product ID."

                } else {

                    onProductFound(
                        scannedValue
                    )
                }
            }
            .addOnCanceledListener {

                isScanning = false
            }
            .addOnFailureListener {

                isScanning = false

                errorMessage =
                    "Unable to scan the QR code. Please try again."
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                colors.background
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            TextButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "← Back",
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Icon(
                imageVector =
                    Icons.Default.QrCodeScanner,
                contentDescription =
                    "QR Scanner",
                tint = colors.primary,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Verify Agricultural Product",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text =
                    "Scan the Product QR code to retrieve its traceability information.",
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    colors.onBackground.copy(
                        alpha = 0.65f
                    ),
                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Button(
                onClick = {
                    if (!isScanning) {
                        startScanner()
                    }
                },
                enabled = !isScanning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            colors.primary
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.QrCodeScanner,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.height(0.dp)
                )

                Text(
                    text =
                        if (isScanning) {
                            "Opening Scanner..."
                        } else {
                            "Scan Product QR"
                        },
                    modifier = Modifier
                        .padding(
                            start = 8.dp
                        ),
                    fontWeight =
                        FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            if (!errorMessage.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                OutlinedButton(
                    onClick = {
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Warning,
                        contentDescription = null
                    )

                    Text(
                        text =
                            errorMessage.orEmpty(),
                        modifier = Modifier
                            .padding(
                                start = 8.dp
                            ),
                        textAlign =
                            TextAlign.Center
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Only the Product ID is encoded in the QR code.",
                style =
                    MaterialTheme.typography.labelMedium,
                color =
                    colors.onBackground.copy(
                        alpha = 0.50f
                    ),
                textAlign =
                    TextAlign.Center
            )
        }
    }
}

private fun Context.findActivity(): Activity? {

    var currentContext = this

    while (currentContext is ContextWrapper) {

        if (currentContext is Activity) {
            return currentContext
        }

        currentContext =
            currentContext.baseContext
    }

    return null
}