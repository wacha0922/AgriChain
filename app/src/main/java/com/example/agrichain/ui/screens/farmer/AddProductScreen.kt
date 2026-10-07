package com.example.agrichain.ui.screens.farmer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.location.Address
import android.location.Geocoder

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.Spa

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.content.ContextCompat
import androidx.core.net.toUri

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.suspendCancellableCoroutine

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume


@OptIn(ExperimentalMaterial3Api::class)
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

    // =========================================================
    // CONTEXT
    // =========================================================

    val context =
        androidx.compose.ui.platform.LocalContext.current

    // =========================================================
    // FORM STATE
    // =========================================================

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

    var selectedDateMillis by remember {
        mutableStateOf<Long?>(null)
    }

    var quantity by remember {
        mutableStateOf("")
    }

    var quantityUnit by remember {
        mutableStateOf("kg")
    }

    var quality by remember {
        mutableStateOf("")
    }

    var cultivationMethod by remember {
        mutableStateOf("")
    }

    // =========================================================
    // DROPDOWN STATE
    // =========================================================

    var cropExpanded by remember {
        mutableStateOf(false)
    }

    var unitExpanded by remember {
        mutableStateOf(false)
    }

    var qualityExpanded by remember {
        mutableStateOf(false)
    }

    var cultivationExpanded by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // DATE STATE
    // =========================================================

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var dateError by remember {
        mutableStateOf<String?>(null)
    }

    // =========================================================
    // LOCATION STATE
    // =========================================================

    var locationLoading by remember {
        mutableStateOf(false)
    }

    var locationError by remember {
        mutableStateOf<String?>(null)
    }

    var locationRequestId by remember {
        mutableIntStateOf(0)
    }

    var showLocationPermissionDialog by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // GOOGLE LOCATION CLIENTS
    // =========================================================

    val fusedLocationClient =
        remember {
            LocationServices.getFusedLocationProviderClient(
                context
            )
        }

    val settingsClient =
        remember {
            LocationServices.getSettingsClient(
                context
            )
        }

    // =========================================================
    // LOCATION SETTINGS RESOLUTION
    // =========================================================

    val locationSettingsLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->

            if (
                result.resultCode ==
                Activity.RESULT_OK
            ) {

                locationError = null
                locationRequestId++

            } else {

                locationLoading = false

                locationError =
                    "Please turn on Location Services to detect the farm location."
            }
        }

    // =========================================================
    // LOCATION PERMISSION REQUEST
    // =========================================================

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineGranted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true

            val coarseGranted =
                permissions[
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ] == true

            if (
                fineGranted ||
                coarseGranted
            ) {

                locationError = null
                locationRequestId++

            } else {

                locationLoading = false

                val activity =
                    context.findActivity()

                val shouldOpenSettings =
                    activity != null &&
                            !activity.shouldShowRequestPermissionRationale(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) &&
                            !activity.shouldShowRequestPermissionRationale(
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )

                if (shouldOpenSettings) {

                    showLocationPermissionDialog = true

                } else {

                    locationError =
                        "Location permission was denied."
                }
            }
        }

    // =========================================================
    // REQUEST LOCATION
    // =========================================================

    fun requestCurrentLocation() {

        locationError = null

        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (
            fineGranted ||
            coarseGranted
        ) {

            locationRequestId++

        } else {

            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // =========================================================
    // LOCATION WORKFLOW
    // =========================================================

    LaunchedEffect(locationRequestId) {

        if (locationRequestId == 0) {
            return@LaunchedEffect
        }

        locationLoading = true
        locationError = null

        try {

            // -----------------------------------------------------
            // Determine permission precision.
            //
            // If precise permission exists, request high accuracy.
            // If only approximate permission exists, use balanced
            // accuracy so the feature still works.
            // -----------------------------------------------------

            val fineGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            val locationPriority =
                if (fineGranted) {
                    Priority.PRIORITY_HIGH_ACCURACY
                } else {
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY
                }

            // -----------------------------------------------------
            // Location settings request
            // -----------------------------------------------------

            val locationRequest =
                LocationRequest.Builder(
                    locationPriority,
                    10_000L
                )
                    .setWaitForAccurateLocation(
                        fineGranted
                    )
                    .build()

            val settingsRequest =
                LocationSettingsRequest.Builder()
                    .addLocationRequest(
                        locationRequest
                    )
                    .build()

            try {

                settingsClient
                    .checkLocationSettings(
                        settingsRequest
                    )
                    .await()

            } catch (
                exception: ApiException
            ) {

                if (
                    exception.statusCode ==
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED &&
                    exception is ResolvableApiException
                ) {

                    locationLoading = false

                    locationSettingsLauncher.launch(
                        IntentSenderRequest.Builder(
                            exception.resolution
                        ).build()
                    )

                    return@LaunchedEffect
                }

                throw exception
            }

            // -----------------------------------------------------
            // Get current device location
            // -----------------------------------------------------

            val currentLocationRequest =
                CurrentLocationRequest.Builder()
                    .setPriority(
                        locationPriority
                    )
                    .setMaxUpdateAgeMillis(
                        5_000L
                    )
                    .setDurationMillis(
                        15_000L
                    )
                    .build()

            val cancellationTokenSource =
                CancellationTokenSource()

            val location =
                fusedLocationClient
                    .getCurrentLocation(
                        currentLocationRequest,
                        cancellationTokenSource.token
                    )
                    .await()

            if (location == null) {

                locationError =
                    "Unable to detect the current device location. Please try again."

            } else {

                // -------------------------------------------------
                // Convert coordinates into readable address
                // -------------------------------------------------

                val readableLocation =
                    getReadableLocation(
                        context = context,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )

                farmLocation =
                    readableLocation
                        ?: formatCoordinateLocation(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
            }

        } catch (
            exception: Exception
        ) {

            locationError =
                when {
                    exception.message
                        ?.contains(
                            "permission",
                            ignoreCase = true
                        ) == true -> {
                        "Location permission is required."
                    }

                    else -> {
                        exception.message
                            ?: "Unable to obtain the current device location."
                    }
                }

        } finally {

            locationLoading = false
        }
    }

    // =========================================================
    // COLORS
    // =========================================================

    val colors =
        MaterialTheme.colorScheme

    // =========================================================
    // OPTIONS
    // =========================================================

    val cropCategories =
        listOf(
            "Vegetables",
            "Fruits",
            "Grains",
            "Pulses",
            "Spices",
            "Millets",
            "Oilseeds",
            "Other"
        )

    val productSuggestions =
        when (cropType) {

            "Vegetables" -> listOf(
                "Organic Tomatoes",
                "Fresh Spinach",
                "Potatoes",
                "Onions"
            )

            "Fruits" -> listOf(
                "Alphonso Mangoes",
                "Bananas",
                "Pomegranates",
                "Papaya"
            )

            "Grains" -> listOf(
                "Basmati Rice",
                "Wheat",
                "Maize",
                "Sorghum"
            )

            "Pulses" -> listOf(
                "Toor Dal",
                "Moong Dal",
                "Chickpeas",
                "Urad Dal"
            )

            "Spices" -> listOf(
                "Turmeric",
                "Black Pepper",
                "Cumin",
                "Chilli"
            )

            "Millets" -> listOf(
                "Pearl Millet",
                "Finger Millet",
                "Foxtail Millet",
                "Little Millet"
            )

            "Oilseeds" -> listOf(
                "Groundnut",
                "Soybean",
                "Sunflower",
                "Sesame"
            )

            else -> listOf(
                "Organic Tomatoes",
                "Basmati Rice",
                "Alphonso Mangoes"
            )
        }

    val quantityUnits =
        listOf(
            "kg",
            "quintal",
            "tonnes",
            "litres",
            "units"
        )

    val qualityOptions =
        listOf(
            "Premium",
            "Grade A",
            "Grade B",
            "Standard"
        )

    val cultivationOptions =
        listOf(
            "Organic",
            "Conventional",
            "Natural Farming",
            "Integrated Farming"
        )

    // =========================================================
    // VALIDATION
    // =========================================================

    val quantityValue =
        quantity
            .trim()
            .toDoubleOrNull()

    val isFormValid =
        productName.trim().length >= 2 &&
                cropType.isNotBlank() &&
                farmLocation.trim().length >= 2 &&
                harvestDate.isNotBlank() &&
                quantityValue != null &&
                quantityValue > 0 &&
                quality.isNotBlank() &&
                cultivationMethod.isNotBlank()

    // =========================================================
    // MAIN SCREEN
    // =========================================================

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
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
        ) {

            // =====================================================
            // TOP BAR
            // =====================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                TextButton(
                    onClick = onBack
                ) {

                    Text(
                        text = "← Back",
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            colors.primary
                    )
                }

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Surface(
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        colors.primaryContainer
                ) {

                    Text(
                        text = "NEW LOT",
                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                        style =
                            MaterialTheme.typography.labelSmall,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color =
                            colors.onPrimaryContainer,
                        letterSpacing =
                            1.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            // =====================================================
            // HEADER
            // =====================================================

            Text(
                text = "Register Produce",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    colors.onBackground
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    "Create a digital record for your produce so its journey can be traced through the supply chain.",
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    colors.onBackground.copy(
                        alpha = 0.62f
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // =====================================================
            // PROGRESS STRIP
            // =====================================================

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                color =
                    colors.primaryContainer.copy(
                        alpha = 0.55f
                    )
            ) {

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    ProgressStep(
                        number = "1",
                        title = "Produce",
                        active = true
                    )

                    ProgressLine(
                        modifier =
                            Modifier.weight(1f)
                    )

                    ProgressStep(
                        number = "2",
                        title = "Farm",
                        active =
                            farmLocation.isNotBlank()
                    )

                    ProgressLine(
                        modifier =
                            Modifier.weight(1f)
                    )

                    ProgressStep(
                        number = "3",
                        title = "Details",
                        active =
                            quantity.isNotBlank() &&
                                    quality.isNotBlank()
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // =====================================================
            // SECTION 1 — PRODUCT
            // =====================================================

            FormSection(
                number = "01",
                title = "Product"
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                },
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Product Name")
                },
                placeholder = {
                    Text("e.g. Organic Tomatoes")
                },
                leadingIcon = {
                    Icon(
                        imageVector =
                            Icons.Default.Spa,
                        contentDescription =
                            null
                    )
                },
                trailingIcon = {

                    if (productName.isNotBlank()) {

                        IconButton(
                            onClick = {
                                productName = ""
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Close,
                                contentDescription =
                                    "Clear product name"
                            )
                        }
                    }
                },
                singleLine = true,
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    agriChainOutlinedColors()
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = "Suggested produce",
                style =
                    MaterialTheme.typography.labelMedium,
                fontWeight =
                    FontWeight.Bold,
                color =
                    colors.onBackground.copy(
                        alpha = 0.65f
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                productSuggestions
                    .take(3)
                    .forEach { suggestion ->

                        FilterChip(
                            selected =
                                productName == suggestion,

                            onClick = {
                                productName = suggestion
                            },

                            label = {
                                Text(suggestion)
                            }
                        )
                    }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            SelectionField(
                value = cropType,
                label = "Crop Category",
                placeholder = "Select a category",
                expanded = cropExpanded,
                onExpandedChange = {
                    cropExpanded = it
                },
                options = cropCategories,
                onOptionSelected = {
                    cropType = it
                    productName = ""
                }
            )

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =====================================================
            // SECTION 2 — FARM & HARVEST
            // =====================================================

            FormSection(
                number = "02",
                title = "Farm & Harvest"
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // =====================================================
            // FARM LOCATION
            // =====================================================

            OutlinedTextField(
                value = farmLocation,
                onValueChange = {
                    farmLocation = it
                    locationError = null
                },
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Farm Location")
                },
                placeholder = {
                    Text("e.g. Thane, Maharashtra")
                },
                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.LocationOn,
                        contentDescription =
                            null
                    )
                },
                trailingIcon = {

                    IconButton(
                        onClick = {
                            requestCurrentLocation()
                        },
                        enabled =
                            !locationLoading
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.MyLocation,
                            contentDescription =
                                "Use current device location",
                            tint =
                                if (locationLoading) {
                                    colors.onSurface.copy(
                                        alpha = 0.40f
                                    )
                                } else {
                                    colors.primary
                                }
                        )
                    }
                },
                singleLine = true,
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    agriChainOutlinedColors()
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text =
                    if (locationLoading) {
                        "Detecting your current device location…"
                    } else {
                        "Tap the location icon to use the device's current location."
                    },
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    if (locationLoading) {
                        colors.primary
                    } else {
                        colors.onBackground.copy(
                            alpha = 0.52f
                        )
                    }
            )

            if (!locationError.isNullOrBlank()) {

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        locationError!!,
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        colors.error
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =====================================================
            // HARVEST DATE
            // =====================================================

            OutlinedTextField(
                value = harvestDate,
                onValueChange = {},
                readOnly = true,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {

                            dateError = null

                            showDatePicker =
                                true
                        },
                label = {
                    Text("Harvest Date")
                },
                placeholder = {
                    Text("Select harvest date")
                },
                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.CalendarMonth,
                        contentDescription =
                            null
                    )
                },
                trailingIcon = {

                    IconButton(
                        onClick = {

                            dateError = null

                            showDatePicker =
                                true
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.CalendarMonth,
                            contentDescription =
                                "Open harvest date calendar",
                            tint =
                                colors.primary
                        )
                    }
                },
                singleLine = true,
                isError =
                    dateError != null,
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    agriChainOutlinedColors()
            )

            if (!dateError.isNullOrBlank()) {

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        dateError!!,
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        colors.error
                )
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =====================================================
            // SECTION 3 — QUANTITY & QUALITY
            // =====================================================

            FormSection(
                number = "03",
                title = "Quantity & Quality"
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // -------------------------------------------------
                // QUANTITY
                // -------------------------------------------------

                OutlinedTextField(
                    value = quantity,

                    onValueChange = { value ->

                        if (
                            value.isEmpty() ||
                            value.matches(
                                Regex(
                                    "^\\d*(\\.\\d*)?$"
                                )
                            )
                        ) {

                            quantity = value
                        }
                    },

                    modifier =
                        Modifier.weight(1f),

                    label = {
                        Text("Quantity")
                    },

                    placeholder = {
                        Text("500")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.ProductionQuantityLimits,
                            contentDescription =
                                null
                        )
                    },

                    singleLine = true,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        agriChainOutlinedColors()
                )

                // -------------------------------------------------
                // UNIT DROPDOWN
                // -------------------------------------------------

                SelectionField(
                    modifier =
                        Modifier.width(120.dp),

                    value =
                        quantityUnit,

                    label =
                        "Unit",

                    placeholder =
                        "kg",

                    expanded =
                        unitExpanded,

                    onExpandedChange = {
                        unitExpanded = it
                    },

                    options =
                        quantityUnits,

                    onOptionSelected = {
                        quantityUnit = it
                    }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =====================================================
            // QUALITY
            // =====================================================

            SelectionField(
                value =
                    quality,

                label =
                    "Quality Grade",

                placeholder =
                    "Select quality",

                expanded =
                    qualityExpanded,

                onExpandedChange = {
                    qualityExpanded = it
                },

                options =
                    qualityOptions,

                onOptionSelected = {
                    quality = it
                }
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =====================================================
            // CULTIVATION METHOD
            // =====================================================

            SelectionField(
                value =
                    cultivationMethod,

                label =
                    "Cultivation Method",

                placeholder =
                    "Select method",

                expanded =
                    cultivationExpanded,

                onExpandedChange = {
                    cultivationExpanded = it
                },

                options =
                    cultivationOptions,

                onOptionSelected = {
                    cultivationMethod = it
                }
            )

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // =====================================================
            // REGISTRATION SUMMARY
            // =====================================================

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                color =
                    colors.surface,

                tonalElevation =
                    3.dp,

                border =
                    BorderStrokeSafe(
                        colors
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Row(
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
                                Modifier.width(9.dp)
                        )

                        Text(
                            text =
                                "Registration Summary",
                            style =
                                MaterialTheme.typography.titleMedium,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    SummaryRow(
                        label = "Product",
                        value =
                            productName.ifBlank {
                                "Not selected"
                            }
                    )

                    SummaryRow(
                        label = "Category",
                        value =
                            cropType.ifBlank {
                                "Not selected"
                            }
                    )

                    SummaryRow(
                        label = "Location",
                        value =
                            farmLocation.ifBlank {
                                "Not selected"
                            }
                    )

                    SummaryRow(
                        label = "Harvest",
                        value =
                            harvestDate.ifBlank {
                                "Not selected"
                            }
                    )

                    SummaryRow(
                        label = "Quantity",
                        value =
                            if (quantity.isBlank()) {
                                "Not entered"
                            } else {
                                "$quantity $quantityUnit"
                            }
                    )

                    SummaryRow(
                        label = "Quality",
                        value =
                            quality.ifBlank {
                                "Not selected"
                            }
                    )

                    SummaryRow(
                        label = "Cultivation",
                        value =
                            cultivationMethod.ifBlank {
                                "Not selected"
                            }
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // =====================================================
            // CREATE BUTTON
            // =====================================================

            Button(
                onClick = {

                    onProductCreated(
                        productName.trim(),
                        cropType,
                        farmLocation.trim(),
                        harvestDate.trim(),
                        "$quantity $quantityUnit",
                        quality,
                        cultivationMethod
                    )
                },

                enabled =
                    isFormValid,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            colors.primary,

                        contentColor =
                            colors.onPrimary,

                        disabledContainerColor =
                            colors.primary.copy(
                                alpha = 0.22f
                            ),

                        disabledContentColor =
                            colors.onPrimary.copy(
                                alpha = 0.50f
                            )
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.CheckCircle,
                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(9.dp)
                )

                Text(
                    text =
                        "Create Traceable Lot",
                    fontWeight =
                        FontWeight.ExtraBold,
                    fontSize =
                        16.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    "A unique AgriChain product ID will be generated after registration.",
                modifier =
                    Modifier.padding(
                        horizontal = 6.dp
                    ),
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    colors.onBackground.copy(
                        alpha = 0.52f
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )
        }
    }

    // =========================================================
    // DATE PICKER
    // =========================================================

    if (showDatePicker) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    selectedDateMillis
            )

        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val millis =
                            datePickerState.selectedDateMillis

                        when {

                            millis == null -> {

                                dateError =
                                    "Please select a harvest date."
                            }

                            isFutureDate(millis) -> {

                                dateError =
                                    "Harvest date cannot be in the future."
                            }

                            else -> {

                                selectedDateMillis =
                                    millis

                                harvestDate =
                                    formatHarvestDate(
                                        millis
                                    )

                                dateError = null

                                showDatePicker =
                                    false
                            }
                        }
                    }
                ) {

                    Text(
                        text = "Select",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {

                    Text("Cancel")
                }
            }

        ) {

            DatePicker(
                state =
                    datePickerState
            )
        }
    }

    // =========================================================
    // LOCATION PERMISSION DIALOG
    // =========================================================

    if (showLocationPermissionDialog) {

        AlertDialog(

            onDismissRequest = {
                showLocationPermissionDialog = false
            },

            title = {

                Text(
                    text =
                        "Location Permission Needed",
                    fontWeight =
                        FontWeight.Bold
                )
            },

            text = {

                Text(
                    text =
                        "AgriChain needs location access to automatically identify the farm location for this produce lot."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        showLocationPermissionDialog =
                            false

                        val settingsIntent =
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            ).apply {

                                data =
                                    "package:${context.packageName}"
                                        .toUri()
                            }

                        context.startActivity(
                            settingsIntent
                        )
                    }
                ) {

                    Text("Open Settings")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showLocationPermissionDialog =
                            false
                    }
                ) {

                    Text("Cancel")
                }
            }
        )
    }
}


