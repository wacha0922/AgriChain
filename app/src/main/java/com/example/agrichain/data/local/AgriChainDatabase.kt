package com.example.agrichain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AgriChainDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {

        const val DATABASE_NAME =
            "agrichain_database"

        /**
         * Version 1 -> Version 2
         *
         * Adds farmerId to the local products table.
         *
         * Existing products receive an empty farmerId.
         * Newly synchronized products will contain the real
         * Firebase UID.
         */
        val MIGRATION_1_2 =
            object : Migration(1, 2) {

                override fun migrate(
                    database: SupportSQLiteDatabase
                ) {

                    database.execSQL(
                        """
                        ALTER TABLE products
                        ADD COLUMN farmerId TEXT NOT NULL DEFAULT ''
                        """.trimIndent()
                    )
                }
            }
    }
}