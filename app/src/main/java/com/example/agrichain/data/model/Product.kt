package com.example.agrichain.data.model

/**
 * Represents an agricultural product registered in AgriChain.
 *
 * This is currently an in-memory/domain model.
 * Persistent database storage and Polygon blockchain storage
 * will be added in later phases.
 */
data class Product(
    val productId: String,
    val productName: String,
    val cropType: String,
    val farmLocation: String,
    val harvestDate: String,
    val quantity: String,
    val quality: String,
    val cultivationMethod: String,
    val createdAt: Long = System.currentTimeMillis(),
    val status: ProductStatus = ProductStatus.CREATED,
    val blockchainStatus: BlockchainStatus = BlockchainStatus.PENDING
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
    FAILED,
}