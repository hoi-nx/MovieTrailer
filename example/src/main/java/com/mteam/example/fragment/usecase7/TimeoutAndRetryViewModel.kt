package com.mteam.example.fragment.usecase7
import androidx.lifecycle.viewModelScope
import com.mteam.example.fragment.BaseViewModel
import com.mteam.example.mock.MockApi
import kotlinx.coroutines.*

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            val numberOfRetries = 2
            val timeout = 1000L

            try {
                val oreoVersionsDeferred = async {
                    retryWithTimeout(numberOfRetries, timeout) {
                        api.getAndroidVersionFeatures(27)
                    }
                }

                val pieVersionsDeferred = async {
                    retryWithTimeout(numberOfRetries, timeout) {
                        api.getAndroidVersionFeatures(28)
                    }
                }

                val versionFeatures = listOf(
                    oreoVersionsDeferred,
                    pieVersionsDeferred
                ).awaitAll()

                uiState.value = UiState.Success(versionFeatures)

            } catch (e: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    private suspend fun <T> retryWithTimeout(
        numberOfRetries: Int,
        timeout: Long,
        block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: Exception) {
            }
            delay(delayBetweenRetries)
        }
        return block() // last attempt
    }
}