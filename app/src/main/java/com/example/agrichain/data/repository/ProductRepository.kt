package com.example.agrichain.data.repository

import android.content.Context
import androidx.room.Room
import com.example.agrichain.data.local.AgriChainDatabase
import com.example.agrichain.data.local.ProductEntity
import com.example.agrichain.data.model.BlockchainStatus
import com.example.agrichain.data.model.Product
import com.example.agrichain.data.model.ProductStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Central repository for AgriChain products.
 *
 * Architecture:
 *
 * Firestore = shared cloud source of truth
 * Room      = local/offline cache
 *
 * UI -> Repository -> Firestore / Room
 */
object ProductRepository {

    private var database: AgriChainDatabase? = null

    private const val PRODUCTS_COLLECTION = "products"

    // =========================================================
    // FIREBASE ACCESS
    // =========================================================

    /**
     * Firebase instances are deliberately obtained through
     * functions instead of being stored in static fields.
     *
     * This avoids the Android Lint warning about a static
     * reference retaining a Context through Firebase internals.
     */
    private fun getFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    private fun getAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // =========================================================
    // ROOM INITIALIZATION
    // =========================================================

    fun initialize(context: Context) {

        if (database == null) {

            database =
                Room.databaseBuilder(
                    context.applicationContext,
                    AgriChainDatabase::class.java,
                    AgriChainDatabase.DATABASE_NAME
                )
                    .addMigrations(
                        AgriChainDatabase.MIGRATION_1_2
                    )
                    .build()
        }
    }

    // =========================================================
    // INTERNAL DATABASE ACCESS
    // =========================================================

    private fun getDatabase(): AgriChainDatabase {

        return database
            ?: throw IllegalStateException(
                "ProductRepository has not been initialized. " +
                        "Call ProductRepository.initialize(context) " +
                        "when the application starts."
            )
    }

    // =========================================================
    // ADD PRODUCT
    // =========================================================

    /**
     * Saves a new product to Firestore and then stores a local
     * Room copy.
     *
     * The farmerId is ALWAYS taken from the currently
     * authenticated Firebase user.
     */
    suspend fun addProduct(
        product: Product
    ): Result<Unit> {

        return try {

            val currentUser =
                getAuth().currentUser
                    ?: return Result.failure(
                        IllegalStateException(
                            "You must be signed in to register a product."
                        )
                    )

            val productToStore =
                product.copy(
                    farmerId = currentUser.uid
                )

            // -------------------------------------------------
            // STEP 1: Write to Firestore
            // -------------------------------------------------

            getFirestore()
                .collection(PRODUCTS_COLLECTION)
                .document(productToStore.productId)
                .set(
                    productToFirestoreMap(
                        productToStore
                    )
                )
                .await()

            // -------------------------------------------------
            // STEP 2: Update local Room cache
            // -------------------------------------------------

            getDatabase()
                .productDao()
                .insertProduct(
                    productToEntity(
                        productToStore
                    )
                )

            Result.success(Unit)

        } catch (exception: Exception) {

            Result.failure(
                exception
            )
        }
    }

    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    /**
     * Loads all products from Firestore.
     *
     * If Firestore cannot be reached, Room is used as the
     * offline fallback.
     */
    suspend fun  getAllProducts(): List<Product> {

        return try {

            val snapshot =
                getFirestore()
                    .collection(PRODUCTS_COLLECTION)
                    .get()
                    .await()

            val products =
                snapshot.documents
                    .mapNotNull { document ->
                        documentToProduct(
                            document
                        )
                    }
                    .sortedByDescending { product ->
                        product.createdAt
                    }

            // Update Room cache
            products.forEach { product ->

                getDatabase()
                    .productDao()
                    .insertProduct(
                        productToEntity(
                            product
                        )
                    )
            }

            products

        } catch (_: Exception) {

            getDatabase()
                .productDao()
                .getAllProducts()
                .map { entity ->

                    entityToProduct(
                        entity
                    )
                }
        }
    }

