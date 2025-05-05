package com.example.work;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int leftScore = 0;
    private int rightScore = 0;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private TextView leftTeamNameTextView;
    private TextView rightTeamNameTextView;
    private long currentMatchId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
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
        leftTeamNameTextView.setText("隊伍一");
        rightTeamNameTextView.setText("隊伍二");

        // 获取重置分数和设置按钮
        ImageButton resetButton = findViewById(R.id.reset_button);
        ImageButton settingsButton = findViewById(R.id.settings_button);

        // 从 SharedPreferences 中读取分数
        leftScore = sharedPreferences.getInt("leftScore", 0);
        rightScore = sharedPreferences.getInt("rightScore", 0);
        updateScoreTextViews(leftScoreTextView, rightScoreTextView);

        // 設置點擊監聽器
        setupClickListeners(leftScoreTextView, rightScoreTextView, resetButton, settingsButton);
    }

    private void setupClickListeners(TextView leftScoreTextView, TextView rightScoreTextView,
                                   ImageButton resetButton, ImageButton settingsButton) {
        // 设置左边的点击事件
        leftScoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Left score clicked");
                leftScore++;
                String currentScore = leftScore + ":" + rightScore;
                updateScoreTextViews(leftScoreTextView, rightScoreTextView);
                saveScores();
                insertScoreDetail(currentScore);
            }
        });

        // 设置右边的点击事件
        rightScoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Right score clicked");
                rightScore++;
                String currentScore = leftScore + ":" + rightScore;
                updateScoreTextViews(leftScoreTextView, rightScoreTextView);
                saveScores();
                insertScoreDetail(currentScore);
            }
        });

        // 设置重置分数按钮的点击事件
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Reset button clicked");
                String finalScore = leftScore + ":" + rightScore;
                insertFinalScore(finalScore);
                currentMatchId = -1;
                leftScore = 0;
                rightScore = 0;
                updateScoreTextViews(leftScoreTextView, rightScoreTextView);
                saveScores();
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // 设置设置按钮的点击事件
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Settings button clicked");
                Intent intent = new Intent(MainActivity.this, BackgroundActivity.class);
                intent.putExtra("leftScore", leftScore);
                intent.putExtra("rightScore", rightScore);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 從 SharedPreferences 讀取分數
        leftScore = sharedPreferences.getInt("leftScore", 0);
        rightScore = sharedPreferences.getInt("rightScore", 0);
        
        // 從 SharedPreferences 讀取隊伍名稱
        String leftTeamName = sharedPreferences.getString("leftTeamName", "隊伍一");
        String rightTeamName = sharedPreferences.getString("rightTeamName", "隊伍二");
        
        // 更新隊伍名稱顯示
        leftTeamNameTextView.setText(leftTeamName);
        rightTeamNameTextView.setText(rightTeamName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // 接收從 ChangeTeamNameActivity 返回的隊伍名稱
            String teamAName = data.getStringExtra("teamAName");
            String teamBName = data.getStringExtra("teamBName");

            // 更新隊伍名稱显示
            leftTeamNameTextView.setText(teamAName);
            rightTeamNameTextView.setText(teamBName);

            // 保存到 SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("leftTeamName", teamAName);
            editor.putString("rightTeamName", teamBName);
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

    private void insertScoreDetail(String score) {
        // 如果是第一次得分，創建新的比賽記錄
        if (currentMatchId == -1) {
            String leftTeamName = leftTeamNameTextView.getText().toString();
            String rightTeamName = rightTeamNameTextView.getText().toString();
            currentMatchId = dbHelper.insertHistory(
                score,
                String.valueOf(System.currentTimeMillis()),
                leftTeamName,
                rightTeamName
            );
        }

        // 插入詳細得分記錄
        ContentValues detailValues = new ContentValues();
        detailValues.put(DatabaseHelper.COLUMN_HISTORY_ID, currentMatchId);
        detailValues.put(DatabaseHelper.COLUMN_SCORE, score);
        detailValues.put(DatabaseHelper.COLUMN_DETAIL_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        dbHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_SCORE_DETAIL, null, detailValues);
    }

    private void insertFinalScore(String score) {
        // 更新現有比賽記錄的最終分數
        if (currentMatchId != -1) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TITLE, score);
            values.put(DatabaseHelper.COLUMN_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
            values.put(DatabaseHelper.COLUMN_LEFT_TEAM_NAME, leftTeamNameTextView.getText().toString());
            values.put(DatabaseHelper.COLUMN_RIGHT_TEAM_NAME, rightTeamNameTextView.getText().toString());
            dbHelper.getWritableDatabase().update(
                DatabaseHelper.TABLE_HISTORY,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(currentMatchId)}
            );
        }
    }
} 