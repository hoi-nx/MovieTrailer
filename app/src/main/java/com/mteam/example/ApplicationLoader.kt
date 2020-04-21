package com.mteam.example

import android.app.Application

class ApplicationLoader : Application() {

    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("api_keys")
    }

    override fun onCreate() {
        super.onCreate()

    }
}