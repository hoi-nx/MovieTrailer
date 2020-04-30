package com.mteam.example.fragment.usecase5

import com.mteam.example.utils.MockNetworkInterceptor
import com.google.gson.Gson
import com.mteam.example.mock.createMockApi
import com.mteam.example.mock.mockAndroidVersions


fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            Gson().toJson(mockAndroidVersions),
            200,
            1000
        )
)