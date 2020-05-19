package com.mteam.movie_trailer

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.mteam.movie_trailer.di.AppComponent
import com.mteam.movie_trailer.di.DaggerAppComponent
import com.mteam.movie_trailer.fragment.usecase14.AndroidVersionDatabase
import com.mteam.movie_trailer.fragment.usecase14.AndroidVersionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WatchlistApp : Application() {
    val watchlistRepository by lazy {
        WatchlistRepository()
    }

    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }

    private val applicationScope = CoroutineScope(SupervisorJob())

    val androidVersionRepository by lazy {
        val database = AndroidVersionDatabase.getInstance(applicationContext).androidVersionDao()
        AndroidVersionRepository(
            database,
            applicationScope
        )
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: WatchlistApp
            private set
        val applicationContext: Context?
            get() = if (::INSTANCE.isInitialized) INSTANCE.applicationContext else null    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AndroidUtilities.checkDisplaySize(this, newConfig)
    }

}