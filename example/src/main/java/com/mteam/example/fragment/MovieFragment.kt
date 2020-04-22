package com.mteam.example.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import com.mteam.example.MovieAdapter
import com.mteam.example.R
import com.mteam.example.WatchlistViewModel
import kotlinx.android.synthetic.main.fragment_all_movies.*
/**
 * MvRx, pronounced “Mavericks,” is an Android framework from Airbnb.
 * It makes it easy to build simple and complex screens for your apps. It’s based on Model-View-ViewModel architecture
 * MvRx has four main concepts:
 * 1. State : là một lớp dữ liệu Kotlin bất biến có chứa tất cả các thuộc tính cần thiết để thể hiện màn hình của bạn.
 * Nói cách khác, nó đại diện cho trạng thái của ứng dụng.
 * Nó cần phải bất biến để bạn có thể truy cập nó một cách an toàn từ các luồng khác nhau.
 * Cách duy nhất để sửa đổi trạng thái là sử dụng toán tử copy () trên nó. Mỗi lớp trạng thái nên thực hiện MvRxState.
 * 2.ViewModel : MvRx uses a class called MvRxViewModel that extends Google’s ViewModel class.
 * Sự khác biệt chính là MvRxViewModel phụ thuộc vào một thể hiện MvRxState bất biến duy nhất.
 * Nó không dựa vào LiveData để thông báo thay đổi. MvRxViewModel chứa các phương thức có thể sửa đổi trạng thái;
 * View chỉ có thể sử dụng các chức năng này để sửa đổi trạng thái.
 * 3. View : MvRxView has a method called invalidate() that the view must implement. Whenever any property of the state changes, it calls invalidate() and updates the view.
 * 4. Async: Async is a wrapper class that makes it easy to deal with asynchronous sources.
 * Async allows your state’s properties to exist in different states, depending on the ViewModelState.
 * Async has four types: 1: Uninitialized, 2: Loading, 3: Success, 4 :Fail
 * Thank for example of By Subhrajyoti Sen : https://www.raywenderlich.com/8121045-mvrx-android-on-autopilot-getting-started
 */
class MovieFragment : BaseMvRxFragment() {
    private lateinit var movieAdapter: MovieAdapter

    // add ViewModel declaration here
    private val watchlistViewModel: WatchlistViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter(object : MovieAdapter.WatchlistListener {
            override fun addToWatchlist(movieId: Long) {
                // call ViewModel to add movie to watchlist
                watchlistViewModel.watchlistMovie(movieId)
            }

            override fun removeFromWatchlist(movieId: Long) {
                // call ViewModel to remove movie from watchlist
                watchlistViewModel.removeMovieFromWatchlist(movieId)
            }
        })
        all_movies_recyclerview.adapter = movieAdapter
        watchlistViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun invalidate() {
        withState(watchlistViewModel) { state ->
            when (state.movies) {
                is Loading -> {
                    progress_bar.visibility = View.VISIBLE
                    all_movies_recyclerview.visibility = View.GONE
                }
                is Success -> {
                    progress_bar.visibility = View.GONE
                    all_movies_recyclerview.visibility = View.VISIBLE
                    movieAdapter.setMovies(state.movies.invoke())
                }
                is Fail -> {
                    Toast.makeText(requireContext(), "Failed to load all movies", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.watchlist, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.watchlist) {
            findNavController().navigate(R.id.action_movieFragment_to_watchlistFragment)

        }
        return super.onOptionsItemSelected(item)
    }

}