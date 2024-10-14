package com.example.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.content.SharedPreferences;



public class BackgroundActivity extends AppCompatActivity {

    private GridLayout buttonGroup1, buttonGroup2; // 左右两侧的 GridLayout
    private int leftScore;
    private int rightScore;

    // 手动追踪左侧按钮的行和列 (顺时针)
    private int[][] leftButtonPositions = {
            {0, 0}, {0, 1}, {1, 1}, {2, 1}, {2, 0}, {1, 0}
    };

    // 手动追踪右侧按钮的行和列 (顺时针)
    private int[][] rightButtonPositions = {
            {0, 0}, {0, 1}, {1, 1}, {2, 1}, {2, 0}, {1, 0}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        // 查找 Toolbar 并设置为 ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("後台模式");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(BackgroundActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // 結束當前 Activity
        });

        // 查找包含按钮的 GridLayout
        buttonGroup1 = findViewById(R.id.buttonGroup1); // 左侧按钮组
        buttonGroup2 = findViewById(R.id.buttonGroup2); // 右侧按钮组

        // 获取传递过来的分数
        Intent intent = getIntent();
        leftScore = intent.getIntExtra("leftScore", 0);  // 默认为 0
        rightScore = intent.getIntExtra("rightScore", 0);  // 默认为 0


        // 获取 TextView 用于显示分数
        TextView leftScoreTextView = findViewById(R.id.left_score_background);
        TextView rightScoreTextView = findViewById(R.id.right_score_background);
        // 包装分数变量到数组中，以便在内部类中修改
        final int[] leftScore = {0};  // 使用数组存储，以便在匿名类中修改
        final int[] rightScore = {0};
        leftScoreTextView.setText(String.valueOf(leftScore));
        rightScoreTextView.setText(String.valueOf(rightScore));

        // 获取主按钮
        ImageButton pointButton1 = findViewById(R.id.point_button1);
        ImageButton pointButton2 = findViewById(R.id.point_button2);
        ImageButton pointButton3 = findViewById(R.id.point_button3);
        ImageButton pointButton4 = findViewById(R.id.point_button4);
        ImageButton pointButton5 = findViewById(R.id.point_button5);
        ImageButton pointButton6 = findViewById(R.id.point_button6);

        // 立即展开主按钮（围绕中心按钮）
        List<ImageButton> pointButtons = Arrays.asList(pointButton1, pointButton2, pointButton3, pointButton4, pointButton5, pointButton6);
        expandMainButtons(pointButtons, 250);  // 半径为250的圆形布局

        // 设置每个主按钮的子按钮展开/收回
        setupPointButtonWithSubButtons(pointButton1, R.id.sub_button1_1, R.id.sub_button1_2);
        setupPointButtonWithSubButtons(pointButton2, R.id.sub_button2_1, R.id.sub_button2_2);
        setupPointButtonWithSubButtons(pointButton3, R.id.sub_button3_1, R.id.sub_button3_2);
        setupPointButtonWithSubButtons(pointButton4, R.id.sub_button4_1, R.id.sub_button4_2);
        setupPointButtonWithSubButtons(pointButton5, R.id.sub_button5_1, R.id.sub_button5_2);
        setupPointButtonWithSubButtons(pointButton6, R.id.sub_button6_1, R.id.sub_button6_2);

        // 获取centerButton
        ImageButton centerButton = findViewById(R.id.centerButton);
        ImageButton[] gridButtons = new ImageButton[12];

        // 获取gridButtons
        for (int i = 1; i <= 12; i++) {
            String buttonID = "button_grid_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            gridButtons[i - 1] = findViewById(resID);
        }

        // 创建一个点击监听器，点击后将对应的按钮图片设置到centerButton上，并处理加分
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的按钮的图片并设置到centerButton
                ImageButton clickedButton = (ImageButton) v;
                centerButton.setImageDrawable(clickedButton.getDrawable());
            }
        };

        // 为每个按钮设置点击监听器
        for (ImageButton gridButton : gridButtons) {
            gridButton.setOnClickListener(buttonClickListener);
        }

        // 获取所有子按钮
        ImageButton subButton1_1 = findViewById(R.id.sub_button1_1);
        ImageButton subButton1_2 = findViewById(R.id.sub_button1_2);
        ImageButton subButton2_1 = findViewById(R.id.sub_button2_1);
        ImageButton subButton2_2 = findViewById(R.id.sub_button2_2);
        ImageButton subButton3_1 = findViewById(R.id.sub_button3_1);
        ImageButton subButton3_2 = findViewById(R.id.sub_button3_2);
        ImageButton subButton4_1 = findViewById(R.id.sub_button4_1);
        ImageButton subButton4_2 = findViewById(R.id.sub_button4_2);

        // 为每个 sub_button 设置点击事件监听器
        setupSubButtonClickListener(subButton1_1, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton1_2, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton2_1, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton2_2, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton3_1, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton3_2, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton4_1, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
        setupSubButtonClickListener(subButton4_2, centerButton, gridButtons, leftScore, rightScore, leftScoreTextView, rightScoreTextView);
    }

    // 方法：展开主按钮
    private void expandMainButtons(List<ImageButton> buttons, float radius) {
        // 定义按钮的角度偏移（圆形排列），从顶部顺时针
        double[] angles = {0, 60, 120, 180, 240, 300}; // 每个按钮的角度

        // 立即展开主按钮
        for (int i = 0; i < buttons.size(); i++) {
            ImageButton button = buttons.get(i);
            button.setVisibility(View.VISIBLE);  // 确保按钮可见

            // 将角度转化为弧度
            double angleInRadians = Math.toRadians(angles[i]);

            // 根据半径和角度计算 X 和 Y 轴上的偏移量
            float translationX = (float) (radius * Math.cos(angleInRadians));
            float translationY = (float) (radius * Math.sin(angleInRadians));

            // Debug: 打印每个按钮的角度和偏移量
            System.out.println("Button " + (i + 1) + ": angle=" + angles[i] + "°, translationX=" + translationX + ", translationY=" + translationY);

            // 设置按钮的 X 和 Y 位置
            button.setTranslationX(translationX);
            button.setTranslationY(translationY);
        }
    }

    // 方法：为每个sub_button设置监听器
    private void setupSubButtonClickListener(ImageButton subButton, ImageButton centerButton, ImageButton[] gridButtons, int[] leftScore, int[] rightScore, TextView leftScoreTextView, TextView rightScoreTextView) {
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历所有 gridButtons，判断 centerButton 显示的图片是哪个 gridButton 的
                for (int i = 0; i < gridButtons.length; i++) {
                    if (centerButton.getDrawable().getConstantState().equals(gridButtons[i].getDrawable().getConstantState())) {
                        // 如果匹配到图片，判断是前6个还是后6个 gridButton
                        if (i < 6) {  // gridButton1 到 gridButton6
                            leftScore[0]++;
                            leftScoreTextView.setText(String.valueOf(leftScore[0]));
                        } else {  // gridButton7 到 gridButton12
                            rightScore[0]++;
                            rightScoreTextView.setText(String.valueOf(rightScore[0]));
                        }
                        break;
                    }
                }
            }
        });
    }

    // 方法：设置每个主按钮与它的子按钮，并控制点击后的显示/隐藏
    private void setupPointButtonWithSubButtons(ImageButton pointButton, int... subButtonIds) {
        // 获取子按钮列表
        List<ImageButton> subButtons = new ArrayList<>();
        for (int id : subButtonIds) {
            ImageButton subButton = findViewById(id);
            subButtons.add(subButton);
            subButton.setVisibility(View.GONE);  // 初始隐藏子按钮
        }

        // 监听点击主按钮
        pointButton.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;  // 用于跟踪是否展开子按钮

            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    // 展开子按钮
                    for (int i = 0; i < subButtons.size(); i++) {
                        ImageButton subButton = subButtons.get(i);
                        subButton.setVisibility(View.VISIBLE);

                        // 根据角度计算位移，假设每个子按钮相隔 45 度
                        double angle = Math.toRadians(45 * i);
                        float translationX = (float) (150 * Math.cos(angle));  // X 轴偏移
                        float translationY = (float) (150 * Math.sin(angle));  // Y 轴偏移

                        // 动画展开，基于主按钮的位置
                        subButton.animate()
                                .translationX(translationX + pointButton.getTranslationX())  // 相对于主按钮展开
                                .translationY(translationY + pointButton.getTranslationY())  // 相对于主按钮展开
                                .setDuration(300)
                                .start();
                    }
                } else {
                    // 收回子按钮
                    for (ImageButton subButton : subButtons) {
                        subButton.animate()
                                .translationX(pointButton.getTranslationX())  // 回到主按钮位置
                                .translationY(pointButton.getTranslationY())  // 回到主按钮位置
                                .setDuration(300)
                                .withEndAction(() -> subButton.setVisibility(View.GONE))  // 动画结束后隐藏
                                .start();
                    }
                }
                isExpanded = !isExpanded;  // 切换展开/收回状态
            }
        });
    }

    // 加载 Toolbar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_analyze) {
            Intent intent = new Intent(BackgroundActivity.this, AnalyzeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_history) {
            Intent intent = new Intent(BackgroundActivity.this, HistoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_changename) {
            Intent intent = new Intent(BackgroundActivity.this, ChangeTeamNameActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            // 接收從 ChangeTeamNameActivity 返回的隊伍名稱
            String teamAName = data.getStringExtra("teamAName");
            String teamBName = data.getStringExtra("teamBName");

            // 把隊伍名稱傳回 MainActivity
            Intent returnIntent = new Intent();
            returnIntent.putExtra("teamAName", teamAName);
            returnIntent.putExtra("teamBName", teamBName);
            setResult(Activity.RESULT_OK, returnIntent);
            finish(); // 關閉 BackgroundActivity
        }
    }

}