// =============================================================
// SELECTION FIELD
// =============================================================

@Composable
private fun SelectionField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    placeholder: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {

    Box(
        modifier =
            modifier.fillMaxWidth()
    ) {

        OutlinedTextField(

            value = value,

            onValueChange = {},

            readOnly = true,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable {

                        onExpandedChange(
                            true
                        )
                    },

            label = {
                Text(label)
            },

            placeholder = {
                Text(placeholder)
            },

            trailingIcon = {

                IconButton(
                    onClick = {

                        onExpandedChange(
                            !expanded
                        )
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.KeyboardArrowDown,

                        contentDescription =
                            if (expanded) {
                                "Collapse $label"
                            } else {
                                "Expand $label"
                            },

                        tint =
                            MaterialTheme.colorScheme.primary
                    )
                }
            },

            singleLine = true,

            shape =
                RoundedCornerShape(18.dp),

            colors =
                agriChainOutlinedColors()
        )

        DropdownMenu(

            expanded =
                expanded,

            onDismissRequest = {

                onExpandedChange(
                    false
                )
            },

            modifier =
                Modifier.fillMaxWidth(0.92f)
        ) {

            options.forEach { option ->

                DropdownMenuItem(

                    text = {
                        Text(option)
                    },

                    onClick = {

                        onOptionSelected(
                            option
                        )

                        onExpandedChange(
                            false
                        )
                    }
                )
            }
        }
    }
}


