package com.example.work;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView left_score;
    private TextView right_score;
    private ImageButton resetScoreButton;
    private ImageButton settingButton;

    private TextView left_TextView;
    private TextView right_TextView;
    private SharedPreferences sharedPreferences;

    private int left_Score = 0;
    private int right_Score = 0;
    private int count01 = 0; // 新增 count01 属性
    private int count02 = 0; // 新增 count02 属性

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // 获取上方分数 TextView 和设置点击监听器
        left_score = findViewById(R.id.left_score);
        left_score.setOnClickListener(this);

        // 获取下方分数 TextView 和设置点击监听器
        right_score = findViewById(R.id.right_score);
        right_score.setOnClickListener(this);

        // 获取重新计算分数的 Button 和设置点击监听器
        resetScoreButton = findViewById(R.id.reset_button);
        resetScoreButton.setOnClickListener(v -> resetScores());

        // 获取设置的 Button 和设置点击监听器
        settingButton = findViewById(R.id.settings_button);
        settingButton.setOnClickListener(v -> {
            // 跳转到设置界面
            Intent intent = new Intent(MainActivity.this, BackgroundActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHANGE_TEAM_NAME);
        });

        // 从SharedPreferences中读取分数并更新显示
        left_Score = sharedPreferences.getInt("left_Score", 0);
        right_Score = sharedPreferences.getInt("right_Score", 0);
        updateTopScoreTextView();
        updateBottomScoreTextView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("TeamNames", MODE_PRIVATE);

        TextView left_TextView = findViewById(R.id.left_TextView);
        TextView right_TextView = findViewById(R.id.right_TextView);

        String teamAName = sharedPreferences.getString("teamAName", "隊伍 A");
        String teamBName = sharedPreferences.getString("teamBName", "隊伍 B");

        left_TextView.setText(teamAName);
        right_TextView.setText(teamBName);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.left_score) {
            left_Score++;
            count01++; // 更新 count01 的值
            updateTopScoreTextView();
            saveScores();
        } else if (view.getId() == R.id.right_score) {
            right_Score++;
            count02++; // 更新 count02 的值
            updateBottomScoreTextView();
            saveScores();
        }
    }

    // 更新上方分数显示
    private void updateTopScoreTextView() {
        left_score.setText(String.valueOf(left_Score));
    }

    // 更新下方分数显示
    private void updateBottomScoreTextView() {
        right_score.setText(String.valueOf(right_Score));
    }

    // 重置分数
    private void resetScores() {
        left_Score = 0;
        right_Score = 0;
        count01 = 0; // 重置 count01 的值
        count02 = 0; // 重置 count02 的值
        updateTopScoreTextView();
        updateBottomScoreTextView();
        saveScores();
    }

    private void saveScores() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("left_Score", left_Score);
        editor.putInt("right_Score", right_Score);
        editor.apply();
    }

    public static final int REQUEST_CODE_CHANGE_TEAM_NAME = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANGE_TEAM_NAME && resultCode == Activity.RESULT_OK && data != null) {
            String leftName = data.getStringExtra("left_Name");
            String rightName = data.getStringExtra("right_Name");

            // 更新隊伍名稱顯示
            TextView leftTeamTextView = findViewById(R.id.left_name);
            TextView rightTeamTextView = findViewById(R.id.right_name);
            leftTeamTextView.setText(leftName);
            rightTeamTextView.setText(rightName);
        }
    }

}
