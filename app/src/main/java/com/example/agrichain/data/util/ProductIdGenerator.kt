package com.example.agrichain.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Generates unique identifiers for AgriChain products.
 *
 * Prototype format:
 * AGRI-YYYYMMDD-XXXXXXXX
 *
 * Example:
 * AGRI-20260813-A7F31C92
 *
 * The identifier is generated locally for now.
 * Later it will be used as the primary reference for:
 * - Product records
 * - QR codes
 * - Polygon transactions
 * - Consumer verification
 */
object ProductIdGenerator {

    private val dateFormatter =
        SimpleDateFormat(
            "yyyyMMdd",
            Locale.US
        )

    fun generate(): String {
        val datePart = dateFormatter.format(Date())

        val uniquePart = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .take(8)
            .uppercase(Locale.US)

        return "AGRI-$datePart-$uniquePart"
    }
}