// =============================================================
// FORM SECTION
// =============================================================

@Composable
private fun FormSection(
    number: String,
    title: String
) {

    val colors =
        MaterialTheme.colorScheme

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Surface(
            modifier =
                Modifier.size(34.dp),
            shape =
                RoundedCornerShape(11.dp),
            color =
                colors.primary
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        number,

                    style =
                        MaterialTheme.typography.labelMedium,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        colors.onPrimary
                )
            }
        }

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Text(
            text =
                title,

            style =
                MaterialTheme.typography.titleMedium,

            fontWeight =
                FontWeight.ExtraBold,

            color =
                colors.onBackground
        )
    }
}


// =============================================================
// PROGRESS STEP
// =============================================================

@Composable
private fun ProgressStep(
    number: String,
    title: String,
    active: Boolean
) {

    val colors =
        MaterialTheme.colorScheme

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(28.dp),

            shape =
                CircleShape,

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

                Text(
                    text =
                        number,

                    style =
                        MaterialTheme.typography.labelSmall,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        if (active) {
                            colors.onPrimary
                        } else {
                            colors.onSurfaceVariant
                        }
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(
            text =
                title,

            style =
                MaterialTheme.typography.labelSmall,

            fontWeight =
                FontWeight.Bold,

            color =
                colors.onBackground.copy(
                    alpha =
                        if (active) {
                            0.85f
                        } else {
                            0.45f
                        }
                )
        )
    }
}


