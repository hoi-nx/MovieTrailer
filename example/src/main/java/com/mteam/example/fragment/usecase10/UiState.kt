package com.mteam.example.fragment.usecase10

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val result: String,
        val computationDuration: Long,
        val stringConversionDuration: Long
    ) : UiState()

    data class Error(val message: String) : UiState()
}