package com.mteam.example.resp

import com.mteam.example.resp.MovieDetailResponse
import com.mteam.example.resp.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {

    @GET("/3/discover/movie")
    suspend fun discoverAllMovies(): Response<MovieResponse>

    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: Int): Response<MovieDetailResponse>
}