// =============================================================
// PROGRESS LINE
// =============================================================

@Composable
private fun ProgressLine(
    modifier: Modifier
) {

    val colors =
        MaterialTheme.colorScheme

    Box(
        modifier =
            modifier
                .padding(
                    horizontal = 7.dp,
                    vertical = 13.dp
                )
                .height(2.dp)
                .background(
                    colors.outline.copy(
                        alpha = 0.25f
                    )
                )
    )
}


// =============================================================
// SUMMARY ROW
// =============================================================

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {

    val colors =
        MaterialTheme.colorScheme

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                )
    ) {

        Text(
            text =
                label,

            style =
                MaterialTheme.typography.bodySmall,

            color =
                colors.onSurface.copy(
                    alpha = 0.55f
                ),

            modifier =
                Modifier.weight(0.38f)
        )

        Text(
            text =
                value,

            style =
                MaterialTheme.typography.bodySmall,

            fontWeight =
                FontWeight.SemiBold,

            color =
                colors.onSurface,

            modifier =
                Modifier.weight(0.62f)
        )
    }
}


// =============================================================
// SAFE BORDER
// =============================================================

@Composable
private fun BorderStrokeSafe(
    colors:
    androidx.compose.material3.ColorScheme
): BorderStroke {

    return BorderStroke(

        width = 1.dp,

        color =
            colors.outline.copy(
                alpha = 0.10f
            )
    )
}


