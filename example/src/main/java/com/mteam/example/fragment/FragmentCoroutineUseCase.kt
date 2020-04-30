package com.mteam.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mteam.example.R
import com.mteam.example.WatchlistApp
import com.mteam.example.adapter.UseCaseAdapter
import kotlinx.android.synthetic.main.fragment_usecases.*

/**
 * https://github.com/LukasLechnerDev/Kotlin-Coroutine-Use-Cases-on-Android
 */
class FragmentCoroutineUseCase :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usecases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

    }
    private val onUseCaseClickListener: (position:Int) -> Unit = { clickedUseCase ->
        when(clickedUseCase){
            0 -> findNavController().navigate(R.id.tofragmentSingleNetworkRequest)
            1 -> findNavController().navigate(R.id.toperform2SequentialNetworkRequestsFragment)
            2 -> findNavController().navigate(R.id.toPerformNetworkRequestsConcurrentlyFragment)
            3 -> findNavController().navigate(R.id.variableAmountOfNetworkRequestsActivity)
            4 -> findNavController().navigate(R.id.networkRequestWithTimeoutActivity)
            5 -> findNavController().navigate(R.id.retryNetworkRequestActivity)
            6 -> findNavController().navigate(R.id.timeoutAndRetryActivity)
            7 -> findNavController().navigate(R.id.timeoutAndRetryRxActivity)
            8 -> findNavController().navigate(R.id.roomAndCoroutinesActivity)
            9 -> findNavController().navigate(R.id.calculationInBackgroundActivity)
            10 -> findNavController().navigate(R.id.calculationInSeveralCoroutinesActivity)
            11 -> findNavController().navigate(R.id.exceptionHandlingActivity)
            12 -> findNavController().navigate(R.id.continueCoroutineWhenUserLeavesScreenActivity)
            13 -> findNavController().navigate(R.id.workManagerActivity)
            14 -> findNavController().navigate(R.id.performanceAnalysisActivity)

        }

    }
    private fun initRecyclerView() {
        recyclerView.apply {
            adapter =
                UseCaseAdapter(onUseCaseClickListener)
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(initItemDecoration())
        }
    }

    private fun initItemDecoration(): DividerItemDecoration {
        val itemDecorator =
            DividerItemDecoration(WatchlistApp.applicationContext, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                WatchlistApp.applicationContext!!,
                R.drawable.recyclerview_divider
            )!!
        )
        return itemDecorator
    }
}