package com.mteam.movietrailer

object NativeKey {
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun getAuthenKey(): String
    external fun getAuthenKeyTrakt(): Array<String>

    val static: Int = 3

    @JvmStatic
    fun staticMethod(int: Int) = int
}