// =============================================================
// DATE FORMATTER
// =============================================================

private fun formatHarvestDate(
    millis: Long
): String {

    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(
        Date(millis)
    )
}


// =============================================================
// FUTURE DATE CHECK
// =============================================================

private fun isFutureDate(
    millis: Long
): Boolean {

    val selected =
        java.util.Calendar.getInstance().apply {
            timeInMillis = millis
        }

    val today =
        java.util.Calendar.getInstance()

    selected.set(
        java.util.Calendar.HOUR_OF_DAY,
        0
    )

    selected.set(
        java.util.Calendar.MINUTE,
        0
    )

    selected.set(
        java.util.Calendar.SECOND,
        0
    )

    selected.set(
        java.util.Calendar.MILLISECOND,
        0
    )

    today.set(
        java.util.Calendar.HOUR_OF_DAY,
        0
    )

    today.set(
        java.util.Calendar.MINUTE,
        0
    )

    today.set(
        java.util.Calendar.SECOND,
        0
    )

    today.set(
        java.util.Calendar.MILLISECOND,
        0
    )

    return selected.after(today)
}


// =============================================================
// TEXT FIELD COLORS
// =============================================================

@Composable
private fun agriChainOutlinedColors() =
    OutlinedTextFieldDefaults.colors(

        focusedBorderColor =
            MaterialTheme.colorScheme.primary,

        unfocusedBorderColor =
            MaterialTheme.colorScheme.outline.copy(
                alpha = 0.45f
            ),

        focusedLabelColor =
            MaterialTheme.colorScheme.primary,

        cursorColor =
            MaterialTheme.colorScheme.primary,

        focusedContainerColor =
            MaterialTheme.colorScheme.surface,

        unfocusedContainerColor =
            MaterialTheme.colorScheme.surface
    )


