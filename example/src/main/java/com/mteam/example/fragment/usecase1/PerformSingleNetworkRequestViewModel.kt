package com.mteam.example.fragment.usecase1

import androidx.lifecycle.viewModelScope
import com.mteam.example.fragment.BaseViewModel
import com.mteam.example.mock.MockApi
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