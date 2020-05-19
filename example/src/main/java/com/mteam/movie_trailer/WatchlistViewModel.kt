package com.mteam.movie_trailer

import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*

class WatchlistViewModel(
    initialState: WatchlistState,
    private val watchlistRepository: WatchlistRepository
) : BaseMvRxViewModel<WatchlistState>(initialState, debugMode = true) {

  val errorMessage = MutableLiveData<String>()

  init {
    setState {
      copy(movies = Loading())
    }
    watchlistRepository.getWatchlistedMovies()
        .execute {
          copy(movies = it)
        }
  }

  fun watchlistMovie(movieId: Long) {
    withState { state ->
      if (state.movies is Success) {
        val index = state.movies.invoke().indexOfFirst {
          it.id == movieId
        }
        watchlistRepository.watchlistMovie(movieId)
            .execute {
              if (it is Success) {
                copy(
                    movies = Success(
                        state.movies.invoke().toMutableList().apply {
                          set(index, it.invoke())
                        }
                    )
                )
              } else if (it is Fail){
                errorMessage.postValue("Failed to add movie to watchlist")
                copy()
              } else {
                copy()
              }
            }
      }
    }
  }

  fun removeMovieFromWatchlist(movieId: Long) {
    withState { state ->
      if (state.movies is Success) {
        val index = state.movies.invoke().indexOfFirst {
          it.id == movieId
        }
        watchlistRepository.removeMovieFromWatchlist(movieId)
            .execute {
              if (it is Success) {
                copy(
                    movies = Success(
                        state.movies.invoke().toMutableList().apply {
                          set(index, it.invoke())
                        }
                    )
                )
              } else if (it is Fail){
                errorMessage.postValue("Failed to remove movie from watchlist")
                copy()
              } else {
                copy()
              }
            }
      }
    }
  }

  companion object : MvRxViewModelFactory<WatchlistViewModel, WatchlistState> {
    override fun create(viewModelContext: ViewModelContext,
        state: WatchlistState): WatchlistViewModel? {
      val watchlistRepository = viewModelContext.app<WatchlistApp>().watchlistRepository
      return WatchlistViewModel(state, watchlistRepository)
    }
  }
}