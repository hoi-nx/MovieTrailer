package com.mteam.movie_trailer.fragment.usecase13

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mteam.movie_trailer.R
import com.mteam.movie_trailer.utils.fromHtml
import com.mteam.movie_trailer.utils.setGone
import com.mteam.movie_trailer.utils.setVisible
import kotlinx.android.synthetic.main.activity_exceptionhandling.*

class ExceptionHandlingActivity : Fragment() {


    private val viewModel: ExceptionHandlingViewModel by viewModels()

    private var operationStartTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.activity_exceptionhandling,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        btnExceptionTryCatch.setOnClickListener {
            viewModel.handleExceptionWithTryCatch()
        }
        btnCoroutineExceptionHandler.setOnClickListener {
            viewModel.handleWithCoroutineExceptionHandler()
        }
        btnShowResultsEvenIfChildCoroutineFailsTryCatch.setOnClickListener {
            viewModel.showResultsEvenIfChildCoroutineFails()
        }
        btnShowResultsEvenIfChildCoroutineFailsRunCatching.setOnClickListener {
            viewModel.showResultsEvenIfChildCoroutineFailsRunCatching()
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad()
            }
            is UiState.Success -> {
                onSuccess(uiState)
            }
            is UiState.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() {
        operationStartTime = System.currentTimeMillis()
        progressBar.setVisible()
        textViewDuration.text = ""
        textViewResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        progressBar.setGone()
        val duration = System.currentTimeMillis() - operationStartTime
        textViewDuration.text = getString(R.string.duration, duration)

        val versionFeatures = uiState.versionFeatures
        val versionFeaturesString = versionFeatures.joinToString(separator = "<br><br>") {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${it.features.joinToString(
                prefix = "- ",
                separator = "<br>- "
            )}"
        }

        textViewResult.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        progressBar.setGone()
        textViewDuration.setGone()
        enableButtons()
    }

    private fun enableButtons() {
        btnExceptionTryCatch.isEnabled = true
        btnCoroutineExceptionHandler.isEnabled = true
        btnShowResultsEvenIfChildCoroutineFailsTryCatch.isEnabled = true
        btnShowResultsEvenIfChildCoroutineFailsRunCatching.isEnabled = true
    }

    private fun disableButtons() {
        btnExceptionTryCatch.isEnabled = false
        btnCoroutineExceptionHandler.isEnabled = false
        btnShowResultsEvenIfChildCoroutineFailsTryCatch.isEnabled = false
        btnShowResultsEvenIfChildCoroutineFailsRunCatching.isEnabled = false
    }
}