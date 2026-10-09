package com.example.agrichain.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.agrichain.data.auth.FirebaseAuthRepository
import com.example.agrichain.data.auth.GoogleAuthRepository
import com.example.agrichain.data.model.BlockchainStatus
import com.example.agrichain.data.model.Product
import com.example.agrichain.data.model.ProductStatus
import com.example.agrichain.data.repository.ProductRepository
import com.example.agrichain.data.util.ProductIdGenerator
import com.example.agrichain.ui.screens.auth.SignInScreen
import com.example.agrichain.ui.screens.auth.SignUpScreen
import com.example.agrichain.ui.screens.auth.WelcomeScreen
import com.example.agrichain.ui.screens.consumer.ProductVerificationScreen
import com.example.agrichain.ui.screens.consumer.QrScannerScreen
import com.example.agrichain.ui.screens.dashboard.RoleDashboardScreen
import com.example.agrichain.ui.screens.farmer.AddProductScreen
import com.example.agrichain.ui.screens.farmer.FarmerDashboardScreen
import com.example.agrichain.ui.screens.farmer.FarmerProductPreview
import com.example.agrichain.ui.screens.farmer.ProductCreatedScreen
import com.example.agrichain.ui.screens.farmer.ProductDetailsScreen
import com.example.agrichain.ui.screens.farmer.ProductQrScreen
import com.example.agrichain.ui.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun AgriChainNavGraph(
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val authRepository = remember {
        FirebaseAuthRepository()
    }

    val googleAuthRepository = remember(context) {
        GoogleAuthRepository(context)
    }

    fun navigateToRoleDashboard(
        role: String
    ) {

        val destination = when (role) {

            "Farmer" ->
                AgriChainRoutes.FARMER_DASHBOARD

            "Transporter" ->
                AgriChainRoutes.TRANSPORTER_DASHBOARD

            "Processor" ->
                AgriChainRoutes.PROCESSOR_DASHBOARD

            "Retailer" ->
                AgriChainRoutes.RETAILER_DASHBOARD

            "Consumer" ->
                AgriChainRoutes.CONSUMER_DASHBOARD

            "Government" ->
                AgriChainRoutes.GOVERNMENT_DASHBOARD

            else ->
                AgriChainRoutes.FARMER_DASHBOARD
        }

        navController.navigate(destination) {

            popUpTo(
                AgriChainRoutes.SIGN_IN
            ) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun safeNavigateBack() {
        if (!navController.popBackStack()) {
            navController.navigate(AgriChainRoutes.WELCOME) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AgriChainRoutes.SPLASH
    ) {

        // =========================================================
        // SPLASH
        // =========================================================

        composable(
            route = AgriChainRoutes.SPLASH
        ) {

            SplashScreen(
                onSplashFinished = {

                    navController.navigate(
                        AgriChainRoutes.WELCOME
                    ) {

                        popUpTo(
                            AgriChainRoutes.SPLASH
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =========================================================
        // WELCOME
        // =========================================================

        composable(
            route = AgriChainRoutes.WELCOME
        ) {

            WelcomeScreen(
                onRoleSelected = { selectedRole ->

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "selectedRole",
                            selectedRole
                        )

                    navController.navigate(
                        AgriChainRoutes.SIGN_IN
                    )
                }
            )
        }

        // =========================================================
        // SIGN IN
        // =========================================================

        composable(
            route = AgriChainRoutes.SIGN_IN
        ) {

            val selectedRole =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("selectedRole")
                    ?: "Farmer"

            var isLoading by remember {
                mutableStateOf(false)
            }

            var errorMessage by remember {
                mutableStateOf<String?>(null)
            }

            var successMessage by remember {
                mutableStateOf<String?>(null)
            }

            LaunchedEffect(Unit) {

                val message =
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.get<String>(
                            "authSuccessMessage"
                        )

                if (!message.isNullOrBlank()) {

                    successMessage = message

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<String>(
                            "authSuccessMessage"
                        )
                }
            }

            SignInScreen(

                selectedRole = selectedRole,

                isLoading = isLoading,

                errorMessage = errorMessage,

                successMessage = successMessage,

                onSignIn = { email, password ->

                    errorMessage = null
                    successMessage = null
                    isLoading = true

                    scope.launch {

                        val result =
                            authRepository.signIn(
                                email = email,
                                password = password
                            )

                        isLoading = false

                        result.onSuccess {

                            navigateToRoleDashboard(
                                selectedRole
                            )

                        }.onFailure { exception ->

                            errorMessage =
                                getAuthErrorMessage(
                                    exception
                                )
                        }
                    }
                },

                onGoogleSignIn = {

                    errorMessage = null
                    successMessage = null
                    isLoading = true

                    scope.launch {

                        val result =
                            googleAuthRepository
                                .signInWithGoogle()

                        isLoading = false

                        result.onSuccess {

                            navigateToRoleDashboard(
                                selectedRole
                            )

                        }.onFailure { exception ->

                            errorMessage =
                                getAuthErrorMessage(
                                    exception
                                )
                        }
                    }
                },

                onForgotPassword = {

                    errorMessage =
                        "Enter your email address and use password recovery. Password reset will be completed in the next authentication step."
                },

                onSignUp = {

                    navController.navigate(
                        AgriChainRoutes.SIGN_UP
                    )
                }
            )
        }

        // =========================================================
        // SIGN UP
        // =========================================================

        composable(
            route = AgriChainRoutes.SIGN_UP
        ) {

            SignUpScreen(

                onBack = {
                    safeNavigateBack()
                },

                onCreateAccount = {
                        fullName,
                        email,
                        phone,
                        password ->

                    scope.launch {

                        val result =
                            authRepository.createAccount(
                                email = email,
                                password = password
                            )

                        result.onSuccess { _ ->

                            val verificationResult =
                                authRepository
                                    .sendEmailVerification()

                            verificationResult
                                .onSuccess {

                                    navController
                                        .previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(
                                            "authSuccessMessage",
                                            "Account created successfully. A verification email has been sent to $email."
                                        )

                                    authRepository.signOut()

                                    safeNavigateBack()

                                }
                                .onFailure { exception ->

                                    navController
                                        .currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(
                                            "authErrorMessage",
                                            getAuthErrorMessage(
                                                exception
                                            )
                                        )
                                }

                        }.onFailure { exception ->

                            navController
                                .currentBackStackEntry
                                ?.savedStateHandle
                                ?.set(
                                    "authErrorMessage",
                                    getAuthErrorMessage(
                                        exception
                                    )
                                )
                        }
                    }
                },

                onSignIn = {
                    safeNavigateBack()
                }
            )
        }

        // =========================================================
        // FARMER DASHBOARD
        // =========================================================

        composable(
            route = AgriChainRoutes.FARMER_DASHBOARD
        ) {

            var products by remember {
                mutableStateOf<List<Product>>(emptyList())
            }

            LaunchedEffect(Unit) {
                products = ProductRepository.getProductsForCurrentFarmer().ifEmpty {
                    ProductRepository.getAllProducts()
                }
            }

            FarmerDashboardScreen(

                farmerName = authRepository.getCurrentUser()?.displayName?.ifBlank { "Farmer" } ?: "Farmer",

                activeLots = products.count { it.status == ProductStatus.CREATED || it.status == ProductStatus.HARVESTED },

                inTransit = products.count { it.status == ProductStatus.IN_TRANSIT },

                verifiedLots = products.count { it.blockchainStatus == BlockchainStatus.CONFIRMED },

                recentProducts = products.map { p ->
                    FarmerProductPreview(
                        productId = p.productId,
                        name = p.productName,
                        cropType = p.cropType,
                        quantity = p.quantity,
                        status = p.status.name.replace("_", " ")
                    )
                },

                onAddProduct = {

                    navController.navigate(
                        AgriChainRoutes.ADD_PRODUCT
                    )
                },

                onProductSelected = { productId ->

                    navController.navigate(
                        AgriChainRoutes.productDetails(
                            productId
                        )
                    )
                }
            )
        }

        // =========================================================
        // ADD PRODUCT
        // =========================================================

        composable(
            route = AgriChainRoutes.ADD_PRODUCT
        ) {

            AddProductScreen(

                onBack = {
                    safeNavigateBack()
                },

                onProductCreated = {
                        productName,
                        cropType,
                        farmLocation,
                        harvestDate,
                        quantity,
                        quality,
                        cultivationMethod ->

                    val productId =
                        ProductIdGenerator.generate()

                    val product = Product(
                        productId = productId,
                        productName = productName,
                        cropType = cropType,
                        farmLocation = farmLocation,
                        harvestDate = harvestDate,
                        quantity = quantity,
                        quality = quality,
                        cultivationMethod =
                            cultivationMethod
                    )

                    scope.launch {

                        ProductRepository.addProduct(
                            product
                        )

                        navController.navigate(
                            AgriChainRoutes.productCreated(
                                productId
                            )
                        )
                    }
                }
            )
        }

        // =========================================================
        // PRODUCT CREATED
        // =========================================================

        composable(
            route =
                AgriChainRoutes.PRODUCT_CREATED_WITH_ID,

            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments
                    ?.getString("productId")

            var product by remember {
                mutableStateOf<Product?>(null)
            }

            var isLoading by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(productId) {

                if (!productId.isNullOrBlank()) {

                    product =
                        ProductRepository
                            .getProductById(
                                productId
                            )
                }

                isLoading = false
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                product?.let { currentProduct ->

                    ProductCreatedScreen(

                        product = currentProduct,

                        onGenerateQr = {

                            navController.navigate(
                                AgriChainRoutes.productQr(
                                    currentProduct.productId
                                )
                            )
                        },

                        onDone = {

                            navController.navigate(
                                AgriChainRoutes.FARMER_DASHBOARD
                            ) {

                                popUpTo(
                                    AgriChainRoutes.FARMER_DASHBOARD
                                ) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Product not found.")
                    }
                }
            }
        }

        // =========================================================
        // PRODUCT DETAILS
        // =========================================================

        composable(
            route =
                AgriChainRoutes.PRODUCT_DETAILS_WITH_ID,

            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments
                    ?.getString("productId")

            var product by remember {
                mutableStateOf<Product?>(null)
            }

            var isLoading by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(productId) {

                if (!productId.isNullOrBlank()) {

                    product =
                        ProductRepository
                            .getProductById(
                                productId
                            )
                }

                isLoading = false
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                product?.let { currentProduct ->

                    ProductDetailsScreen(

                        product = currentProduct,

                        onBack = {
                            safeNavigateBack()
                        },

                        onGenerateQr = {

                            navController.navigate(
                                AgriChainRoutes.productQr(
                                    currentProduct.productId
                                )
                            )
                        },

                        onVerifyProduct = {

                            navController.navigate(
                                AgriChainRoutes.productVerification(
                                    currentProduct.productId
                                )
                            )
                        }
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Product details unavailable.")
                    }
                }
            }
        }

        // =========================================================
        // PRODUCT QR
        // =========================================================

        composable(
            route =
                AgriChainRoutes.PRODUCT_QR_WITH_ID,

            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments
                    ?.getString("productId")

            var product by remember {
                mutableStateOf<Product?>(null)
            }

            LaunchedEffect(productId) {

                if (!productId.isNullOrBlank()) {

                    product =
                        ProductRepository
                            .getProductById(
                                productId
                            )
                }
            }

            ProductQrScreen(

                productId =
                    productId ?: currentProductOrEmpty(product),

                productName =
                    product?.productName ?: "Agricultural Product",

                onBack = {
                    safeNavigateBack()
                }
            )
        }

        // =========================================================
        // CONSUMER DASHBOARD
        // =========================================================

        composable(
            route =
                AgriChainRoutes.CONSUMER_DASHBOARD
        ) {

            QrScannerScreen(

                onBack = {
                    safeNavigateBack()
                },

                onProductFound = {
                        scannedProductId ->

                    scope.launch {

                        val product =
                            ProductRepository
                                .getProductById(
                                    scannedProductId
                                )

                        if (product != null) {

                            navController.navigate(
                                AgriChainRoutes.productVerification(
                                    product.productId
                                )
                            )
                        }
                    }
                }
            )
        }

        // =========================================================
        // QR SCANNER
        // =========================================================

        composable(
            route = AgriChainRoutes.QR_SCANNER
        ) {

            QrScannerScreen(

                onBack = {
                    safeNavigateBack()
                },

                onProductFound = {
                        scannedProductId ->

                    scope.launch {

                        val product =
                            ProductRepository
                                .getProductById(
                                    scannedProductId
                                )

                        if (product != null) {

                            navController.navigate(
                                AgriChainRoutes.productVerification(
                                    product.productId
                                )
                            )
                        }
                    }
                }
            )
        }

        // =========================================================
        // PRODUCT VERIFICATION
        // =========================================================

        composable(
            route =
                AgriChainRoutes
                    .PRODUCT_VERIFICATION_WITH_ID,

            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments
                    ?.getString("productId")

            var product by remember {
                mutableStateOf<Product?>(null)
            }

            var isLoading by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(productId) {

                if (!productId.isNullOrBlank()) {

                    product =
                        ProductRepository
                            .getProductById(
                                productId
                            )
                }

                isLoading = false
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                product?.let { currentProduct ->

                    ProductVerificationScreen(

                        product = currentProduct,

                        onScanAnother = {

                            navController.navigate(
                                AgriChainRoutes.QR_SCANNER
                            ) {

                                popUpTo(
                                    AgriChainRoutes
                                        .PRODUCT_VERIFICATION_WITH_ID
                                ) {
                                    inclusive = true
                                }
                            }
                        },

                        onDone = {
                            safeNavigateBack()
                        }
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Verification record unavailable.")
                    }
                }
            }
        }

        // =========================================================
        // OTHER DASHBOARDS
        // =========================================================

        composable(
            route =
                AgriChainRoutes.TRANSPORTER_DASHBOARD
        ) {
            var products by remember {
                mutableStateOf<List<Product>>(emptyList())
            }

            LaunchedEffect(Unit) {
                products = ProductRepository.getAllProducts()
            }

            RoleDashboardScreen(
                roleTitle = "Transporter",
                roleIcon = "🚚",
                roleDescription = "Logistics & Transport Records",
                products = products,
                onScanQr = {
                    navController.navigate(AgriChainRoutes.QR_SCANNER)
                },
                onProductSelected = { productId ->
                    navController.navigate(AgriChainRoutes.productDetails(productId))
                },
                onSwitchRole = {
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignOut = {
                    authRepository.signOut()
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route =
                AgriChainRoutes.PROCESSOR_DASHBOARD
        ) {
            var products by remember {
                mutableStateOf<List<Product>>(emptyList())
            }

            LaunchedEffect(Unit) {
                products = ProductRepository.getAllProducts()
            }

            RoleDashboardScreen(
                roleTitle = "Processor",
                roleIcon = "🏭",
                roleDescription = "Processing & Quality Compliance",
                products = products,
                onScanQr = {
                    navController.navigate(AgriChainRoutes.QR_SCANNER)
                },
                onProductSelected = { productId ->
                    navController.navigate(AgriChainRoutes.productDetails(productId))
                },
                onSwitchRole = {
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignOut = {
                    authRepository.signOut()
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route =
                AgriChainRoutes.RETAILER_DASHBOARD
        ) {
            var products by remember {
                mutableStateOf<List<Product>>(emptyList())
            }

            LaunchedEffect(Unit) {
                products = ProductRepository.getAllProducts()
            }

            RoleDashboardScreen(
                roleTitle = "Retailer",
                roleIcon = "🏪",
                roleDescription = "Retail Inventory & Provenance",
                products = products,
                onScanQr = {
                    navController.navigate(AgriChainRoutes.QR_SCANNER)
                },
                onProductSelected = { productId ->
                    navController.navigate(AgriChainRoutes.productDetails(productId))
                },
                onSwitchRole = {
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignOut = {
                    authRepository.signOut()
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route =
                AgriChainRoutes.GOVERNMENT_DASHBOARD
        ) {
            var products by remember {
                mutableStateOf<List<Product>>(emptyList())
            }

            LaunchedEffect(Unit) {
                products = ProductRepository.getAllProducts()
            }

            RoleDashboardScreen(
                roleTitle = "Government",
                roleIcon = "🏛️",
                roleDescription = "Regulatory & Supply Supervision",
                products = products,
                onScanQr = {
                    navController.navigate(AgriChainRoutes.QR_SCANNER)
                },
                onProductSelected = { productId ->
                    navController.navigate(AgriChainRoutes.productDetails(productId))
                },
                onSwitchRole = {
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignOut = {
                    authRepository.signOut()
                    navController.navigate(AgriChainRoutes.WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

private fun currentProductOrEmpty(product: Product?): String {
    return product?.productId ?: ""
}

private fun getAuthErrorMessage(
    exception: Throwable
): String {

    val message =
        exception.message
            ?.lowercase()
            ?: ""

    return when {

        "password is invalid" in message ||
                "invalid credential" in message ||
                "wrong-password" in message ->
            "The email or password is incorrect."

        "no user record" in message ||
                "user-not-found" in message ->
            "No account exists with this email address."

        "email-already-in-use" in message ||
                "already in use" in message ->
            "An account already exists with this email address."

        "email address is badly formatted" in message ||
                "invalid-email" in message ->
            "Please enter a valid email address."

        "weak-password" in message ->
            "The password does not meet the required security rules."

        "network" in message ||
                "network-request-failed" in message ->
            "Network error. Check your internet connection and try again."

        "verify your email" in message ->
            "Please verify your email address before signing in."

        "canceled" in message ||
                "cancelled" in message ->
            "Google sign-in was cancelled."

        "developer" in message ->
            "Google sign-in configuration is incomplete. Check the Firebase OAuth configuration."

        else ->
            exception.message
                ?: "Authentication failed. Please try again."
    }
}
