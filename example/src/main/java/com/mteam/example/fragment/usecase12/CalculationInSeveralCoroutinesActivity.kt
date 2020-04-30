package com.mteam.example.fragment.usecase12

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mteam.example.R
import kotlinx.android.synthetic.main.activity_calculationinmultiplebackgroundthreads.*
import com.mteam.example.utils.fromHtml
import com.mteam.example.utils.hideKeyboard
import com.mteam.example.utils.setGone
import com.mteam.example.utils.setVisible

class CalculationInSeveralCoroutinesActivity : Fragment() {

    private val viewModel: CalculationInSeveralCoroutinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.activity_calculationinmultiplebackgroundthreads,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState()
            .observe(this, Observer { uiState ->
                if (uiState != null) {
                    render(uiState)
                }
            })
        btnCalculate.setOnClickListener {
            val factorialOf = editTextFactorialOf.text.toString().toIntOrNull()
            val numberOfThreads = editTextNumberOfThreads.text.toString().toIntOrNull()
            if (factorialOf != null && numberOfThreads != null) {
                viewModel.performCalculation(
                    factorialOf,
                    numberOfThreads
                )
            }
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
        progressBar.setVisible()
        textViewResult.text = ""
        textViewDuration.text = ""
        textViewStringConversionDuration.text = ""
        btnCalculate.isEnabled = false
        textViewResult.hideKeyboard()
    }

    private fun onSuccess(uiState: UiState.Success) {
        textViewDuration.text =
            getString(R.string.duration_calculation, uiState.computationDuration)
        textViewStringConversionDuration.text =
            getString(R.string.duration_stringconversion, uiState.stringConversionDuration)
        progressBar.setGone()
        btnCalculate.isEnabled = true
        textViewResult.text = if (uiState.result.length <= 150) {
            uiState.result
        } else {
            "${uiState.result.substring(0, 147)}..."
        }
    }

    private fun onError(uiState: UiState.Error) {
        progressBar.setGone()
        btnCalculate.isEnabled = true
    }
}