package com.mteam.example
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * https://github.com/herisulistiyanto/Simple-Retrofit-Coroutine
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResponseResult<ResponseWrapper<T>> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (null != body) return ResponseResult.Success(ResponseWrapper(body, null))
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(msg: String): ResponseResult<ResponseWrapper<T>> {
        return ResponseResult.Error(ResponseWrapper(null, msg))
    }

}

fun <T> resultLiveData(scope: CoroutineScope, call: suspend () -> ResponseResult<T>): LiveData<ResponseResult<T>> {
    return liveData(scope.coroutineContext) {
        emit(ResponseResult.Loading)

        withContext(Dispatchers.IO) {
            emit(call.invoke())
        }
    }
}