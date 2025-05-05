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
                try {
                    Log.d(TAG, "Reset button clicked");
                    String finalScore = leftScore + ":" + rightScore;
                    
                    // 如果有進行中的比賽，更新最終分數
                    if (currentMatchId != -1) {
                        insertFinalScore(finalScore);
                    }
                    
                    // 重置分數
                    currentMatchId = -1;
                    leftScore = 0;
                    rightScore = 0;
                    
                    // 更新分數顯示
                    updateScoreTextViews(leftScoreTextView, rightScoreTextView);
                    
                    // 保存分數
                    saveScores();
                    
                    // 最後再切換到歷史記錄頁面
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error in reset button click: " + e.getMessage());
                    e.printStackTrace();
                }
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
        try {
            Log.d(TAG, "onResume: 恢復分數和隊伍名稱");
            // 從 SharedPreferences 讀取分數
            leftScore = sharedPreferences.getInt("leftScore", 0);
            rightScore = sharedPreferences.getInt("rightScore", 0);
            
            // 從 SharedPreferences 讀取隊伍名稱
            String leftTeamName = sharedPreferences.getString("leftTeamName", "隊伍一");
            String rightTeamName = sharedPreferences.getString("rightTeamName", "隊伍二");
            
            Log.d(TAG, "恢復隊伍名稱: " + leftTeamName + " vs " + rightTeamName);
            // 更新隊伍名稱顯示
            leftTeamNameTextView.setText(leftTeamName);
            rightTeamNameTextView.setText(rightTeamName);

            // 更新分數顯示
            TextView leftScoreTextView = findViewById(R.id.left_score);
            TextView rightScoreTextView = findViewById(R.id.right_score);
            updateScoreTextViews(leftScoreTextView, rightScoreTextView);
        } catch (Exception e) {
            Log.e(TAG, "onResume 出錯: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            try {
                Log.d(TAG, "接收隊伍名稱更新");
                // 接收從 ChangeTeamNameActivity 返回的隊伍名稱
                String teamAName = data.getStringExtra("teamAName");
                String teamBName = data.getStringExtra("teamBName");

                if (teamAName != null && teamBName != null) {
                    Log.d(TAG, "更新隊伍名稱: " + teamAName + " vs " + teamBName);
                    // 更新隊伍名稱显示
                    leftTeamNameTextView.setText(teamAName);
                    rightTeamNameTextView.setText(teamBName);

                    // 保存到 SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("leftTeamName", teamAName);
                    editor.putString("rightTeamName", teamBName);
                    editor.apply();

                    // 如果有進行中的比賽，更新隊伍名稱
                    if (currentMatchId != -1) {
                        String currentScore = leftScore + ":" + rightScore;
                        dbHelper.updateHistory(
                            currentMatchId,
                            currentScore,
                            String.valueOf(System.currentTimeMillis()),
                            teamAName,
                            teamBName
                        );
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "更新隊伍名稱時出錯: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateScoreTextViews(TextView leftScoreTextView, TextView rightScoreTextView) {
        Log.d(TAG, "更新分數顯示: " + leftScore + ":" + rightScore);
        leftScoreTextView.setText(String.valueOf(leftScore));
        rightScoreTextView.setText(String.valueOf(rightScore));
    }

    private void saveScores() {
        Log.d(TAG, "保存分數: " + leftScore + ":" + rightScore);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("leftScore", leftScore);
        editor.putInt("rightScore", rightScore);
        editor.apply();
    }

    private void insertScoreDetail(String score) {
        try {
            Log.d(TAG, "開始記錄分數: " + score);
            // 如果是第一次得分，創建新的比賽記錄
            if (currentMatchId == -1) {
                String leftTeamName = leftTeamNameTextView.getText().toString();
                String rightTeamName = rightTeamNameTextView.getText().toString();
                Log.d(TAG, "創建新比賽記錄: " + leftTeamName + " vs " + rightTeamName);
                currentMatchId = dbHelper.insertHistory(
                    score,
                    String.valueOf(System.currentTimeMillis()),
                    leftTeamName,
                    rightTeamName
                );
                Log.d(TAG, "新比賽記錄ID: " + currentMatchId);
            }

            // 插入詳細得分記錄
            long detailId = dbHelper.insertScoreDetail(
                currentMatchId,
                score,
                String.valueOf(System.currentTimeMillis())
            );
            Log.d(TAG, "插入詳細記錄ID: " + detailId);
        } catch (Exception e) {
            Log.e(TAG, "記錄分數時出錯: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertFinalScore(String score) {
        try {
            Log.d(TAG, "開始記錄最終分數: " + score);
            // 更新現有比賽記錄的最終分數
            if (currentMatchId != -1) {
                String leftTeamName = leftTeamNameTextView.getText().toString();
                String rightTeamName = rightTeamNameTextView.getText().toString();
                Log.d(TAG, "更新比賽記錄: " + currentMatchId);
                dbHelper.updateHistory(
                    currentMatchId,
                    score,
                    String.valueOf(System.currentTimeMillis()),
                    leftTeamName,
                    rightTeamName
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "記錄最終分數時出錯: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 