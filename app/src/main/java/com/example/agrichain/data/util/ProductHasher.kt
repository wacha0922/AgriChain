package com.example.agrichain.data.util

import com.example.agrichain.data.model.Product
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.security.MessageDigest
import java.util.Locale

/**
 * Calculates a deterministic SHA-256 fingerprint for an AgriChain product.
 *
 * The complete product record remains in Firestore.
 * Only the resulting hash will be stored on Polygon.
 */
object ProductHasher {

    /**
     * Generates a 0x-prefixed, 64-character SHA-256 hash.
     *
     * The result has the hexadecimal format required by Solidity bytes32.
     */
    fun sha256(product: Product): String {

        val serializedData = ByteArrayOutputStream().use { buffer ->

            DataOutputStream(buffer).use { output ->

                // Version identifier for this serialization format.
                output.writeField("AgriChainProductV1")

                // Keep this order unchanged for consistent hashing.
                output.writeField(product.productId)
                output.writeField(product.farmerId)
                output.writeField(product.productName)
                output.writeField(product.cropType)
                output.writeField(product.farmLocation)
                output.writeField(product.harvestDate)
                output.writeField(product.quantity)
                output.writeField(product.quality)
                output.writeField(product.cultivationMethod)
                output.writeField(product.createdAt.toString())
            }

            buffer.toByteArray()
        }

        val hashBytes = MessageDigest
            .getInstance("SHA-256")
            .digest(serializedData)

        return buildString {
            append("0x")

            hashBytes.forEach { byte ->
                append(
                    String.format(
                        Locale.ROOT,
                        "%02x",
                        byte.toInt() and 0xff
                    )
                )
            }
        }
    }

    /**
     * Writes each string as its UTF-8 byte length followed by its bytes.
     * This prevents ambiguity when fields are joined together.
     */
    private fun DataOutputStream.writeField(value: String) {

        val bytes = value.toByteArray(Charsets.UTF_8)

        writeInt(bytes.size)
        write(bytes)
    }
}