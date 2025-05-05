package com.example.work

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ChangeTeamNameActivity : AppCompatActivity() {
    private lateinit var teamOneInput: EditText
    private lateinit var teamTwoInput: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_team_name)

        // 設置 Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "更改隊伍名稱"
            setDisplayHomeAsUpEnabled(true)
        }

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // 獲取輸入框
        teamOneInput = findViewById(R.id.team_a_input)
        teamTwoInput = findViewById(R.id.team_b_input)

        // 設置當前隊伍名稱
        teamOneInput.setText(sharedPreferences.getString("leftTeamName", "隊伍一"))
        teamTwoInput.setText(sharedPreferences.getString("rightTeamName", "隊伍二"))

        // 設置返回按鈕
        toolbar.setNavigationOnClickListener { finish() }

        // 設置保存按鈕
        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            var teamOneName = teamOneInput.text.toString().trim()
            var teamTwoName = teamTwoInput.text.toString().trim()

            // 如果名稱為空，使用默認值
            if (teamOneName.isEmpty()) teamOneName = "隊伍一"
            if (teamTwoName.isEmpty()) teamTwoName = "隊伍二"

            // 保存到 SharedPreferences
            sharedPreferences.edit().apply {
                putString("leftTeamName", teamOneName)
                putString("rightTeamName", teamTwoName)
                apply()
            }

            finish()
        }
    }
}

