package com.mteam.movie_trailer.fragment.usecase6
import com.google.gson.Gson
import com.mteam.movie_trailer.mock.createMockApi
import com.mteam.movie_trailer.mock.mockAndroidVersions
import com.mteam.movie_trailer.utils.MockNetworkInterceptor

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