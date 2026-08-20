package com.example.agrichain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AgriChainDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        const val DATABASE_NAME = "agrichain_database"
    }
}