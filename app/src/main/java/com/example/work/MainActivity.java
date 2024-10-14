package com.example.work;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int leftScore = 0;
    private int rightScore = 0;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private TextView leftTeamNameTextView;
    private TextView rightTeamNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // 初始化数据库助手
        dbHelper = new DatabaseHelper(this);

        // 获取左边和右边的 TextView
        TextView leftScoreTextView = findViewById(R.id.left_score);
        TextView rightScoreTextView = findViewById(R.id.right_score);

        // 获取队伍名称 TextView
        leftTeamNameTextView = findViewById(R.id.left_team_name);
        rightTeamNameTextView = findViewById(R.id.right_team_name);

        // 设置默认队伍名称
        leftTeamNameTextView.setText(sharedPreferences.getString("leftTeamName", "Team A"));
        rightTeamNameTextView.setText(sharedPreferences.getString("rightTeamName", "Team B"));

        // 获取重置分数和设置按钮
        ImageButton resetButton = findViewById(R.id.reset_button);
        ImageButton settingsButton = findViewById(R.id.settings_button);

        // 从 SharedPreferences 中读取分数
        leftScore = sharedPreferences.getInt("leftScore", 0);
        rightScore = sharedPreferences.getInt("rightScore", 0);
        updateScoreTextViews(leftScoreTextView, rightScoreTextView);

        // 设置左边的点击事件
        leftScoreTextView.setOnClickListener(v -> {
            leftScore++;
            updateScoreTextViews(leftScoreTextView, rightScoreTextView);
            saveScores();
        });

        // 设置右边的点击事件
        rightScoreTextView.setOnClickListener(v -> {
            rightScore++;
            updateScoreTextViews(leftScoreTextView, rightScoreTextView);
            saveScores();
        });

        // 设置重置分数按钮的点击事件
        resetButton.setOnClickListener(v -> {
            insertHistory(leftScore + " - " + rightScore);
            leftScore = 0;
            rightScore = 0;
            updateScoreTextViews(leftScoreTextView, rightScoreTextView);
            saveScores();
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // 设置设置按钮的点击事件
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BackgroundActivity.class);
            intent.putExtra("leftScore", leftScore);
            intent.putExtra("rightScore", rightScore);
            intent.putExtra("leftTeamName", leftTeamNameTextView.getText().toString());
            intent.putExtra("rightTeamName", rightTeamNameTextView.getText().toString());
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        leftScore = sharedPreferences.getInt("leftScore", 0);
        rightScore = sharedPreferences.getInt("rightScore", 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // 接收从 BackgroundActivity 返回的队伍名称
            String leftTeamName = data.getStringExtra("leftTeamName");
            String rightTeamName = data.getStringExtra("rightTeamName");

            // 更新队伍名称显示
            leftTeamNameTextView.setText(leftTeamName);
            rightTeamNameTextView.setText(rightTeamName);

            // 保存到 SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("leftTeamName", leftTeamName);
            editor.putString("rightTeamName", rightTeamName);
            editor.apply();
        }
    }

    private void updateScoreTextViews(TextView leftScoreTextView, TextView rightScoreTextView) {
        leftScoreTextView.setText(String.valueOf(leftScore));
        rightScoreTextView.setText(String.valueOf(rightScore));
    }

    private void saveScores() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("leftScore", leftScore);
        editor.putInt("rightScore", rightScore);
        editor.apply();
    }

    private void insertHistory(String score) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, score);
        values.put(DatabaseHelper.COLUMN_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        dbHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_HISTORY, null, values);
    }
} 