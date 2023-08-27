package com.projectbyzakaria.scanner.ui.presentassions

import com.projectbyzakaria.scanner.data.DataBase
import com.projectbyzakaria.scanner.data.ScannerEntity
import com.projectbyzakaria.scanner.model.ScanningResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataBase:DataBase
) {
    val dao = dataBase.getScannerDao()
    suspend fun insert(scanningResult: ScanningResult){
        dao.insertScanner(scanningResult.toEntity())
    }

    suspend fun delete(scanningResult: ScanningResult){
        dao.deleteScanner(scanningResult.toEntity())
    }


    fun getAll() :Flow<List<ScanningResult>> = dao.getListOfScanner().map { it.map { it.toScannerResultModel() } }

    private fun ScanningResult.toEntity():ScannerEntity{
        return ScannerEntity(text,image)
    }
    private fun ScannerEntity.toScannerResultModel():ScanningResult{
        return ScanningResult(name,image)
    }

}