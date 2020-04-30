package com.mteam.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mteam.example.R
import com.mteam.example.UseCase

class UseCaseAdapter(private val onUseCaseClick: (position:Int) -> Unit
) : RecyclerView.Adapter<UseCaseAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false) as TextView
        return ViewHolder(
            textView
        )
    }

    override fun getItemCount() = UseCase.getUserCase().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = UseCase.getUserCase()[position]
        holder.textView.setOnClickListener {
            onUseCaseClick(position)
        }
    }
}