package ru.georgeee.android.gwhirl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 07.09.13
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
public class WhirlView extends View {
    public static final int DEFAULT_HEIGHT = 320;
    public static final int DEFAULT_WIDTH = 240;
    public static final int DEFAULT_COLOR_COUNT = 15;
    private static final long FPS_CALC_INTERVAL = 1000L;
    public boolean showFPS = true;
    protected int matrixHeight;
    protected int matrixWidth;
    protected int colorPlaceCount;
    protected int[] colors;
    protected int[][] colorMap;
    protected int[][] colorMapCopy;
    protected int[] compiledColors;
    protected Bitmap bitmap;
    protected Paint paint;
    protected long lastFpsCalcUptime;
    protected long frameCounter;
    protected float fps;
    protected float avgFps = 0;
    protected int measureCount = 0;


    public WhirlView(Context context, int colorCount, int width, int height) {
        super(context);
        Random rand = new Random(System.currentTimeMillis());
        colorPlaceCount = height * width;
        this.matrixWidth = width;
        this.matrixHeight = height;
        colors = new int[colorCount];
        for (int i = 0; i < colors.length; ++i) {
            colors[i] = rand.nextInt(0xffffff + 1);
        }
        colorMap = new int[height][width];
        colorMapCopy = new int[height][width];
        compiledColors = new int[colorPlaceCount];
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < colorMap.length; ++i) {
            for (int j = 0; j < colorMap[i].length; ++j) {
                colorMap[i][j] = rand.nextInt(colors.length);
            }
        }
        paint = new Paint();
        prepareFpsCounter();
        stepColors();
    }


    public WhirlView(Context context, int colorCount) {
        this(context, colorCount, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public WhirlView(Context context) {
        this(context, DEFAULT_COLOR_COUNT);
    }

    public int getMatrixHeight() {
        return matrixHeight;
    }

    public int getMatrixWidth() {
        return matrixWidth;
    }

    protected void stepColors() {
        int i, j, i0, j0, j2, i5, next_color;
        i = j = 0;
        for (int _colorPlace = 0; _colorPlace < colorPlaceCount; ++_colorPlace, ++j) {
            if (j == matrixWidth) {
                j = 0;
                ++i;
            }
                /*
                0 1 2
                3 x 4
                5 6 7
                 */
            i0 = i == 0 ? matrixHeight - 1 : i - 1;
            j0 = j == 0 ? matrixWidth - 1 : j - 1;
            j2 = j == matrixWidth - 1 ? 0 : j + 1;
            i5 = i == matrixHeight - 1 ? 0 : i + 1;
            next_color = colorMap[i][j] + 1;
            if (next_color == colors.length) next_color = 0;
            if (colorMap[i0][j0] == next_color || colorMap[i0][j] == next_color
                    || colorMap[i0][j2] == next_color || colorMap[i][j0] == next_color
                    || colorMap[i][j2] == next_color || colorMap[i5][j0] == next_color
                    || colorMap[i5][j] == next_color || colorMap[i5][j2] == next_color) {
                colorMapCopy[i][j] = next_color;
                compiledColors[_colorPlace] = colors[next_color];
            }
        }
        int[][] x = colorMap;
        colorMap = colorMapCopy;
        colorMapCopy = x;
        bitmap.setPixels(compiledColors, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        frameCounter++;
        long now = SystemClock.uptimeMillis();
        long delta = now - lastFpsCalcUptime;
        if (delta > FPS_CALC_INTERVAL) {
            fps = (float) frameCounter * FPS_CALC_INTERVAL / delta;
            avgFps = (avgFps * measureCount + fps) / (++measureCount);
            frameCounter = 0;
            lastFpsCalcUptime = now;
            Log.i("GWhirl.FPS", "Fps=" + fps + " Average_fps=" + avgFps);
        }
        stepColors();
        float sX = (float) canvas.getWidth() / matrixWidth;
        float sY = (float) canvas.getHeight() / matrixHeight;
        canvas.scale(sX, sY);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.scale((float) 1 / sX, (float) 1 / sY);
        if (showFPS) {
            paint.setTextSize(30);
            paint.setARGB(127, 0, 0, 0);
            canvas.drawRect(0, 0, 300, 100, paint);
            paint.setARGB(255, 255, 255, 255);
            canvas.drawText("fps=" + (float) Math.round(1000 * fps) / 1000, 10, 40, paint);
            canvas.drawText("average fps=" + (float) Math.round(1000 * avgFps) / 1000, 10, 85, paint);
        }
        postInvalidate();
    }

    protected void prepareFpsCounter() {
        lastFpsCalcUptime = SystemClock.uptimeMillis();
        frameCounter = 0;
    }
}