    // =========================================================
    // GET CURRENT FARMER'S PRODUCTS
    // =========================================================

    /**
     * Gets products belonging to the currently authenticated
     * farmer.
     */
    suspend fun getProductsForCurrentFarmer():
            List<Product> {

        val currentUser =
            getAuth().currentUser
                ?: return emptyList()

        return getProductsByFarmerId(
            currentUser.uid
        )
    }

    // =========================================================
    // GET PRODUCTS BY FARMER ID
    // =========================================================

    suspend fun getProductsByFarmerId(
        farmerId: String
    ): List<Product> {

        return try {

            /*
             * We only filter by farmerId on Firestore.
             *
             * Sorting is performed locally so we don't require
             * a composite Firestore index at this stage.
             */
            val snapshot =
                getFirestore()
                    .collection(PRODUCTS_COLLECTION)
                    .whereEqualTo(
                        "farmerId",
                        farmerId
                    )
                    .get()
                    .await()

            val products =
                snapshot.documents
                    .mapNotNull { document ->
                        documentToProduct(
                            document
                        )
                    }
                    .sortedByDescending { product ->
                        product.createdAt
                    }

            // Update Room cache
            products.forEach { product ->

                getDatabase()
                    .productDao()
                    .insertProduct(
                        productToEntity(
                            product
                        )
                    )
            }

            products

        } catch (_: Exception) {

            getDatabase()
                .productDao()
                .getProductsByFarmerId(
                    farmerId
                )
                .map { entity ->

                    entityToProduct(
                        entity
                    )
                }
        }
    }

    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    /**
     * Attempts to load a product from Firestore first.
     *
     * If the network request fails, Room is used as the
     * offline fallback.
     */
    suspend fun getProductById(
        productId: String
    ): Product? {

        return try {

            val document =
                getFirestore()
                    .collection(PRODUCTS_COLLECTION)
                    .document(productId)
                    .get()
                    .await()

            if (!document.exists()) {

                null

            } else {

                val product =
                    documentToProduct(
                        document
                    )

                if (product != null) {

                    getDatabase()
                        .productDao()
                        .insertProduct(
                            productToEntity(
                                product
                            )
                        )
                }

                product
            }

        } catch (_: Exception) {

            getDatabase()
                .productDao()
                .getProductById(
                    productId
                )
                ?.let { entity ->

                    entityToProduct(
                        entity
                    )
                }
        }
    }

    // =========================================================
    // REMOVE PRODUCT
    // =========================================================

    /**
     * Deletes a product from Firestore and removes the local
     * Room copy.
     */
    suspend fun removeProduct(
        productId: String
    ): Result<Boolean> {

        return try {

            getFirestore()
                .collection(PRODUCTS_COLLECTION)
                .document(productId)
                .delete()
                .await()

            val localProduct =
                getDatabase()
                    .productDao()
                    .getProductById(
                        productId
                    )

            if (localProduct != null) {

                getDatabase()
                    .productDao()
                    .deleteProduct(
                        localProduct
                    )
            }

            Result.success(true)

        } catch (exception: Exception) {

            Result.failure(
                exception
            )
        }
    }

    // =========================================================
    // CLEAR LOCAL ROOM CACHE
    // =========================================================

    /**
     * Development utility.
     *
     * IMPORTANT:
     * This only clears the local Room cache.
     *
     * Firestore data remains untouched.
     */
    suspend fun clear() {

        getDatabase()
            .productDao()
            .deleteAllProducts()
    }

    // =========================================================
    // PRODUCT -> FIRESTORE MAP
    // =========================================================

    private fun productToFirestoreMap(
        product: Product
    ): Map<String, Any> {

        return mapOf(

            "productId" to
                    product.productId,

            "farmerId" to
                    product.farmerId,

            "productName" to
                    product.productName,

            "cropType" to
                    product.cropType,

            "farmLocation" to
                    product.farmLocation,

            "harvestDate" to
                    product.harvestDate,

            "quantity" to
                    product.quantity,

            "quality" to
                    product.quality,

            "cultivationMethod" to
                    product.cultivationMethod,

            "createdAt" to
                    product.createdAt,

            "status" to
                    product.status.name,

            "blockchainStatus" to
                    product.blockchainStatus.name
        )
    }

