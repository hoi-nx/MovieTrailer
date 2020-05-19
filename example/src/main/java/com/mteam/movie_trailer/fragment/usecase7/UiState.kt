package com.mteam.movie_trailer.fragment.usecase7

import com.mteam.movie_trailer.mock.VersionFeatures

sealed class UiState {
    object Loading : UiState()
    data class Success(val versionFeatures: List<VersionFeatures>) : UiState()
    data class Error(val message: String) : UiState()
}