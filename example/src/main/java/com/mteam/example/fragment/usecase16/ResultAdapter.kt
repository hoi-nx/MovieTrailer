package com.mteam.example.fragment.usecase16

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mteam.example.R
import kotlinx.android.synthetic.main.recyclerview_item_calculation_result.view.*

class ResultAdapter(
    private val results: MutableList<UiState.Success> = mutableListOf()
) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    class ViewHolder(
        val layout: LinearLayout,
        val factorialOf: TextView,
        val numberOfCoroutines: TextView,
        val dispatcher: TextView,
        val yielding: TextView,
        val calculationDuration: TextView,
        val stringConversionDuration: TextView,
        val computationResult: TextView

    ) :
        RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.recyclerview_item_calculation_result, parent, false)
        val layout = itemView.findViewById(R.id.linearLayout) as LinearLayout
        val textViewResultFactorialOf = itemView.findViewById(R.id.textViewResultFactorialOf) as TextView
        val textViewResultNumberCoroutines = itemView.findViewById(R.id.textViewResultNumberCoroutines) as TextView
        val textViewResultDispatcher = itemView.findViewById(R.id.textViewResultDispatcher) as TextView
        val textViewResultYield = itemView.findViewById(R.id.textViewResultYield) as TextView
        val textViewDuration = itemView.findViewById(R.id.textViewDuration) as TextView
        val textViewStringConversionDuration = itemView.findViewById(R.id.textViewStringConversionDuration) as TextView
        val textViewResult = itemView.findViewById(R.id.textViewResult) as TextView

        return ViewHolder(
            layout,
           textViewResultFactorialOf,
            textViewResultNumberCoroutines,
            textViewResultDispatcher,
            textViewResultYield,
            textViewDuration,
            textViewStringConversionDuration,
            textViewResult
        )
    }

    fun addResult(state: UiState.Success) {
        results.add(0, state)
        notifyDataSetChanged()
    }

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        val result = results[position]
        val context = layout.context

        factorialOf.text = "Calculated factorial of ${result.factorialOf}"
        numberOfCoroutines.text = "Coroutines: ${result.numberOfCoroutines}"
        dispatcher.text = "Dispatcher: ${result.dispatcherName}"
        yielding.text = "yield(): ${result.yieldDuringCalculation}"

        calculationDuration.text =
            context.getString(R.string.duration_calculation, result.computationDuration)

        stringConversionDuration.text =
            context.getString(R.string.duration_stringconversion, result.stringConversionDuration)

        computationResult.text = if (result.result.length <= 150) {
            "Result: ${result.result}"
        } else {
            "Result: ${result.result.substring(0, 147)}..."
        }
    }
}