// =============================================================
// REVERSE GEOCODING
// =============================================================

private suspend fun getReadableLocation(
    context: Context,
    latitude: Double,
    longitude: Double
): String? {

    return try {

        val geocoder =
            Geocoder(
                context,
                Locale.getDefault()
            )

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            suspendCancellableCoroutine { continuation ->

                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1,
                    object :
                        Geocoder.GeocodeListener {

                        override fun onGeocode(
                            addresses: MutableList<Address>
                        ) {

                            val address =
                                addresses.firstOrNull()

                            val result =
                                address?.let {
                                    formatAddress(it)
                                }

                            if (
                                continuation.isActive
                            ) {

                                continuation.resume(
                                    result
                                )
                            }
                        }

                        override fun onError(
                            errorMessage: String?
                        ) {

                            if (
                                continuation.isActive
                            ) {

                                continuation.resume(
                                    null
                                )
                            }
                        }
                    }
                )
            }

        } else {

            withContext(
                Dispatchers.IO
            ) {

                @Suppress(
                    "DEPRECATION"
                )

                geocoder
                    .getFromLocation(
                        latitude,
                        longitude,
                        1
                    )
                    ?.firstOrNull()
                    ?.let {
                        formatAddress(it)
                    }
            }
        }

    } catch (_: Exception) {

        null
    }
}


