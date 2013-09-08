package ru.georgeee.android.gwhirl;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class WhirlActivity extends Activity {

    private WhirlView mainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        boolean showFPS = getIntent().getExtras().getBoolean(MainActivity.SHOW_FPS_KEY);
        int height = getIntent().getExtras().getInt(MainActivity.HEIGHT_KEY);
        int width = getIntent().getExtras().getInt(MainActivity.WIDTH_KEY);
        int colorCount = getIntent().getExtras().getInt(MainActivity.COLOR_COUNT_KEY);

        mainView = new WhirlView(this, colorCount, width, height);
        mainView.showFPS = showFPS;
        setContentView(mainView);
    }

}
