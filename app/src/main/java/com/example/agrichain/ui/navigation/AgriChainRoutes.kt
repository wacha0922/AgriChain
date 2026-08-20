package com.example.agrichain.ui.navigation

object AgriChainRoutes {

    // =========================================================
    // Authentication / Entry
    // =========================================================

    const val SPLASH = "splash"

    const val WELCOME = "welcome"

    const val SIGN_IN = "sign_in"

    const val SIGN_UP = "sign_up"

    const val VERIFY_PHONE = "verify_phone"

    // =========================================================
    // Farmer
    // =========================================================

    const val FARMER_DASHBOARD = "farmer_dashboard"

    const val ADD_PRODUCT = "add_product"

    const val PRODUCT_CREATED = "product_created"

    const val PRODUCT_CREATED_WITH_ID =
        "$PRODUCT_CREATED/{productId}"

    // =========================================================
    // Other role dashboards
    // =========================================================

    const val TRANSPORTER_DASHBOARD = "transporter_dashboard"

    const val PROCESSOR_DASHBOARD = "processor_dashboard"

    const val RETAILER_DASHBOARD = "retailer_dashboard"

    const val CONSUMER_DASHBOARD = "consumer_dashboard"

    const val GOVERNMENT_DASHBOARD = "government_dashboard"

    // =========================================================
    // QR / Product Verification
    // =========================================================

    const val QR_SCANNER = "qr_scanner"

    const val PRODUCT_VERIFICATION = "product_verification"

    const val PRODUCT_VERIFICATION_WITH_ID =
        "$PRODUCT_VERIFICATION/{productId}"

    // =========================================================
    // Role-aware Sign In
    // =========================================================

    const val SIGN_IN_WITH_ROLE =
        "$SIGN_IN/{role}"

    fun signIn(role: String): String {
        return "$SIGN_IN/${role.encodeForNavigation()}"
    }

    // =========================================================
    // Product Created
    // =========================================================

    fun productCreated(productId: String): String {
        return "$PRODUCT_CREATED/${productId.encodeForNavigation()}"
    }

    // =========================================================
    // Product Verification
    // =========================================================

    fun productVerification(productId: String): String {
        return "$PRODUCT_VERIFICATION/${productId.encodeForNavigation()}"
    }

    // =========================================================
    // Navigation-safe encoding
    // =========================================================

    private fun String.encodeForNavigation(): String {
        return replace(" ", "_")
    }
}