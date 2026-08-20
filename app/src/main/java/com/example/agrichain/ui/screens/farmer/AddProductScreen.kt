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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddProductScreen(
    onBack: () -> Unit = {},
    onProductCreated: (
        String,
        String,
        String,
        String,
        String,
        String,
        String
    ) -> Unit = { _, _, _, _, _, _, _ -> }
) {
    var productName by remember {
        mutableStateOf("")
    }

    var cropType by remember {
        mutableStateOf("")
    }

    var farmLocation by remember {
        mutableStateOf("")
    }

    var harvestDate by remember {
        mutableStateOf("")
    }

    var quantity by remember {
        mutableStateOf("")
    }

    var quality by remember {
        mutableStateOf("")
    }

    var cultivationMethod by remember {
        mutableStateOf("")
    }

    var cropTypeExpanded by remember {
        mutableStateOf(false)
    }

    var qualityExpanded by remember {
        mutableStateOf(false)
    }

    var cultivationExpanded by remember {
        mutableStateOf(false)
    }

    val colors = MaterialTheme.colorScheme

    val cropTypes = listOf(
        "Rice",
        "Wheat",
        "Tomato",
        "Potato",
        "Onion",
        "Mango",
        "Spinach",
        "Other"
    )

    val qualityOptions = listOf(
        "Premium",
        "Grade A",
        "Grade B",
        "Standard"
    )

    val cultivationOptions = listOf(
        "Organic",
        "Conventional",
        "Natural Farming",
        "Other"
    )

    val isFormValid =
        productName.isNotBlank() &&
                cropType.isNotBlank() &&
                farmLocation.isNotBlank() &&
                harvestDate.isNotBlank() &&
                quantity.isNotBlank() &&
                quality.isNotBlank() &&
                cultivationMethod.isNotBlank()

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
                    vertical = 20.dp
                )
        ) {

            // -------------------------------------------------
            // Back
            // -------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBack
                ) {
                    Text(
                        text = "← Back",
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // -------------------------------------------------
            // Header
            // -------------------------------------------------

            Text(
                text = "Add Agricultural Product",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Enter accurate product information for traceability.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.65f)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // -------------------------------------------------
            // Product Name
            // -------------------------------------------------

            OutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Product Name")
                },
                placeholder = {
                    Text("e.g. Organic Tomatoes")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Product"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = outlinedColors()
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // -------------------------------------------------
            // Crop Type
            // -------------------------------------------------

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = cropType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Crop Type")
                    },
                    placeholder = {
                        Text("Select crop")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = outlinedColors()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                ) {
                    TextButton(
                        onClick = {
                            cropTypeExpanded = true
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("")
                    }
                }

                DropdownMenu(
                    expanded = cropTypeExpanded,
                    onDismissRequest = {
                        cropTypeExpanded = false
                    }
                ) {
                    cropTypes.forEach { crop ->
                        DropdownMenuItem(
                            text = {
                                Text(crop)
                            },
                            onClick = {
                                cropType = crop
                                cropTypeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // -------------------------------------------------
            // Farm Location
            // -------------------------------------------------

            OutlinedTextField(
                value = farmLocation,
                onValueChange = {
                    farmLocation = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Farm Location")
                },
                placeholder = {
                    Text("e.g. Nashik, Maharashtra")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Farm location"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = outlinedColors()
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // -------------------------------------------------
            // Harvest Date
            // -------------------------------------------------

            OutlinedTextField(
                value = harvestDate,
                onValueChange = {
                    harvestDate = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Harvest Date")
                },
                placeholder = {
                    Text("DD/MM/YYYY")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Harvest date"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(20.dp),
                colors = outlinedColors()
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // -------------------------------------------------
            // Quantity
            // -------------------------------------------------

            OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Quantity")
                },
                placeholder = {
                    Text("e.g. 250 kg")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ProductionQuantityLimits,
                        contentDescription = "Quantity"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                shape = RoundedCornerShape(20.dp),
                colors = outlinedColors()
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // -------------------------------------------------
            // Quality Grade
            // -------------------------------------------------

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = quality,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Quality Grade")
                    },
                    placeholder = {
                        Text("Select quality")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = outlinedColors()
                )

                Box(
                    modifier = Modifier.matchParentSize()
                ) {
                    TextButton(
                        onClick = {
                            qualityExpanded = true
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("")
                    }
                }

                DropdownMenu(
                    expanded = qualityExpanded,
                    onDismissRequest = {
                        qualityExpanded = false
                    }
                ) {
                    qualityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {
                                quality = option
                                qualityExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // -------------------------------------------------
            // Cultivation Method
            // -------------------------------------------------

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = cultivationMethod,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Cultivation Method")
                    },
                    placeholder = {
                        Text("Select method")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = outlinedColors()
                )

                Box(
                    modifier = Modifier.matchParentSize()
                ) {
                    TextButton(
                        onClick = {
                            cultivationExpanded = true
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("")
                    }
                }

                DropdownMenu(
                    expanded = cultivationExpanded,
                    onDismissRequest = {
                        cultivationExpanded = false
                    }
                ) {
                    cultivationOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {
                                cultivationMethod = option
                                cultivationExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // -------------------------------------------------
            // Create Product
            // -------------------------------------------------

            Button(
                onClick = {
                    onProductCreated(
                        productName.trim(),
                        cropType,
                        farmLocation.trim(),
                        harvestDate.trim(),
                        quantity.trim(),
                        quality,
                        cultivationMethod
                    )
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.primary.copy(
                        alpha = 0.35f
                    ),
                    disabledContentColor = colors.onPrimary.copy(
                        alpha = 0.65f
                    )
                )
            ) {
                Text(
                    text = "Create Product",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Your product information will later be connected to the AgriChain traceability and Polygon verification system.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onBackground.copy(alpha = 0.55f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Composable
private fun outlinedColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
            alpha = 0.5f
        ),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )