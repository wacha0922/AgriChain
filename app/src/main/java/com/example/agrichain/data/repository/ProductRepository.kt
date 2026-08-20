package com.example.agrichain.data.repository

import android.content.Context
import androidx.room.Room
import com.example.agrichain.data.local.AgriChainDatabase
import com.example.agrichain.data.local.ProductEntity
import com.example.agrichain.data.model.BlockchainStatus
import com.example.agrichain.data.model.Product
import com.example.agrichain.data.model.ProductStatus

/**
 * Repository for AgriChain products.
 *
 * Room is now the persistent data source.
 *
 * The repository keeps the conversion between:
 *
 * Product        -> application/domain model
 * ProductEntity  -> Room/database model
 */
object ProductRepository {

    private var database: AgriChainDatabase? = null

    /**
     * Initialize the repository with the application context.
     *
     * This must be called once when the app starts.
     */
    fun initialize(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AgriChainDatabase::class.java,
                AgriChainDatabase.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    private fun getDatabase(): AgriChainDatabase {
        return database
            ?: throw IllegalStateException(
                "ProductRepository has not been initialized. " +
                        "Call ProductRepository.initialize(context) " +
                        "when the application starts."
            )
    }

    /**
     * Add a new product.
     *
     * The actual database write will be connected to
     * the coroutine-based DAO in the next step.
     */
    suspend fun addProduct(product: Product) {
        getDatabase()
            .productDao()
            .insertProduct(product.toEntity())
    }

    /**
     * Return all stored products.
     */
    suspend fun getAllProducts(): List<Product> {
        return getDatabase()
            .productDao()
            .getAllProducts()
            .map { it.toProduct() }
    }

    /**
     * Find a product by its unique Product ID.
     */
    suspend fun getProductById(
        productId: String
    ): Product? {
        return getDatabase()
            .productDao()
            .getProductById(productId)
            ?.toProduct()
    }

    /**
     * Remove a product by Product ID.
     */
    suspend fun removeProduct(
        productId: String
    ): Boolean {
        val productEntity =
            getDatabase()
                .productDao()
                .getProductById(productId)

        return if (productEntity != null) {
            getDatabase()
                .productDao()
                .deleteProduct(productEntity)

            true
        } else {
            false
        }
    }

    /**
     * Remove all products.
     *
     * Development/testing utility.
     */
    suspend fun clear() {
        getDatabase()
            .productDao()
            .deleteAllProducts()
    }

    // ---------------------------------------------------------
    // Product -> ProductEntity
    // ---------------------------------------------------------

    private fun Product.toEntity(): ProductEntity {
        return ProductEntity(
            productId = productId,
            productName = productName,
            cropType = cropType,
            farmLocation = farmLocation,
            harvestDate = harvestDate,
            quantity = quantity,
            quality = quality,
            cultivationMethod = cultivationMethod,
            createdAt = createdAt,
            status = status.name,
            blockchainStatus = blockchainStatus.name
        )
    }

    // ---------------------------------------------------------
    // ProductEntity -> Product
    // ---------------------------------------------------------

    private fun ProductEntity.toProduct(): Product {
        return Product(
            productId = productId,
            productName = productName,
            cropType = cropType,
            farmLocation = farmLocation,
            harvestDate = harvestDate,
            quantity = quantity,
            quality = quality,
            cultivationMethod = cultivationMethod,
            createdAt = createdAt,
            status = parseProductStatus(status),
            blockchainStatus = parseBlockchainStatus(blockchainStatus)
        )
    }

    private fun parseProductStatus(
        value: String
    ): ProductStatus {
        return try {
            ProductStatus.valueOf(value)
        } catch (_: IllegalArgumentException) {
            ProductStatus.CREATED
        }
    }

    private fun parseBlockchainStatus(
        value: String
    ): BlockchainStatus {
        return try {
            BlockchainStatus.valueOf(value)
        } catch (_: IllegalArgumentException) {
            BlockchainStatus.PENDING
        }
    }
}