package com.mteam.movie_trailer.fragment.usecase6

import androidx.lifecycle.viewModelScope
import com.mteam.movie_trailer.fragment.BaseViewModel
import com.mteam.movie_trailer.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            val numberOfRetries = 2
            try {
                retry(times = numberOfRetries) {
                    val recentVersions = api.getRecentAndroidVersions()
                    uiState.value = UiState.Success(recentVersions)
                }
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    // retry with exponential backoff
    // inspired by https://stackoverflow.com/questions/46872242/how-to-exponential-backoff-retry-on-kotlin-coroutines
    private suspend fun <T> retry(
        times: Int,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(times) {
            try {
                return block()
            } catch (exception: Exception) {
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
        return block() // last attempt
    }
}