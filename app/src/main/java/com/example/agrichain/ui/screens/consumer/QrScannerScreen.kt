package com.example.agrichain.ui.screens.consumer

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

@Composable
fun QrScannerScreen(
    onBack: () -> Unit = {},
    onProductFound: (String) -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // -------------------------------------------------
            // Scanner icon
            // -------------------------------------------------

            Surface(
                modifier = Modifier.size(110.dp),
                shape = MaterialTheme.shapes.large,
                color = colors.primary.copy(alpha = 0.10f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▦",
                        fontSize = 54.sp,
                        color = colors.primary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // -------------------------------------------------
            // Title
            // -------------------------------------------------

            Text(
                text = "Scan Product QR",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Scan an AgriChain product QR code to view its traceability information.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // -------------------------------------------------
            // Open scanner button
            // -------------------------------------------------

            Button(
                onClick = {
                    val activity = context as? Activity
                        ?: return@Button

                    scope.launch {
                        try {
                            val scanner =
                                GmsBarcodeScanning.getClient(activity)

                            scanner.startScan()
                                .addOnSuccessListener { barcode ->

                                    val scannedValue =
                                        barcode.rawValue

                                    if (!scannedValue.isNullOrBlank()) {
                                        onProductFound(scannedValue)
                                    }
                                }
                                .addOnCanceledListener {
                                    // User cancelled the scanner.
                                }
                                .addOnFailureListener {
                                    // Scanner failure will be handled later.
                                }

                        } catch (_: Exception) {
                            // Scanner initialization failure will be handled later.
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text(
                    text = "Open QR Scanner",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // -------------------------------------------------
            // Back button
            // -------------------------------------------------

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Back"
                )
            }
        }
    }
}