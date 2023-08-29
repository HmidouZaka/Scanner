package com.projectbyzakaria.scanner.analyzer.image_qr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

class ImageScanner(
    val context:Context,
    val uri:Uri
) {


    fun scan() : String?{
        val contentResolver = context.contentResolver
        val openStream = contentResolver.openInputStream(uri)
        var bitmap: Bitmap? = BitmapFactory.decodeStream(openStream)
        val width = bitmap?.width ?: 0
        val height = bitmap?.height ?: 0
        val pixels = IntArray(width * height)
        bitmap?.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap?.recycle()
        bitmap = null
        val source = RGBLuminanceSource(width, height, pixels)
        val bBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        return try {
            val decode = reader.decode(bBitmap)
            decode.text
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "${ex.message}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }


}