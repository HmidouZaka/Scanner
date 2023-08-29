package com.projectbyzakaria.scanner.model

import android.graphics.Bitmap

data class ScanningResult (
    val text:String,
    val image:Bitmap,
    var id:Int
)