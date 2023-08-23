package com.projectbyzakaria.scanner.analyzer.scanner

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QrCodeAnalyzer(
    private val onScan:(String)->Unit
) : ImageAnalysis.Analyzer {

    private val supportedImageFormat = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormat){
            val bytes = image.planes.first().buffer.toByteArray()
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
            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE))
                    )
                }.decode(binary)
                onScan(result.text)
            }catch (ex:Exception){

            }finally {
                image.close()
            }
        }else{
            return
        }
    }


    private fun ByteBuffer.toByteArray():ByteArray{
        rewind()
        val byteArray = ByteArray(this.remaining()).also {
            get(it)
        }
        return byteArray
    }
}