    // =========================================================
    // PRODUCT -> ROOM ENTITY
    // =========================================================

    private fun productToEntity(
        product: Product
    ): ProductEntity {

        return ProductEntity(

            productId =
                product.productId,

            farmerId =
                product.farmerId,

            productName =
                product.productName,

            cropType =
                product.cropType,

            farmLocation =
                product.farmLocation,

            harvestDate =
                product.harvestDate,

            quantity =
                product.quantity,

            quality =
                product.quality,

            cultivationMethod =
                product.cultivationMethod,

            createdAt =
                product.createdAt,

            status =
                product.status.name,

            blockchainStatus =
                product.blockchainStatus.name
        )
    }

    // =========================================================
    // ROOM ENTITY -> PRODUCT
    // =========================================================

    private fun entityToProduct(
        entity: ProductEntity
    ): Product {

        return Product(

            productId =
                entity.productId,

            farmerId =
                entity.farmerId,

            productName =
                entity.productName,

            cropType =
                entity.cropType,

            farmLocation =
                entity.farmLocation,

            harvestDate =
                entity.harvestDate,

            quantity =
                entity.quantity,

            quality =
                entity.quality,

            cultivationMethod =
                entity.cultivationMethod,

            createdAt =
                entity.createdAt,

            status =
                parseProductStatus(
                    entity.status
                ),

            blockchainStatus =
                parseBlockchainStatus(
                    entity.blockchainStatus
                )
        )
    }

    // =========================================================
    // FIRESTORE DOCUMENT -> PRODUCT
    // =========================================================

    private fun documentToProduct(
        document: DocumentSnapshot
    ): Product? {

        val productId =
            document.getString("productId")
                ?: document.id

        val farmerId =
            document.getString("farmerId")
                ?: ""

        val productName =
            document.getString("productName")
                ?: return null

        val cropType =
            document.getString("cropType")
                ?: ""

        val farmLocation =
            document.getString("farmLocation")
                ?: ""

        val harvestDate =
            document.getString("harvestDate")
                ?: ""

        val quantity =
            document.getString("quantity")
                ?: ""

        val quality =
            document.getString("quality")
                ?: ""

        val cultivationMethod =
            document.getString(
                "cultivationMethod"
            )
                ?: ""

        val createdAt =
            document.getLong("createdAt")
                ?: System.currentTimeMillis()

        val status =
            document.getString("status")
                ?: ProductStatus.CREATED.name

        val blockchainStatus =
            document.getString(
                "blockchainStatus"
            )
                ?: BlockchainStatus.PENDING.name

        return Product(

            productId =
                productId,

            farmerId =
                farmerId,

            productName =
                productName,

            cropType =
                cropType,

            farmLocation =
                farmLocation,

            harvestDate =
                harvestDate,

            quantity =
                quantity,

            quality =
                quality,

            cultivationMethod =
                cultivationMethod,

            createdAt =
                createdAt,

            status =
                parseProductStatus(
                    status
                ),

            blockchainStatus =
                parseBlockchainStatus(
                    blockchainStatus
                )
        )
    }

    // =========================================================
    // PRODUCT STATUS PARSER
    // =========================================================

    private fun parseProductStatus(
        value: String
    ): ProductStatus {

        return try {

            ProductStatus.valueOf(
                value
            )

        } catch (_: IllegalArgumentException) {

            ProductStatus.CREATED
        }
    }

    // =========================================================
    // BLOCKCHAIN STATUS PARSER
    // =========================================================

    private fun parseBlockchainStatus(
        value: String
    ): BlockchainStatus {

        return try {

            BlockchainStatus.valueOf(
                value
            )

        } catch (_: IllegalArgumentException) {

            BlockchainStatus.PENDING
        }
    }
}