// =============================================================
// ADDRESS FORMATTER
// =============================================================

private fun formatAddress(
    address: Address
): String {

    val parts =
        mutableListOf<String>()

    val city =
        address.locality
            ?.trim()
            .orEmpty()

    val state =
        address.adminArea
            ?.trim()
            .orEmpty()

    if (city.isNotBlank()) {
        parts.add(city)
    }

    if (
        state.isNotBlank() &&
        !parts.contains(state)
    ) {
        parts.add(state)
    }

    if (parts.isEmpty()) {

        val district =
            address.subAdminArea
                ?.trim()
                .orEmpty()

        if (district.isNotBlank()) {
            parts.add(district)
        }
    }

    return parts.joinToString(", ")
}


// =============================================================
// COORDINATE FALLBACK
// =============================================================

private fun formatCoordinateLocation(
    latitude: Double,
    longitude: Double
): String {

    return String.format(
        Locale.US,
        "Lat %.5f, Lon %.5f",
        latitude,
        longitude
    )
}


// =============================================================
// FIND ACTIVITY
// =============================================================

private fun Context.findActivity(): Activity? {

    var currentContext =
        this

    while (
        currentContext is ContextWrapper
    ) {

        if (
            currentContext is Activity
        ) {

            return currentContext
        }

        currentContext =
            currentContext.baseContext
    }

    return null
}