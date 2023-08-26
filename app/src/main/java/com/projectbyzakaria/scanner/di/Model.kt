package com.projectbyzakaria.scanner.di

import android.content.Context
import androidx.room.Room
import com.projectbyzakaria.scanner.data.DataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Model {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context = context,
            klass = DataBase::class.java,
            name = "scanner_db.db"
        ).build()
    }


}