package com.example.agrichain.data.qr

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.util.EnumMap

object QrCodeGenerator {

    fun generate(
        content: String,
        size: Int = 800
    ): Bitmap {

        require(content.isNotBlank()) {
            "QR content cannot be empty."
        }

        require(size > 0) {
            "QR size must be greater than zero."
        }

        val hints = EnumMap<EncodeHintType, Any>(
            EncodeHintType::class.java
        ).apply {
            put(
                EncodeHintType.MARGIN,
                1
            )

            put(
                EncodeHintType.CHARACTER_SET,
                "UTF-8"
            )
        }

        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
            )

        val bitmap = Bitmap.createBitmap(
            size,
            size,
            Bitmap.Config.ARGB_8888
        )

        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    }
                )
            }
        }

        return bitmap
    }
}