package com.mteam.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun <T> LiveData<T>.onResult(action: (T) -> Unit) {
        observe(this@MainActivity) { data ->
            data?.let(action)
        }
    }
}