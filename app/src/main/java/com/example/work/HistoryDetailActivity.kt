package com.example.work

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScoreDetailAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "逐球記錄"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val historyId = intent.getLongExtra("history_id", -1)
        if (historyId != -1L) {
            val scoreDetails = dbHelper.getScoreDetails(historyId)
            adapter = ScoreDetailAdapter(scoreDetails)
            recyclerView.adapter = adapter
        }
    }

    inner class ScoreDetailAdapter(private val scoreDetails: List<ScoreDetail>) : RecyclerView.Adapter<ScoreDetailAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
}
