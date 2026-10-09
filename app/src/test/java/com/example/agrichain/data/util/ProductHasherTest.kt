package com.example.agrichain.data.util

import com.example.agrichain.data.model.BlockchainStatus
import com.example.agrichain.data.model.Product
import com.example.agrichain.data.model.ProductStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductHasherTest {

    private fun createTestProduct(): Product {
        return Product(
            productId = "AGRITEST-001",
            farmerId = "test-farmer-001",
            productName = "Organic Tomatoes",
            cropType = "Vegetables",
            farmLocation = "Thane, Maharashtra",
            harvestDate = "01 Oct 2026",
            quantity = "100 kg",
            quality = "Grade A",
            cultivationMethod = "Organic",
            createdAt = 1788502495946L,
            status = ProductStatus.CREATED,
            blockchainStatus = BlockchainStatus.PENDING
        )
    }

    @Test
    fun sameProductProducesSameHash() {
        val firstHash = ProductHasher.sha256(createTestProduct())
        val secondHash = ProductHasher.sha256(createTestProduct())

        assertEquals(firstHash, secondHash)
    }

    @Test
    fun hashHasCorrectBytes32Format() {
        val hash = ProductHasher.sha256(createTestProduct())

        assertTrue(
            "Expected 0x followed by 64 hexadecimal characters",
            hash.matches(Regex("^0x[0-9a-f]{64}$"))
        )
    }

    @Test
    fun changingProductNameChangesHash() {
        val original = createTestProduct()
        val modified = original.copy(
            productName = "Modified Tomatoes"
        )

        assertNotEquals(
            ProductHasher.sha256(original),
            ProductHasher.sha256(modified)
        )
    }

    @Test
    fun changingJourneyStatusDoesNotChangeHash() {
        val original = createTestProduct()

        val updated = original.copy(
            status = ProductStatus.SOLD,
            blockchainStatus = BlockchainStatus.CONFIRMED
        )

        assertEquals(
            ProductHasher.sha256(original),
            ProductHasher.sha256(updated)
        )
    }
}
