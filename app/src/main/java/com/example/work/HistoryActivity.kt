package com.example.work

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val historyList = dbHelper.getHistoryList()

        adapter = HistoryAdapter(
            historyList,
            onDeleteClick = { id ->
                dbHelper.deleteHistory(id)
                refreshHistory()
            },
            onItemClick = { item ->
                val intent = Intent(this, HistoryDetailActivity::class.java)
                intent.putExtra("score", item.title)
                intent.putExtra("timestamp", item.timestamp)
                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter
    }

    private fun refreshHistory() {
        val newHistoryList = dbHelper.getHistoryList()
        adapter.updateData(newHistoryList)
    }
}
