package com.mteam.movie_trailer.fragment.usecase6

import com.mteam.movie_trailer.mock.AndroidVersion


sealed class UiState {
    object Loading : UiState()
    data class Success(val recentVersions: List<AndroidVersion>) : UiState()
    data class Error(val message: String) : UiState()
}