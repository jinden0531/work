package com.example.work

import android.os.Bundle
import android.util.Log
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

class ScoreDetailActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScoreDetailAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_detail)

        try {
            // 設置 Toolbar
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "逐球記錄"

            toolbar.setNavigationOnClickListener {
                finish()
            }

            dbHelper = DatabaseHelper(this)
            recyclerView = findViewById(R.id.score_detail_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val historyId = intent.getLongExtra("history_id", -1)
            if (historyId != -1L) {
                val scoreDetails = dbHelper.getScoreDetails(historyId)
                adapter = ScoreDetailAdapter(scoreDetails)
                recyclerView.adapter = adapter
            } else {
                Log.e("ScoreDetailActivity", "Invalid history_id")
                finish()
            }
        } catch (e: Exception) {
            Log.e("ScoreDetailActivity", "Error in onCreate: ${e.message}", e)
            finish()
        }
    }

    inner class ScoreDetailAdapter(private val scoreDetails: List<ScoreDetail>) : RecyclerView.Adapter<ScoreDetailAdapter.ViewHolder>() {
        private var leftTeamName: String = "隊伍一"
        private var rightTeamName: String = "隊伍二"

        init {
            val historyId = intent.getLongExtra("history_id", -1)
            if (historyId != -1L) {
                val db = dbHelper.readableDatabase
                val cursor = db.query(
                    DatabaseHelper.TABLE_HISTORY,
                    arrayOf(DatabaseHelper.COLUMN_LEFT_TEAM_NAME, DatabaseHelper.COLUMN_RIGHT_TEAM_NAME),
                    "${DatabaseHelper.COLUMN_ID} = ?",
                    arrayOf(historyId.toString()),
                    null,
                    null,
                    null
                )
                cursor.use {
                    if (it.moveToFirst()) {
                        leftTeamName = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEFT_TEAM_NAME))
                        rightTeamName = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RIGHT_TEAM_NAME))
                    }
                }
            }
        }

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
            try {
                val item = scoreDetails[position]
                Log.d("ScoreDetailAdapter", "Binding item at position $position: score=${item.score}")
                
                // 組合隊伍名稱和比分
                val scoreText = "$leftTeamName ${item.score} $rightTeamName"
                holder.scoreTextView.text = scoreText
                holder.timestampTextView.text = formatTimestamp(item.timestamp)
            } catch (e: Exception) {
                Log.e("ScoreDetailAdapter", "Error in onBindViewHolder: ${e.message}", e)
            }
        }

        override fun getItemCount() = scoreDetails.size

        private fun formatTimestamp(timestamp: String): String {
            return try {
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val date = Date(timestamp.toLong())
                sdf.format(date)
            } catch (e: Exception) {
                Log.e("ScoreDetailAdapter", "Error formatting timestamp: ${e.message}", e)
                timestamp
            }
        }
    }
} 