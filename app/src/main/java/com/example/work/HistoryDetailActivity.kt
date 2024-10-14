package com.example.work

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        val score = intent.getStringExtra("score") ?: "N/A"
        val timestamp = intent.getStringExtra("timestamp") ?: "N/A"

        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        val timestampTextView = findViewById<TextView>(R.id.timestampTextView)

        scoreTextView.text = "比分紀錄：$score"
        timestampTextView.text = "時間：$timestamp"
    }
}
