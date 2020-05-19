package com.mteam.movie_trailer.storage

interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String
}
