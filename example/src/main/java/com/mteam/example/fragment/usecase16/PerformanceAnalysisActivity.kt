package com.mteam.example.fragment.usecase16

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mteam.example.R
import com.mteam.example.utils.setGone
import com.mteam.example.utils.setVisible
import kotlinx.android.synthetic.main.activity_performanceanalysis.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PerformanceAnalysisActivity : Fragment() {


    private val viewModel: PerformanceAnalysisViewModel by viewModels()
    private lateinit var selectedDispatcher: CoroutineDispatcher
    private val resultAdapter = ResultAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.activity_performanceanalysis,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        textViewNumberOfCores.text = getString(R.string.device_cores, numberOfCores)
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
                    numberOfThreads,
                    selectedDispatcher,
                    switchYield.isChecked
                )
            }
        }
        ArrayAdapter.createFromResource(
            context!!,
            R.array.dispatchers,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDispatcher.adapter = adapter
        }

        spinnerDispatcher.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent?.getItemAtPosition(position)) {
                        "Default" -> selectedDispatcher = Dispatchers.Default
                        "IO" -> selectedDispatcher = Dispatchers.IO
                        "Main" -> selectedDispatcher = Dispatchers.Main
                        "Unconfined" -> selectedDispatcher = Dispatchers.Unconfined
                    }
                }
            }

        initRecyclerView()
    }


    private fun initRecyclerView() {
        recyclerViewResults.apply {
            adapter = resultAdapter
            hasFixedSize()
            layoutManager = LinearLayoutManager(context!!)
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

    private fun onLoad(){
        progressBar.setVisible()
        btnCalculate.isEnabled = false
    }

    private fun onSuccess(uiState: UiState.Success) {
        progressBar.setGone()
        btnCalculate.isEnabled = true
        resultAdapter.addResult(uiState)
    }

    private fun onError(uiState: UiState.Error){
        progressBar.setGone()
        btnCalculate.isEnabled = true
    }
}