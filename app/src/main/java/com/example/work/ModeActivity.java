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
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ModeActivity extends AppCompatActivity {
    private List<ImageButton> leftSideButtons;
    private static final int REQUEST_CODE_CHANGE_NAME = 1;

    // 左側的圖片資源 ID 陣列
    private int[] drawableIds  = {
            R.drawable.volleyball_1,
            R.drawable.volleyball_2,
            R.drawable.volleyball_3,
            R.drawable.volleyball_4,
            R.drawable.volleyball_5,
            R.drawable.volleyball_6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        // 設置 Toolbar
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

        Intent intent = getIntent();
        final int[] leftScore = {intent.getIntExtra("leftScore", 0)};
        final int[] rightScore = {intent.getIntExtra("rightScore", 0)};

        // 初始化分數顯示
        TextView leftScoreTextView = findViewById(R.id.left_score_background);
        TextView rightScoreTextView = findViewById(R.id.right_score_background);
        leftScoreTextView.setText(String.valueOf(leftScore[0]));
        rightScoreTextView.setText(String.valueOf(rightScore[0]));

        // 加載隊伍名稱
        TextView leftNameTextView = findViewById(R.id.left_name);
        TextView rightNameTextView = findViewById(R.id.right_name);
        SharedPreferences sharedPreferences = getSharedPreferences("TeamNames", MODE_PRIVATE);

        String leftName = sharedPreferences.getString("teamAName", "隊伍 A");
        String rightName = sharedPreferences.getString("teamBName", "隊伍 B");

        leftNameTextView.setText(leftName);
        rightNameTextView.setText(rightName);


        // 获取 centerButton
        ImageButton centerButton = findViewById(R.id.centerButton);

        ImageButton[] gridButtons = new ImageButton[6];
        // 设置 gridButton 的点击监听器
        View.OnClickListener buttonClickListener = v -> {
            ImageButton clickedButton = (ImageButton) v;
            centerButton.setImageDrawable(clickedButton.getDrawable());
        };

        leftSideButtons = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            String buttonID = "button_grid_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ImageButton button = findViewById(resID);

            if (button != null) {
                leftSideButtons.add(button);
            } else {
                Log.e("Initialization", "Button not found: " + buttonID);
            }
        }

        // 初始化 gridButtons
        for (int i = 1; i <= 6; i++) {
            String buttonID = "button_grid_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            gridButtons[i - 1] = findViewById(resID);

            if (gridButtons[i - 1] == null) {
                Log.e("Initialization", "Button not found: " + buttonID);
            } else {
                gridButtons[i - 1].setOnClickListener(buttonClickListener);
            }
        }

        // 为每个 gridButton 设置点击监听器
        for (int i = 0; i < gridButtons.length; i++) {
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

        if (subButton != null) { // 确保 subButton 不为空
            subButton.setOnClickListener(view -> {
                // 确保 centerButton 的 Drawable 不为空
                if (centerButton.getDrawable() != null) {
                    // 更新 CenterButton 的 Tag
                    centerButton.setTag(subButtonId);

                    // 判定是哪一侧加分并轮转图片
                    if (subButtonId == R.id.sub_button1_1 ||
                            subButtonId == R.id.sub_button1_2 ||
                            subButtonId == R.id.sub_button3_1 ||
                            subButtonId == R.id.sub_button6_1) {

                        rightScore[0]++;
                        rightScoreTextView.setText(String.valueOf(rightScore[0]));
                        rotateImagesClockwise();
                    } else {
                        leftScore[0]++;
                        leftScoreTextView.setText(String.valueOf(leftScore[0]));
                        rotateImagesClockwise();
                    }
                }
            });
        } else {
            Log.e("setupSubButtonClick", "SubButton with ID " + subButtonId + " not found!");
        }
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

    private void rotateImagesClockwise() {
        // 保存最后一个图片资源（顺时针移动）
        int lastDrawable = drawableIds[drawableIds.length - 1];

        // 图片资源顺时针移动
        for (int i = drawableIds.length - 1; i > 0; i--) {
            drawableIds[i] = drawableIds[i - 1];
        }
        drawableIds[0] = lastDrawable;

        // 更新按钮图片
        updateButtons();

        // 日志调试
        Log.d("RotationLogic", "Drawable IDs: " + Arrays.toString(drawableIds));
    }


    // 左側按鈕的輪轉方法
    private void rotateLeftSide() {
        if (leftSideButtons != null && !leftSideButtons.isEmpty()) {
            ImageButton lastButton = leftSideButtons.remove(leftSideButtons.size() - 1);
            leftSideButtons.add(0, lastButton); // 將最後一個按鈕移到第一個位置
            updateButtons(); // 更新按鈕的圖像
        }
    }
    private void updateButtons() {
        for (int i = 0; i < leftSideButtons.size(); i++) {
            leftSideButtons.get(i).setImageResource(drawableIds[i]);
            leftSideButtons.get(i).setTag(drawableIds[i]); // 同步更新 Tag
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
        menu.findItem(R.id.action_mode).setVisible(false);
        menu.findItem(R.id.action_background).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_analyze) {
            startActivity(new Intent(ModeActivity.this, AnalyzeActivity.class));
            return true;
        } else if (id == R.id.action_history) {
            startActivity(new Intent(ModeActivity.this, HistoryActivity.class));
            return true;
        } else if (id == R.id.action_changename) {
            Intent intent = new Intent(ModeActivity.this, ChangenameActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME);
            return true;
        } else if (id == R.id.action_background) {
            startActivity(new Intent(ModeActivity.this, BackgroundActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
