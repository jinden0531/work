package com.example.work

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // 你可以在這裡取得 intent 傳來的資料
        val historyId = intent.getLongExtra("history_id", -1)
        val historyTitle = intent.getStringExtra("history_title")
        val historyTimestamp = intent.getStringExtra("history_timestamp")

        // 接下來你可以根據 ID 去資料庫查詳細內容，然後顯示出來
    }
}
