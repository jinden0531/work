package com.example.work

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private var historyItems: List<HistoryItem>,
    private val onDeleteClick: (Long) -> Unit,
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
        val viewDetailsButton: Button = view.findViewById(R.id.viewDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = historyItems[position]
        holder.titleTextView.text = historyItem.title
        holder.timestampTextView.text = historyItem.timestamp

        holder.deleteButton.setOnClickListener {
            onDeleteClick(historyItem.id)
        }

        holder.viewDetailsButton.setOnClickListener {
            onItemClick(historyItem)
        }
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    fun updateData(newHistoryItems: List<HistoryItem>) {
        historyItems = newHistoryItems
        notifyDataSetChanged()
    }
}
