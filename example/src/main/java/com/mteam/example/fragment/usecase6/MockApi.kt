package com.mteam.example.fragment.usecase6
import com.google.gson.Gson
import com.mteam.example.mock.createMockApi
import com.mteam.example.mock.mockAndroidVersions
import com.mteam.example.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            "something went wrong on server side",
            500,
            1000,
            persist = false
        ).mock(
            "http://localhost/recent-android-versions",
            "something went wrong on server side",
            500,
            1000,
            persist = false
        ).mock(
            "http://localhost/recent-android-versions",
            Gson().toJson(mockAndroidVersions),
            200,
            1000
        )
)