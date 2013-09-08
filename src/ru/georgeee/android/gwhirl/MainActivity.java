package ru.georgeee.android.gwhirl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import ru.georgeee.android.R;

/**
 * Main screen to choose single thread or another thread drawing model.
 */
public class MainActivity extends Activity {

    public static final String COLOR_COUNT_KEY = "color_count";
    public static final String SHOW_FPS_KEY = "show_fps";
    public static final String HEIGHT_KEY = "height";
    public static final String WIDTH_KEY = "width";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final Button actionButton = (Button) findViewById(R.id.actionButton);
        final CheckBox showFPSBox = (CheckBox) findViewById(R.id.showFPSBox);
        final SeekBar colorCountBar = (SeekBar) findViewById(R.id.colorCountBar);
        final EditText heightTextField = (EditText) findViewById(R.id.heightTextField);
        final EditText widthTextField = (EditText) findViewById(R.id.widthTextField);
        final TextView colorCountText = (TextView) findViewById(R.id.colorCountText);
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WhirlActivity.class);

                intent.putExtra(SHOW_FPS_KEY, showFPSBox.isChecked());
                intent.putExtra(COLOR_COUNT_KEY, colorCountBar.getProgress());
                intent.putExtra(HEIGHT_KEY, Integer.parseInt(heightTextField.getText().toString()));
                intent.putExtra(WIDTH_KEY, Integer.parseInt(widthTextField.getText().toString()));

                startActivity(intent);
            }
        });
        colorCountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                colorCountText.setText(String.valueOf(colorCountBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                colorCountText.setText(String.valueOf(colorCountBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                colorCountText.setText(String.valueOf(colorCountBar.getProgress()));
            }
        });

    }

}