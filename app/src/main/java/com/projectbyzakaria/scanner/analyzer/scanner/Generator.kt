package com.projectbyzakaria.scanner.analyzer.scanner

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class Generator (val target:String) {


    fun generate(width:Int = 1000,height:Int = 1000): Bitmap?{
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(target, BarcodeFormat.QR_CODE,width,height);
            val barcodeEncoder =  BarcodeEncoder();
            val bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

}