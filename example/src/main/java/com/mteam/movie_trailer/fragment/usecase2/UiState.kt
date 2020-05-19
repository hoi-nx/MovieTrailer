package com.mteam.movie_trailer.fragment.usecase2

import com.mteam.movie_trailer.mock.VersionFeatures


sealed class UiState {
    object Loading : UiState()
    data class Success(
        val versionFeatures: VersionFeatures
    ) : UiState()

    data class Error(val message: String) : UiState()
}