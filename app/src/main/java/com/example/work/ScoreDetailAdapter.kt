package com.example.work

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScoreDetailAdapter(private val scoreDetails: List<ScoreDetail>) :
    RecyclerView.Adapter<ScoreDetailAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val scoreTextView: TextView = view.findViewById(R.id.scoreTextView)
        val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = scoreDetails[position]
        holder.scoreTextView.text = item.score
        holder.timestampTextView.text = formatTimestamp(item.timestamp)
    }

    override fun getItemCount() = scoreDetails.size

    private fun formatTimestamp(timestamp: String): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date = Date(timestamp.toLong())
            sdf.format(date)
        } catch (e: Exception) {
            timestamp
        }
    }
}
