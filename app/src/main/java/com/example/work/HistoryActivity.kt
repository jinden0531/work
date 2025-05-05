package com.example.work

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : AppCompatActivity() {
    private val TAG = "HistoryActivity"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
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

            try {
                val historyList = dbHelper.getHistoryList()
                Log.d(TAG, "獲取到 ${historyList.size} 條歷史記錄")

                adapter = HistoryAdapter(
                    historyList,
                    onDeleteClick = { id ->
                        try {
                            dbHelper.deleteHistory(id)
                            refreshHistory()
                        } catch (e: Exception) {
                            Log.e(TAG, "刪除歷史記錄失敗: ${e.message}")
                            Toast.makeText(this, "刪除失敗", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onViewDetailsClick = { item ->
                        try {
                            val intent = Intent(this, HistoryDetailActivity::class.java)
                            intent.putExtra("history_id", item.id)
                            startActivity(intent)
                        } catch (e: Exception) {
                            Log.e(TAG, "查看詳情失敗: ${e.message}")
                            Toast.makeText(this, "無法查看詳情", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e(TAG, "載入歷史記錄失敗: ${e.message}")
                Toast.makeText(this, "載入歷史記錄失敗", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "初始化失敗: ${e.message}")
            Toast.makeText(this, "初始化失敗", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun refreshHistory() {
        try {
            val newHistoryList = dbHelper.getHistoryList()
            adapter.updateData(newHistoryList)
        } catch (e: Exception) {
            Log.e(TAG, "更新歷史記錄失敗: ${e.message}")
            Toast.makeText(this, "更新失敗", Toast.LENGTH_SHORT).show()
        }
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
            try {
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
            } catch (e: Exception) {
                Log.e(TAG, "綁定視圖失敗: ${e.message}")
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
                Log.e(TAG, "格式化時間戳失敗: ${e.message}")
                timestamp
            }
        }
    }
}
