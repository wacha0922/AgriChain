package com.example.agrichain.data.model

/**
 * Represents an agricultural product / traceability lot
 * registered in AgriChain.
 *
 * Firestore is the shared source of truth.
 * Room is used as a local/offline cache.
 */
data class Product(

    val productId: String,

    /**
     * Firebase UID of the farmer who created the product.
     */
    val farmerId: String = "",

    val productName: String,

    val cropType: String,

    val farmLocation: String,

    val harvestDate: String,

    val quantity: String,

    val quality: String,

    val cultivationMethod: String,

    val createdAt: Long = System.currentTimeMillis(),

    val status: ProductStatus = ProductStatus.CREATED,

    val blockchainStatus: BlockchainStatus =
        BlockchainStatus.PENDING
)

/**
 * Current lifecycle status of the product.
 */
enum class ProductStatus {

    CREATED,

    HARVESTED,

    IN_TRANSIT,

    PROCESSED,

    AT_RETAILER,

    SOLD
}

/**
 * Current Polygon/blockchain state of the product record.
 */
enum class BlockchainStatus {

    PENDING,

    SUBMITTED,

    CONFIRMED,

    FAILED
}