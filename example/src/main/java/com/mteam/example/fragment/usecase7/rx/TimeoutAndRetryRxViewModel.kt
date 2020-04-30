package com.mteam.example.fragment.usecase7.rx
import com.mteam.example.fragment.BaseViewModel
import com.mteam.example.mock.VersionFeatures
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimeoutAndRetryRxViewModel(
    private val api: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposables = CompositeDisposable()

    fun performNetworkRequest() {

        uiState.value = UiState.Loading

        val timeout = 1000L
        val numberOfRetries = 2

        Single.zip(
            api.getAndroidVersionFeatures(27)
                .timeout(timeout, TimeUnit.MILLISECONDS)
                .retry { x, e ->
                    x <= numberOfRetries
                },
            api.getAndroidVersionFeatures(28)
                .timeout(timeout, TimeUnit.MILLISECONDS)
                .retry { x, e ->
                    x <= numberOfRetries
                },
            BiFunction<VersionFeatures, VersionFeatures, List<VersionFeatures>> { versionFeaturesOreo, versionFeaturesPie ->
                listOf(versionFeaturesOreo, versionFeaturesPie)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { versionFeatures ->
                    uiState.value = UiState.Success(versionFeatures)
                },
                onError = { error ->
                    uiState.value = UiState.Error("Network Request failed")
                })
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}