package com.lambdaschool.blockchain_wallet.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lambdaschool.blockchain_wallet.R

class ListAdapter(private val transactionList: List<String>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val viewGroup = LayoutInflater.from(parent.context).inflate(R.layout.rv_items, parent, false)
        return ViewHolder(viewGroup)
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.details.text = transactionList[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val details: TextView = view.findViewById(R.id.rv_text)
    }
}