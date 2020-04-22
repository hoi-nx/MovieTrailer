package com.mteam.example

import android.app.Application

class WatchlistApp : Application() {
  val watchlistRepository by lazy {
    WatchlistRepository()
  }
}