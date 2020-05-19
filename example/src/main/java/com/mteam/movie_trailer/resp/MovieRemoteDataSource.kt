package com.mteam.movie_trailer.resp

import androidx.lifecycle.LiveData
import com.mteam.movie_trailer.BaseDataSource
import com.mteam.movie_trailer.ResponseResult
import com.mteam.movie_trailer.ResponseWrapper
import com.mteam.movie_trailer.resultLiveData
import kotlinx.coroutines.CoroutineScope

class MovieRemoteDataSource(private val movieApiService: MovieApiService) : BaseDataSource() {

    fun discoverAllMovies(scope: CoroutineScope): LiveData<ResponseResult<ResponseWrapper<MovieResponse>>> = resultLiveData(scope) {
        getResult {
            movieApiService.discoverAllMovies()
        }
    }

    fun getMovieDetails(movieId: Int, scope: CoroutineScope): LiveData<ResponseResult<ResponseWrapper<MovieDetailResponse>>> = resultLiveData(scope) {
        getResult {
            movieApiService.getMovieDetail(movieId)
        }
    }

}