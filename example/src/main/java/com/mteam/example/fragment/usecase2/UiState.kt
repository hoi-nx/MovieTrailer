package com.mteam.example.fragment.usecase2

import com.mteam.example.mock.VersionFeatures


sealed class UiState {
    object Loading : UiState()
    data class Success(
        val versionFeatures: VersionFeatures
    ) : UiState()

    data class Error(val message: String) : UiState()
}