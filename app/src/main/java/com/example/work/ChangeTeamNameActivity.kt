package com.example.work

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ChangeTeamNameActivity : AppCompatActivity() {
    private lateinit var teamAEditText: EditText
    private lateinit var teamBEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private val TAG = "ChangeTeamNameActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_team_name)

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // 設置 Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "更改隊伍名稱"
            setDisplayHomeAsUpEnabled(true)
        }

        // 獲取輸入框
        teamAEditText = findViewById(R.id.team_a_input)
        teamBEditText = findViewById(R.id.team_b_input)

        // 從 SharedPreferences 獲取當前隊伍名稱
        val currentTeamAName = sharedPreferences.getString("leftTeamName", "隊伍一") ?: "隊伍一"
        val currentTeamBName = sharedPreferences.getString("rightTeamName", "隊伍二") ?: "隊伍二"

        Log.d(TAG, "當前隊伍名稱: $currentTeamAName vs $currentTeamBName")

        teamAEditText.setText(currentTeamAName)
        teamBEditText.setText(currentTeamBName)

        // 設置返回按鈕
        toolbar.setNavigationOnClickListener { finish() }

        // 設置保存按鈕
        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            try {
                var teamAName = teamAEditText.text.toString().trim()
                var teamBName = teamBEditText.text.toString().trim()

                // 如果名稱為空，使用默認值
                if (teamAName.isEmpty()) teamAName = "隊伍一"
                if (teamBName.isEmpty()) teamBName = "隊伍二"

                Log.d(TAG, "保存隊伍名稱: $teamAName vs $teamBName")

                // 保存到 SharedPreferences
                sharedPreferences.edit().apply {
                    putString("leftTeamName", teamAName)
                    putString("rightTeamName", teamBName)
                    apply()
                }

                // 回傳結果
                val resultIntent = Intent()
                resultIntent.putExtra("teamAName", teamAName)
                resultIntent.putExtra("teamBName", teamBName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "保存隊伍名稱時出錯: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

