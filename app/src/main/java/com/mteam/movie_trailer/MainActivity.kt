package com.mteam.movie_trailer

import android.os.Bundle
import com.mteam.base_ui.ui.BaseActivity
import com.mteam.movie_trailer.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        syllabus.bind(this)
        viewContainer.inflateBinding(ActivityMainBinding::inflate)
    }
}