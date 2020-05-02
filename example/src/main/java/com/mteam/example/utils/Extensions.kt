package com.mteam.example.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun fromHtml(source: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(source)
    }
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun logWithThreadAndCoroutineInfo(message: String) =
    println("[${Thread.currentThread().name}] $message")

fun addCoroutineDebugInfo(message: String) = "[${Thread.currentThread().name}] $message"

/**
 * From kotlinextensions.com
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun <T> Single<T>.with(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


private const val INITIAL_BACKOFF = 2000L

fun getBackoffDelay(attempt: Long) = INITIAL_BACKOFF * (attempt + 1)


//https://medium.com/androiddevelopers/lessons-learnt-using-coroutines-flow-4a6b285c0d06
fun <T : Any> Flow<Result<T>>.applyCommonSideEffects() =
    retryWhen { cause, attempt ->
        when {
            (cause is IOException && attempt < 3L) -> {
                delay(getBackoffDelay(attempt))
                true
            }
            else -> {
                false
            }
        }
    }
        .onStart { emit(Progress(isLoading = true)) }
        .onCompletion { emit(Progress(isLoading = false)) }

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}

inline fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner, Observer { it?.apply(observer) })
}