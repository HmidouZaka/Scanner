package com.projectbyzakaria.scanner.ui.presentassions


import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectbyzakaria.scanner.analyzer.scanner.Generator
import com.projectbyzakaria.scanner.model.ScanningResult
import com.projectbyzakaria.scanner.utils.ScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _scanningState =
        MutableStateFlow<ScanResult<ScanningResult>>(ScanResult.Scanning<ScanningResult>())
    val scanningState = _scanningState.asStateFlow()

    private val _itemScanner = MutableStateFlow<ScanResult<ScanningResult>>(ScanResult.Scanning<ScanningResult>())
    val itemScanner = _itemScanner.asStateFlow()

    val scanners = repository.getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun showResult(text: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _scanningState.emit(ScanResult.Loading())
            val generator = Generator(text)
            val bitmapImage = generator.generate()
            delay(1000)
            if (bitmapImage == null) {
                _scanningState.emit(ScanResult.Error("generating is field pleas try again"))
            } else {
                _scanningState.emit(ScanResult.Success(ScanningResult(text, bitmapImage,0)))
            }
        }
    }

    fun retry() {
        viewModelScope.launch(Dispatchers.Main) {
            _scanningState.emit(ScanResult.Scanning())
        }

    }



    fun insertScanner(scanner:ScanningResult){
        viewModelScope.launch(Dispatchers.Main) {
            repository.insert(scanner)
        }
    }
    fun deleteScanner(scanner:ScanningResult){
        viewModelScope.launch(Dispatchers.Main) {
            repository.delete(scanner.id)
        }
    }

    fun findItem(index: Int) {
        viewModelScope.launch {
            _itemScanner.emit(ScanResult.Loading())
            val item =  scanners.first().get(index)
            _itemScanner.emit(ScanResult.Success(item))
        }
    }



    private val _textGenerator = MutableStateFlow<String>("")
    val textGenerator = _textGenerator.asStateFlow()

    private val _resultImage = MutableStateFlow<Bitmap?>(null)
    val resultImage = _resultImage.asStateFlow()


    fun setTextGenerator(text:String){
        viewModelScope.launch {
            _textGenerator.emit(text)
            if (text.isEmpty()){
                _resultImage.emit(null)
            }else{
                val generator = Generator(text)
                val image = generator.generate()
                _resultImage.emit(image)
            }
        }
    }


    fun reset(){
        viewModelScope.launch {
            _resultImage.emit(null)
            _textGenerator.emit("")
        }
    }

}