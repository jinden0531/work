package com.example.work

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ChangeTeamNameActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changeteamname)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "更改隊伍名稱"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val teamAEditText: EditText = findViewById(R.id.team_a)
        val teamBEditText: EditText = findViewById(R.id.team_b)
        val confirmButton: Button = findViewById(R.id.button_confirm)

        databaseHelper = DatabaseHelper(this)

        // 從資料庫獲取當前隊伍名稱
        val currentTeamAName = databaseHelper.getTeamName(1) // 假設隊伍 A ID 為 1
        val currentTeamBName = databaseHelper.getTeamName(2) // 假設隊伍 B ID 為 2

        teamAEditText.setText(currentTeamAName)
        teamBEditText.setText(currentTeamBName)

        confirmButton.setOnClickListener {
            val teamAName = teamAEditText.text.toString()
            val teamBName = teamBEditText.text.toString()

            // 更新資料庫中的隊伍名稱
            databaseHelper.updateTeamName(1, teamAName)
            databaseHelper.updateTeamName(2, teamBName)

            // 回傳結果
            val resultIntent = Intent()
            resultIntent.putExtra("teamAName", teamAName)
            resultIntent.putExtra("teamBName", teamBName)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

