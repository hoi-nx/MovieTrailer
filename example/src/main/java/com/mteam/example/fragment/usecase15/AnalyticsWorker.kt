package com.mteam.example.fragment.usecase15

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mteam.example.mock.createMockAnalyticsApi
import com.mteam.example.utils.MockNetworkInterceptor

class AnalyticsWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    private val analyticsApi = createMockAnalyticsApi()

    override suspend fun doWork(): Result {
        return try {
            analyticsApi.trackScreenOpened()
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    companion object {
        fun createMockAnalyticsApi() = createMockAnalyticsApi(
            MockNetworkInterceptor()
                .mock(
                    "http://localhost/analytics/workmanager-screen-opened",
                    "true",
                    200,
                    1500
                )
        )
    }
}