package com.mteam.example.fragment.usecase4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mteam.example.R
import kotlinx.android.synthetic.main.activity_performvariableamountofnetworkrequestsconcurrently.*
import com.mteam.example.utils.fromHtml
import com.mteam.example.utils.setGone
import com.mteam.example.utils.setVisible

class VariableAmountOfNetworkRequestsActivity : Fragment() {

    private val viewModel: VariableAmountOfNetworkRequestsViewModel by viewModels()

    private var operationStartTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.activity_performvariableamountofnetworkrequestsconcurrently,
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
        btnRequestsSequentially.setOnClickListener {
            viewModel.performNetworkRequestsSequentially()
        }

        btnRequestsConcurrently.setOnClickListener {
            viewModel.performNetworkRequestsConcurrently()
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
        textViewResult.text = ""
        textViewDuration.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        progressBar.setGone()
        val duration = System.currentTimeMillis() - operationStartTime
        textViewDuration.text = getString(R.string.duration, duration)

        val versionFeatures = uiState.versionFeatures
        val versionFeaturesString = versionFeatures.map {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${it.features.joinToString(
                prefix = "- ",
                separator = "<br>- "
            )}"
        }.joinToString(separator = "<br><br>")

        textViewResult.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        progressBar.setGone()
        enableButtons()
    }

    private fun enableButtons() {
        btnRequestsSequentially.isEnabled = true
        btnRequestsConcurrently.isEnabled = true
    }

    private fun disableButtons() {
        btnRequestsSequentially.isEnabled = false
        btnRequestsConcurrently.isEnabled = false
    }
}