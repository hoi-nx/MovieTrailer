package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase15

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mteam.example.R
import com.mteam.example.fragment.usecase15.ViewModelFactory
import com.mteam.example.fragment.usecase15.WorkManagerViewModel

class WorkManagerActivity : Fragment() {


    private val viewModel: WorkManagerViewModel by viewModels {
        ViewModelFactory(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.activity_workmanger,
            container,
            false
        )
    }
}