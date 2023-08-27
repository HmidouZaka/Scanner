package com.projectbyzakaria.scanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ScannerEntity::class], version = 2)
@TypeConverters(value = [ImageConvertor::class])
abstract class DataBase : RoomDatabase() {
    abstract fun getScannerDao():ScannerDao
}