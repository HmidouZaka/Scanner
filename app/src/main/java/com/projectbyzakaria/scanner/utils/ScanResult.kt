package com.projectbyzakaria.scanner.utils

sealed class ScanResult<T>(
    val data: T? = null,
    val message: String? = null,
) {
    data class Scanning<T>(val any: Any? = null) : ScanResult<T>(null,null)
    data class Success<T>(val result: T) : ScanResult<T>(result,null)
    data class Loading<T>(val any: Any? = null) : ScanResult<T>(null,null)
    data class Error<T>(val error: String) : ScanResult<T>(null,error)
}
