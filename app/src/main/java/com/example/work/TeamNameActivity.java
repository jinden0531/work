package com.example.work;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TeamNameActivity extends AppCompatActivity {
    private EditText leftTeamEdit;
    private EditText rightTeamEdit;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name);

        // 設置 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // 初始化視圖
        leftTeamEdit = findViewById(R.id.left_team_edit);
        rightTeamEdit = findViewById(R.id.right_team_edit);
        Button saveButton = findViewById(R.id.save_button);

        // 設置當前隊伍名稱
        leftTeamEdit.setText(sharedPreferences.getString("leftTeamName", "Team A"));
        rightTeamEdit.setText(sharedPreferences.getString("rightTeamName", "Team B"));

        // 設置保存按鈕點擊事件
        saveButton.setOnClickListener(v -> {
            String leftTeamName = leftTeamEdit.getText().toString().trim();
            String rightTeamName = rightTeamEdit.getText().toString().trim();

            // 如果名稱為空，使用默認名稱
            if (leftTeamName.isEmpty()) leftTeamName = "Team A";
            if (rightTeamName.isEmpty()) rightTeamName = "Team B";

            // 保存到 SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("leftTeamName", leftTeamName);
            editor.putString("rightTeamName", rightTeamName);
            editor.apply();

            // 返回結果到 MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("leftTeamName", leftTeamName);
            resultIntent.putExtra("rightTeamName", rightTeamName);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
} 