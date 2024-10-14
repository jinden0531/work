package com.example.work;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;

public class ChangenameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changename);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("更改隊伍名稱");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 初始化 SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TeamNames", MODE_PRIVATE);

        // 找到確認按鈕
        Button confirmButton = findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 獲取 EditText 中的文本
                String leftName = ((EditText) findViewById(R.id.team_a)).getText().toString();
                String rightName = ((EditText) findViewById(R.id.team_b)).getText().toString();

                // 保存到 SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("teamAName", leftName);
                editor.putString("teamBName", rightName);
                editor.apply();

                // 返回主頁面
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // 預填充當前名稱
        String currentLeftName = sharedPreferences.getString("teamAName", "");
        String currentRightName = sharedPreferences.getString("teamBName", "");
        ((EditText) findViewById(R.id.team_a)).setText(currentLeftName);
        ((EditText) findViewById(R.id.team_b)).setText(currentRightName);
    }
}
