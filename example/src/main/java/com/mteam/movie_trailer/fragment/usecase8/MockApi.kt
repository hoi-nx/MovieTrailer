package com.mteam.movie_trailer.fragment.usecase8
import com.google.gson.Gson
import com.mteam.movie_trailer.mock.createMockApi
import com.mteam.movie_trailer.mock.mockAndroidVersions
import com.mteam.movie_trailer.utils.MockNetworkInterceptor

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