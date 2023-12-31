package com.projectbyzakaria.scanner.analyzer.scanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ResultPoint
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer


class QrCodeAnalyzer(
    private val onScan: (String) -> Unit,
    private val detectPaint: (Float, Float) -> Unit,
) : ImageAnalysis.Analyzer {

    var isFindQRCode = false
    private val supportedImageFormat = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun  analyze(image: ImageProxy) {
        if (image.format in supportedImageFormat){
            val bytes = image.planes[0].buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                 bytes,
                image.width,
                image.height,
                0,0,
                image.width,
                image.height,
                false
            )
            val binary = BinaryBitmap(HybridBinarizer(source))
            val result = MultiFormatReader().apply {
                setHints(
                    mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE,BarcodeFormat.CODABAR))
                )
            }
            try {
                val finally = result.decode(binary)
                val qrCodePoints: Array<ResultPoint> = finally.resultPoints
                for (point in qrCodePoints) {
                    val x = point.x
                    val y = point.y
                    Log.d("ssssssssssssssssssss", "QR Code Corner - X: $x, Y: $y")
                    detectPaint(x,y)
                }

                if (image.format ==   ImageFormat.YUV_420_888) {
                    if (!isFindQRCode) {
                            onScan(finally.text)
                            isFindQRCode = true
                    }
                }

            }catch (ex:Exception){
                detectPaint(0f,0f)
                Log.d("gggggggggggggggggg", "analyze: ${ex.message}")
            }finally {
                image.close()
            }
        }else{
            return
        }
    }


    private fun ByteBuffer.toByteArray():ByteArray{
        rewind()
        val data = ByteArray(this.remaining())
        get(data)
        return data
    }



}
