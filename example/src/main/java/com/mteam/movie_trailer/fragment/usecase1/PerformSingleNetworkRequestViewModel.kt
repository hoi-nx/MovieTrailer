package com.mteam.movie_trailer.fragment.usecase1

import androidx.lifecycle.viewModelScope
import com.mteam.movie_trailer.fragment.BaseViewModel
import com.mteam.movie_trailer.mock.MockApi
import kotlinx.coroutines.launch

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed!")
            }
        }
    }
}