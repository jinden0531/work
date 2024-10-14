package com.example.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.app.Activity;
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class BackgroundActivity extends AppCompatActivity {
    private List<ImageButton> leftSideButtons;
    private List<ImageButton> rightSideButtons;
    private static final int REQUEST_CODE_CHANGE_NAME = 1;

    // 左側和右側的圖片資源 ID 陣列
    int[] leftSideImages = {
            R.drawable.volleyball_5, // 對應左側位置 1
            R.drawable.volleyball_4, // 對應左側位置 2
            R.drawable.volleyball_6, // 對應左側位置 3
            R.drawable.volleyball_3, // 對應左側位置 4
            R.drawable.volleyball_1, // 對應左側位置 5
            R.drawable.volleyball_2  // 對應左側位置 6
    };

    int[] rightSideImages = {
            R.drawable.volleyball_2, // 對應右側位置 1
            R.drawable.volleyball_1, // 對應右側位置 2
            R.drawable.volleyball_3, // 對應右側位置 3
            R.drawable.volleyball_6, // 對應右側位置 4
            R.drawable.volleyball_4, // 對應右側位置 5
            R.drawable.volleyball_5  // 對應右側位置 6
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("後台模式");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 获取传递过来的分数
        Intent intent = getIntent();
        final int[] leftScore = {intent.getIntExtra("leftScore", 0)};
        final int[] rightScore = {intent.getIntExtra("rightScore", 0)};

        // 初始化分数 TextView
        TextView leftScoreTextView = findViewById(R.id.left_score_background);
        TextView rightScoreTextView = findViewById(R.id.right_score_background);
        leftScoreTextView.setText(String.valueOf(leftScore[0]));
        rightScoreTextView.setText(String.valueOf(rightScore[0]));
        // 獲取 TextView
        TextView left_name = findViewById(R.id.left_name);
        TextView right_name = findViewById(R.id.right_name);

        // 加載 SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TeamNames", MODE_PRIVATE);

        // 更新隊伍名稱
        String leftName = sharedPreferences.getString("teamAName", "隊伍 A");
        String rightName = sharedPreferences.getString("teamBName", "隊伍 B");

        left_name.setText(leftName);
        right_name.setText(rightName);

        // 获取主按钮和 gridButtons
        ImageButton[] gridButtons = new ImageButton[12];
        for (int i = 1; i <= 12; i++) {
            String buttonID = "button_grid_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            gridButtons[i - 1] = findViewById(resID);
        }

        // 获取 centerButton
        ImageButton centerButton = findViewById(R.id.centerButton);

        // 设置 gridButton 的点击监听器
        View.OnClickListener buttonClickListener = v -> {
            ImageButton clickedButton = (ImageButton) v;
            centerButton.setImageDrawable(clickedButton.getDrawable());

            // 确保设置 tag 到 centerButton
            Object tagObj = clickedButton.getTag();
            if (tagObj != null) {
                centerButton.setTag(tagObj);
                Log.d("CenterButtonTag", "Set tag on centerButton: " + tagObj);  // 添加调试日志
            } else {
                Log.d("CenterButtonTag", "Failed to set tag - tag is null");  // 调试
            }
        };

        leftSideButtons = new ArrayList<>(Arrays.asList(
                findViewById(R.id.button_grid_5), // 位置 1
                findViewById(R.id.button_grid_3), // 位置 2
                findViewById(R.id.button_grid_1), // 位置 3
                findViewById(R.id.button_grid_2), // 位置 4
                findViewById(R.id.button_grid_4), // 位置 5
                findViewById(R.id.button_grid_6)  // 位置 6
        ));

        rightSideButtons = new ArrayList<>(Arrays.asList(
                findViewById(R.id.button_grid_1), // 順序 1
                findViewById(R.id.button_grid_2), // 順序 2
                findViewById(R.id.button_grid_3), // 順序 3
                findViewById(R.id.button_grid_4), // 順序 4
                findViewById(R.id.button_grid_5), // 順序 5
                findViewById(R.id.button_grid_6)  // 順序 6
        ));




        // 为每个 gridButton 设置点击监听器
        for (int i = 0; i < gridButtons.length; i++) {
            gridButtons[i].setTag(i + 1);  // 确保每个按钮都有 tag
            gridButtons[i].setOnClickListener(buttonClickListener);
        }

        // 获取主按钮并展开
        ImageButton pointButton1 = findViewById(R.id.point_button1);
        ImageButton pointButton2 = findViewById(R.id.point_button2);
        ImageButton pointButton3 = findViewById(R.id.point_button3);
        ImageButton pointButton4 = findViewById(R.id.point_button4);
        ImageButton pointButton5 = findViewById(R.id.point_button5);
        ImageButton pointButton6 = findViewById(R.id.point_button6);
        List<ImageButton> pointButtons = Arrays.asList(pointButton1, pointButton2, pointButton3, pointButton4, pointButton5, pointButton6);
        expandMainButtons(pointButtons, 210);  // 半径为 250 的圆形布局

        setupPointButtonWithSubButtons(pointButton1, 0, R.id.sub_button1_1, R.id.sub_button1_2);
        setupPointButtonWithSubButtons(pointButton2, 60, R.id.sub_button2_1, R.id.sub_button2_2);
        setupPointButtonWithSubButtons(pointButton3, 120, R.id.sub_button3_1, R.id.sub_button3_2);
        setupPointButtonWithSubButtons(pointButton4, 180, R.id.sub_button4_1, R.id.sub_button4_2);
        setupPointButtonWithSubButtons(pointButton5, 240, R.id.sub_button5_1, R.id.sub_button5_2);
        setupPointButtonWithSubButtons(pointButton6, 300, R.id.sub_button6_1, R.id.sub_button6_2);

        // 设置每个 sub_button 的点击事件监听器
        setupSubButtonClickListener(R.id.sub_button1_1, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button1_2, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button2_1, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button2_2, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button3_1, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button3_2, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button4_1, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button4_2, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button5_1, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button5_2, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button6_1, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(R.id.sub_button6_2, centerButton, leftScore, rightScore, leftScoreTextView, rightScoreTextView);

        // 確保所有按鈕已被找到
        if (leftSideButtons.contains(null) || rightSideButtons.contains(null)) {
            Log.e("Initialization", "Failed to initialize buttons - some buttons are null.");
            return;
        }
    }

    private void expandMainButtons(List<ImageButton> buttons, float radius) {
        double[] angles = {0, 60, 120, 180, 240, 300}; // 每个按钮的角度

        for (int i = 0; i < buttons.size(); i++) {
            ImageButton button = buttons.get(i);
            button.setVisibility(View.VISIBLE);  // 确保按钮可见

            double angleInRadians = Math.toRadians(angles[i]);
            float translationX = (float) (radius * Math.cos(angleInRadians));
            float translationY = (float) (radius * Math.sin(angleInRadians));

            button.setTranslationX(translationX);
            button.setTranslationY(translationY);
        }
    }

    private void setupSubButtonClickListener(int subButtonId, ImageButton centerButton, int[] leftScore, int[] rightScore, TextView leftScoreTextView, TextView rightScoreTextView) {
        ImageButton subButton = findViewById(subButtonId);
        subButton.setOnClickListener(v -> {
            Object tagObj = centerButton.getTag();
            if (tagObj != null) {
                int tag = (int) tagObj;

                if (centerButton.getDrawable() != null) {
                    Log.d("SubButtonClick", "Tag on centerButton: " + tag);

                    if (subButtonId == R.id.sub_button1_1 || subButtonId == R.id.sub_button1_2 ||
                            subButtonId == R.id.sub_button3_1 || subButtonId == R.id.sub_button6_1) {

                        if (tag >= 1 && tag <= 6) {
                            rightScore[0]++;
                            rightScoreTextView.setText(String.valueOf(rightScore[0]));
                            Log.d("Rotation", "Right side rotated");
                            rotateLeftSide();
                        } else if (tag >= 7 && tag <= 12) {
                            leftScore[0]++;
                            leftScoreTextView.setText(String.valueOf(leftScore[0]));
                            Log.d("Rotation", "Left side rotated");
                            rotateRightSide();
                        }

                    } else {
                        if (tag >= 1 && tag <= 6) {
                            leftScore[0]++;
                            leftScoreTextView.setText(String.valueOf(leftScore[0]));
                            Log.d("Rotation", "Left side rotated");
                            rotateLeftSide();
                        } else if (tag >= 7 && tag <= 12) {
                            rightScore[0]++;
                            rightScoreTextView.setText(String.valueOf(rightScore[0]));
                            Log.d("Rotation", "Right side rotated");
                            rotateRightSide();
                        }
                    }
                }
            }
        });
    }

    private void setupPointButtonWithSubButtons(ImageButton pointButton, double angleOffset, int... subButtonIds) {
        List<ImageButton> subButtons = new ArrayList<>();
        for (int id : subButtonIds) {
            ImageButton subButton = findViewById(id);
            subButtons.add(subButton);
            subButton.setVisibility(View.GONE);
        }

        pointButton.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    for (int i = 0; i < subButtons.size(); i++) {
                        ImageButton subButton = subButtons.get(i);
                        subButton.setVisibility(View.VISIBLE);

                        double subAngle = Math.toRadians(angleOffset + 45 * i);
                        float translationX = (float) (150 * Math.cos(subAngle));
                        float translationY = (float) (150 * Math.sin(subAngle));

                        subButton.animate()
                                .translationX(translationX + pointButton.getTranslationX())
                                .translationY(translationY + pointButton.getTranslationY())
                                .setDuration(300)
                                .start();
                    }
                } else {
                    for (ImageButton subButton : subButtons) {
                        subButton.animate()
                                .translationX(pointButton.getTranslationX())
                                .translationY(pointButton.getTranslationY())
                                .setDuration(300)
                                .withEndAction(() -> subButton.setVisibility(View.GONE))
                                .start();
                    }
                }
                isExpanded = !isExpanded;
            }
        });
    }

    private void rotateLeftSide() {
        if (leftSideButtons != null && !leftSideButtons.isEmpty()) {
            ImageButton lastButton = leftSideButtons.remove(leftSideButtons.size() - 1);
            leftSideButtons.add(0, lastButton); // 將最後一個按鈕移到第一個位置
            updateButtons(leftSideButtons, leftSideImages); // 更新按鈕的圖像
        }
    }

    // 右側順時針輪轉
    private void rotateRightSide() {
        if (rightSideButtons != null && !rightSideButtons.isEmpty()) {
            ImageButton firstButton = rightSideButtons.remove(0);
            rightSideButtons.add(firstButton); // 將第一個按鈕移到最後一個位置
            updateButtons(rightSideButtons, rightSideImages); // 更新按鈕的圖像
            Log.d("Rotation", "Right side rotated");
        }
    }

    private void updateButtons(List<ImageButton> buttons, int[] images) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setImageResource(images[i]);
            buttons.get(i).setTag(i + 1); // 更新標籤
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 確保返回時名稱更新
        SharedPreferences sharedPreferences = getSharedPreferences("TeamNames", MODE_PRIVATE);

        TextView leftTeamTextView = findViewById(R.id.left_name);
        TextView rightTeamTextView = findViewById(R.id.right_name);

        String leftName = sharedPreferences.getString("teamAName", "隊伍 A");
        String rightName = sharedPreferences.getString("teamBName", "隊伍 B");

        leftTeamTextView.setText(leftName);
        rightTeamTextView.setText(rightName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_analyze).setVisible(true);
        menu.findItem(R.id.action_history).setVisible(true);
        menu.findItem(R.id.action_changename).setVisible(true);
        menu.findItem(R.id.action_mode).setVisible(true);
        menu.findItem(R.id.action_background).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_analyze) {
            startActivity(new Intent(BackgroundActivity.this, AnalyzeActivity.class));
            return true;
        }
        else if (id == R.id.action_history) {
            startActivity(new Intent(BackgroundActivity.this, HistoryActivity.class));
            return true;
        }
        else if (id == R.id.action_changename) {
            Intent intent = new Intent(BackgroundActivity.this, ChangenameActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME);
            return true;
        }
        else if (id == R.id.action_mode) {
            startActivity(new Intent(BackgroundActivity.this, ModeActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
