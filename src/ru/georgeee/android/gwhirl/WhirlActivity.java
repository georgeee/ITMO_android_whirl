package ru.georgeee.android.gwhirl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class WhirlActivity extends Activity {

    private WhirlSurfaceView mainView;
    private int height, width, colorCount;
    private boolean showFPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        showFPS = getIntent().getExtras().getBoolean(MainActivity.SHOW_FPS_KEY);
        height = getIntent().getExtras().getInt(MainActivity.HEIGHT_KEY);
        width = getIntent().getExtras().getInt(MainActivity.WIDTH_KEY);
        colorCount = getIntent().getExtras().getInt(MainActivity.COLOR_COUNT_KEY);
        mainView = new WhirlSurfaceView(this);
        setContentView(mainView);
    }

    public class WhirlSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private WhirlDrawThread drawThread;

        public WhirlSurfaceView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThread = new WhirlDrawThread(colorCount, width, height);
            drawThread.setSurfaceHolder(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
