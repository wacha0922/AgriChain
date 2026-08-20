package com.example.agrichain.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query(
        "SELECT * FROM products WHERE productId = :productId LIMIT 1"
    )
    suspend fun getProductById(
        productId: String
    ): ProductEntity?

    @Delete
    suspend fun deleteProduct(
        product: ProductEntity
    )

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}