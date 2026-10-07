package com.example.agrichain.ui.navigation

object AgriChainRoutes {

    const val SPLASH = "splash"
    const val WELCOME = "welcome"

    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val VERIFY_PHONE = "verify_phone"

    const val SIGN_IN_WITH_ROLE = "sign_in/{role}"

    const val FARMER_DASHBOARD = "farmer_dashboard"
    const val ADD_PRODUCT = "add_product"

    const val PRODUCT_CREATED = "product_created"
    const val PRODUCT_CREATED_WITH_ID =
        "product_created/{productId}"

    const val PRODUCT_DETAILS =
        "product_details"

    const val PRODUCT_DETAILS_WITH_ID =
        "product_details/{productId}"

    const val PRODUCT_QR =
        "product_qr"

    const val PRODUCT_QR_WITH_ID =
        "product_qr/{productId}"

    const val TRANSPORTER_DASHBOARD =
        "transporter_dashboard"

    const val PROCESSOR_DASHBOARD =
        "processor_dashboard"

    const val RETAILER_DASHBOARD =
        "retailer_dashboard"

    const val CONSUMER_DASHBOARD =
        "consumer_dashboard"

    const val GOVERNMENT_DASHBOARD =
        "government_dashboard"

    const val QR_SCANNER =
        "qr_scanner"

    const val PRODUCT_VERIFICATION =
        "product_verification"

    const val PRODUCT_VERIFICATION_WITH_ID =
        "product_verification/{productId}"


    fun signIn(role: String): String {
        return "sign_in/${encodeForNavigation(role)}"
    }

    fun productCreated(productId: String): String {
        return "$PRODUCT_CREATED/${encodeForNavigation(productId)}"
    }

    fun productDetails(productId: String): String {
        return "$PRODUCT_DETAILS/${encodeForNavigation(productId)}"
    }

    fun productQr(productId: String): String {
        return "$PRODUCT_QR/${encodeForNavigation(productId)}"
    }

    fun productVerification(productId: String): String {
        return "$PRODUCT_VERIFICATION/${encodeForNavigation(productId)}"
    }

    private fun encodeForNavigation(value: String): String {
        return value.replace(" ", "_")
    }
}