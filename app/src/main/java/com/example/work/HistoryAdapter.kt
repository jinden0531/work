package com.example.work

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private var historyItems: List<HistoryItem>,
    private val onDeleteClick: (Long) -> Unit,
    private val onViewDetailsClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamNamesTextView: TextView = view.findViewById(R.id.team_names)
        val titleTextView: TextView = view.findViewById(R.id.history_title)
        val timestampTextView: TextView = view.findViewById(R.id.history_timestamp)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
        val detailButton: Button = view.findViewById(R.id.detail_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = historyItems[position]
        holder.teamNamesTextView.text = "${historyItem.leftTeamName} vs ${historyItem.rightTeamName}"
        holder.titleTextView.text = historyItem.title
        holder.timestampTextView.text = historyItem.timestamp

        holder.deleteButton.setOnClickListener {
            onDeleteClick(historyItem.id)
        }

        holder.detailButton.setOnClickListener {
            onViewDetailsClick(historyItem)
        }
    }

    override fun getItemCount(): Int = historyItems.size

    fun updateData(newHistoryItems: List<HistoryItem>) {
        historyItems = newHistoryItems
        notifyDataSetChanged()
    }
}
