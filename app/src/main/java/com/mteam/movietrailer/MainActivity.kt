package com.mteam.movietrailer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mteam.movietrailer.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sample_text.text = NativeKey.getAuthenKeyTrakt().get(0)
    }


}