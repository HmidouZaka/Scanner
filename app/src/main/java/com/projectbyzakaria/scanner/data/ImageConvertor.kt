package com.projectbyzakaria.scanner.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream


class ImageConvertor {

    @TypeConverter
    fun toBitmapImage(image:ByteArray):Bitmap{
        return BitmapFactory.decodeByteArray(image,0,image.size)
    }


    @TypeConverter
    fun toByteImageImage(image:Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG,100,stream)
        image.recycle()
        return stream.toByteArray()
    }
}