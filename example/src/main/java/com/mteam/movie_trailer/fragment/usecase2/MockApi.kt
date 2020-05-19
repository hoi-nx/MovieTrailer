package com.mteam.movie_trailer.fragment.usecase2
import com.google.gson.Gson
import com.mteam.movie_trailer.mock.createMockApi
import com.mteam.movie_trailer.mock.mockAndroidVersions
import com.mteam.movie_trailer.mock.mockVersionFeaturesAndroid10
import com.mteam.movie_trailer.utils.MockNetworkInterceptor

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