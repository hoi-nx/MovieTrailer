package com.mteam.example.fragment.usecase2
import com.google.gson.Gson
import com.mteam.example.mock.createMockApi
import com.mteam.example.mock.mockAndroidVersions
import com.mteam.example.mock.mockVersionFeaturesAndroid10
import com.mteam.example.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            Gson().toJson(mockAndroidVersions),
            200,
            1500
        )
        .mock(
            "http://localhost/android-version-features/29",
            Gson().toJson(mockVersionFeaturesAndroid10),
            200,
            1500
        )
)