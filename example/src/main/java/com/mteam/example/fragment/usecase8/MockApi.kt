package com.mteam.example.fragment.usecase8
import com.google.gson.Gson
import com.mteam.example.mock.createMockApi
import com.mteam.example.mock.mockAndroidVersions
import com.mteam.example.utils.MockNetworkInterceptor

fun mockApi() =
    createMockApi(
        MockNetworkInterceptor()
            .mock(
                "http://localhost/recent-android-versions",
                Gson().toJson(mockAndroidVersions),
                200,
                5000
            )
    )