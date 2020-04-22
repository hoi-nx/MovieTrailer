package com.mteam.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.airbnb.mvrx.*
import com.mteam.example.MovieAdapter
import com.mteam.example.MovieModel
import com.mteam.example.R
import com.mteam.example.WatchlistViewModel
import kotlinx.android.synthetic.main.fragment_watchlist.*


class WatchlistFragment : BaseMvRxFragment() {

  private lateinit var movieAdapter: MovieAdapter

  // add ViewModel declaration here
  private val watchlistViewModel: WatchlistViewModel by activityViewModel()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_watchlist, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    movieAdapter = MovieAdapter(object :
      MovieAdapter.WatchlistListener {
      override fun addToWatchlist(movieId: Long) {
        // call ViewModel to add movie to watchlist
        watchlistViewModel.watchlistMovie(movieId)
      }

      override fun removeFromWatchlist(movieId: Long) {
        // call ViewModel to remove movie from watchlist
        watchlistViewModel.removeMovieFromWatchlist(movieId)
      }
    })
    watchlist_movies_recyclerview.adapter = movieAdapter
    watchlistViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
      Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    })
  }

  override fun invalidate() {
    // modify UI
    withState(watchlistViewModel) { state ->
      when (state.movies) {
        is Loading -> {
          showLoader()
        }
        is Success -> {
          val watchlistedMovies = state.movies.invoke().filter { it.isWatchlisted }
          showWatchlistedMovies(watchlistedMovies)
        }
        is Fail -> {
          showError()
        }
      }
    }
  }

  private fun showLoader() {
    progress_bar.visibility = View.VISIBLE
    empty_watchlist_textview.visibility = View.GONE
    watchlist_movies_recyclerview.visibility = View.GONE
  }

  private fun showWatchlistedMovies(watchlistedMovies: List<MovieModel>) {
    if (watchlistedMovies.isEmpty()) {
      progress_bar.visibility = View.GONE
      empty_watchlist_textview.visibility = View.VISIBLE
      watchlist_movies_recyclerview.visibility = View.GONE
    } else {
      progress_bar.visibility = View.GONE
      empty_watchlist_textview.visibility = View.GONE
      watchlist_movies_recyclerview.visibility = View.VISIBLE

      movieAdapter.setMovies(watchlistedMovies)
    }
  }

  private fun showError() {
    Toast.makeText(requireContext(), "Failed to load watchlist", Toast.LENGTH_SHORT).show()
  }
}