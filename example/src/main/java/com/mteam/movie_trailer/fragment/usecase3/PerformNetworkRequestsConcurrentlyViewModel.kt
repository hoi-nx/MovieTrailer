package com.mteam.movie_trailer.fragment.usecase3

import androidx.lifecycle.viewModelScope
import com.mteam.movie_trailer.fragment.BaseViewModel
import com.mteam.movie_trailer.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                // we need to wrap this code with a coroutineScope block
                // otherwise the app would crash on unsuccessful network requests
                coroutineScope {
                    val oreoFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(27) }
                    val pieFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(28) }
                    val android10FeaturesDeferred = async { mockApi.getAndroidVersionFeatures(29) }

                    val oreoFeatures = oreoFeaturesDeferred.await()
                    val pieFeatures = pieFeaturesDeferred.await()
                    val android10Features = android10FeaturesDeferred.await()

                    val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)

                    // other alternative: (but slightly different behavior when a deferred fails, see docs)
                    // val versionFeatures = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)

                    uiState.value = UiState.Success(versionFeatures)
                }

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }
}