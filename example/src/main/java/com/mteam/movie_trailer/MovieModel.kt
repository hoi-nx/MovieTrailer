package com.mteam.movie_trailer

data class MovieModel(
    val id: Long,
    val name: String,
    val posterLink: String,
    val isWatchlisted: Boolean
)