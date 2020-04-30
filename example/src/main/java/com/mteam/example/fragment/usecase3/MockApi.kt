package com.mteam.example.fragment.usecase3
import com.google.gson.Gson
import com.mteam.example.mock.createMockApi
import com.mteam.example.mock.mockVersionFeaturesAndroid10
import com.mteam.example.mock.mockVersionFeaturesOreo
import com.mteam.example.mock.mockVersionFeaturesPie
import com.mteam.example.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            1000
        )
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            1000
        )
        .mock(
            "http://localhost/android-version-features/29",
            Gson().toJson(mockVersionFeaturesAndroid10),
            200,
            1000
        )
)