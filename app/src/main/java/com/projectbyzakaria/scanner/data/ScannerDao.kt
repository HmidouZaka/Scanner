package com.projectbyzakaria.scanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ScannerDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanner(scannerEntity: ScannerEntity)


    @Query("DELETE FROM scanner WHERE id =:id")
    suspend fun deleteScanner(id:Int)
    

    @Query("SELECT * FROM scanner ORDER BY id DESC")
    fun getListOfScanner() : Flow<List<ScannerEntity>>
}