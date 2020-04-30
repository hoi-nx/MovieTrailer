package com.mteam.example.resp

import androidx.lifecycle.LiveData
import com.mteam.example.BaseDataSource
import com.mteam.example.ResponseResult
import com.mteam.example.ResponseWrapper
import com.mteam.example.resultLiveData
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