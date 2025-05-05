package com.example.work

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "歷史紀錄"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val historyList = dbHelper.getHistoryList()

        adapter = HistoryAdapter(
            historyList,
            onDeleteClick = { id ->
                dbHelper.deleteHistory(id)
                refreshHistory()
            },
            onViewDetailsClick = { item ->
                val intent = Intent(this, HistoryDetailActivity::class.java)
                intent.putExtra("history_id", item.id)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun refreshHistory() {
        val newHistoryList = dbHelper.getHistoryList()
        adapter.updateData(newHistoryList)
    }

    inner class HistoryAdapter(
        private var historyList: List<HistoryItem>,
        private val onDeleteClick: (Long) -> Unit,
        private val onViewDetailsClick: (HistoryItem) -> Unit
    ) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleTextView: TextView = view.findViewById(R.id.history_title)
            val timestampTextView: TextView = view.findViewById(R.id.history_timestamp)
            val teamNamesTextView: TextView = view.findViewById(R.id.team_names)
            val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
            val detailButton: Button = view.findViewById(R.id.detail_button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.history_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = historyList[position]
            holder.titleTextView.text = item.title
            holder.timestampTextView.text = formatTimestamp(item.timestamp)
            holder.teamNamesTextView.text = "${item.leftTeamName} vs ${item.rightTeamName}"
            
            holder.deleteButton.setOnClickListener {
                onDeleteClick(item.id)
            }
            
            holder.detailButton.setOnClickListener {
                onViewDetailsClick(item)
            }
        }

        override fun getItemCount() = historyList.size

        fun updateData(newHistoryList: List<HistoryItem>) {
            historyList = newHistoryList
            notifyDataSetChanged()
        }

        private fun formatTimestamp(timestamp: String): String {
            return try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = Date(timestamp.toLong())
                sdf.format(date)
            } catch (e: Exception) {
                timestamp
            }
        }
    }
}
