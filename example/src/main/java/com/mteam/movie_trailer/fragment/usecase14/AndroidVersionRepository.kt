package com.mteam.movie_trailer.fragment.usecase14

import com.mteam.movie_trailer.mock.AndroidVersion
import com.mteam.movie_trailer.mock.MockApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
class AndroidVersionRepository(
    private var database: AndroidVersionDao,
    private val scope: CoroutineScope,
    private val api: MockApi = mockApi()
) {

    suspend fun getLocalAndroidVersions(): List<AndroidVersion> {
        return database.getAndroidVersions().mapToUiModelList()
    }

    suspend fun loadAndStoreRemoteAndroidVersions(): List<AndroidVersion> {
        return scope.async {
            val recentVersions = api.getRecentAndroidVersions()
                for (recentVersion in recentVersions) {
                    database.insert(recentVersion.mapToEntity())
                }
                recentVersions
            }.await()
        }

    fun clearDatabase() {
        scope.launch {
            database.clear()
        }
    }
}