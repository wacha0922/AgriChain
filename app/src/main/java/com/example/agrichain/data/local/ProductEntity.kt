package com.example.agrichain.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "products"
)
data class ProductEntity(

    @PrimaryKey
    val productId: String,

    val productName: String,

    val cropType: String,

    val farmLocation: String,

    val harvestDate: String,

    val quantity: String,

    val quality: String,

    val cultivationMethod: String,

    val createdAt: Long,

    val status: String,

    val blockchainStatus: String
)