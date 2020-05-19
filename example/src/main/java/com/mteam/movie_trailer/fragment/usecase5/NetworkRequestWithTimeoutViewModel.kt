package com.mteam.movie_trailer.fragment.usecase5

import androidx.lifecycle.viewModelScope
import com.mteam.movie_trailer.fragment.BaseViewModel
import com.mteam.movie_trailer.mock.MockApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var calculationJob: Job? = null

    fun performNetworkRequest(timeOut: Long) {
        uiState.value = UiState.Loading
        calculationJob = viewModelScope.launch {
            try {
                withTimeout(timeOut) {
                    val recentVersions = api.getRecentAndroidVersions()
                    uiState.value = UiState.Success(recentVersions)
                }
            } catch (timeoutCancellationException: TimeoutCancellationException) {
                uiState.value = UiState.Error("Network Request timed out!")
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed!")
            }
        }
    }

    fun cancelCalculation() {
        calculationJob?.cancel()
    }
}