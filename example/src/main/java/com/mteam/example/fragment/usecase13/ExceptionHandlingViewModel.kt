package com.mteam.example.fragment.usecase13

import androidx.lifecycle.viewModelScope
import com.mteam.example.fragment.BaseViewModel
import com.mteam.example.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(27)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed: $exception")
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Network Request failed!! $exception")
        }

        uiState.value = UiState.Loading
        viewModelScope.launch(exceptionHandler) {
            api.getAndroidVersionFeatures(27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        uiState.value = UiState.Loading
        viewModelScope.launch {

            supervisorScope {
                val oreoFeaturesDeferred = async { api.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { api.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { api.getAndroidVersionFeatures(29) }

                val oreoFeatures = try {
                    oreoFeaturesDeferred.await()
                } catch (e: Exception) {
                    null
                }

                val pieFeatures = try {
                    pieFeaturesDeferred.await()
                } catch (e: Exception) {
                    null
                }

                val android10Features = try {
                    android10FeaturesDeferred.await()
                } catch (e: Exception) {
                    null
                }
                val versionFeatures = listOfNotNull(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            }
        }
    }

    fun showResultsEvenIfChildCoroutineFailsRunCatching() {
        uiState.value = UiState.Loading
        viewModelScope.launch {

            supervisorScope {
                val oreoFeaturesDeferred = async { api.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { api.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { api.getAndroidVersionFeatures(29) }

                val versionFeatures = listOf(
                    oreoFeaturesDeferred,
                    pieFeaturesDeferred,
                    android10FeaturesDeferred
                ).mapNotNull { deferred ->
                    runCatching {
                        deferred.await()
                    }.onFailure {
                    }.getOrNull()
                }

                uiState.value = UiState.Success(versionFeatures)
            }
        }
    }
}