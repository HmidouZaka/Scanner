package com.projectbyzakaria.scanner.data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "scanner")
data class ScannerEntity(
    @ColumnInfo(name = "name")
    val name : String,
    @ColumnInfo(name = "image")
    val image: Bitmap
){
    @PrimaryKey(true)
    var id:Int = 1
}