package com.mteam.example.fragment.usecase13

import com.mteam.example.mock.VersionFeatures


sealed class UiState {
    object Loading : UiState()
    data class Success(
        val versionFeatures: List<VersionFeatures>
    ) : UiState()

    data class Error(val message: String) : UiState()
}