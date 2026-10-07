package com.example.agrichain.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

object DeviceLocationHelper {

    suspend fun getReadableLocation(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String? {

        return try {

            val geocoder = Geocoder(
                context,
                Locale.getDefault()
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                suspendCancellableCoroutine { continuation ->

                    geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1,
                        object : Geocoder.GeocodeListener {

                            override fun onGeocode(
                                addresses: MutableList<Address>
                            ) {

                                val address =
                                    addresses.firstOrNull()

                                val result =
                                    address?.let {
                                        formatAddress(it)
                                    }

                                if (continuation.isActive) {
                                    continuation.resume(result)
                                }
                            }

                            override fun onError(
                                errorMessage: String?
                            ) {

                                if (continuation.isActive) {
                                    continuation.resume(null)
                                }
                            }
                        }
                    )
                }

            } else {

                withContext(Dispatchers.IO) {

                    @Suppress("DEPRECATION")
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

    private fun formatAddress(
        address: Address
    ): String {

        val parts = mutableListOf<String>()

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

    fun fallbackCoordinateLocation(
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
}