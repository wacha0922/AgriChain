package com.example.agrichain.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    // ---------------------------------------------------------
    // Insert / Update
    // ---------------------------------------------------------

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertProduct(
        product: ProductEntity
    )

    // ---------------------------------------------------------
    // Get all cached products
    // ---------------------------------------------------------

    @Query(
        "SELECT * FROM products ORDER BY createdAt DESC"
    )
    suspend fun getAllProducts(): List<ProductEntity>

    // ---------------------------------------------------------
    // Get products created by a specific farmer
    // ---------------------------------------------------------

    @Query(
        """
        SELECT * FROM products
        WHERE farmerId = :farmerId
        ORDER BY createdAt DESC
        """
    )
    suspend fun getProductsByFarmerId(
        farmerId: String
    ): List<ProductEntity>

    // ---------------------------------------------------------
    // Get one product
    // ---------------------------------------------------------

    @Query(
        """
        SELECT * FROM products
        WHERE productId = :productId
        LIMIT 1
        """
    )
    suspend fun getProductById(
        productId: String
    ): ProductEntity?

    // ---------------------------------------------------------
    // Delete one product
    // ---------------------------------------------------------

    @Delete
    suspend fun deleteProduct(
        product: ProductEntity
    )

    // ---------------------------------------------------------
    // Delete all local products
    // ---------------------------------------------------------

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}