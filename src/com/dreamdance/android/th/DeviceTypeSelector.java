package com.dreamdance.android.th;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioButton;
import com.dreamdance.th.R;

/**
 * Created with IntelliJ IDEA.
 * User: dongkai
 * Date: 12-5-21
 * Time: 下午9:21
 * To change this template use File | Settings | File Templates.
 */
public class DeviceTypeSelector extends Activity {
    private Configuration config;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_type_selector);
        checkResolution();

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // Show dialog and end
            DialogFactory.createExternalStorageDialog(this, true).show();

        } else {
            preferences = getSharedPreferences("config", MODE_PRIVATE);

            if (!preferences.getBoolean("select_device_type", true)) {
                finish();
                startActivity(new Intent(this, SDLActivity.class));
            }

            config = Configuration.loadFromPreferences(this, preferences);

            RadioButton phoneRadio = (RadioButton) findViewById(R.id.phoneRadio);
            phoneRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    config.selectConfigForPhone();
                }
            });

            RadioButton padRadio = (RadioButton) findViewById(R.id.padRadio);
            padRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    config.selectConfigForPad();
                }
            });

            if (config.getDeviceType().equals("Phone")) {
                phoneRadio.setChecked(true);
            } else if (config.getDeviceType().equals("Pad")) {
                padRadio.setChecked(true);
            } else { // Have no init value, first launch.
                phoneRadio.setChecked(true);
                config.selectConfigForPhone();
            }

            Button play = (Button) findViewById(R.id.play_button);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    config.saveToPreferences(DeviceTypeSelector.this, preferences);
                    finish();
                    DeviceTypeSelector.this.startActivity(new Intent(
                            DeviceTypeSelector.this, SDLActivity.class));
                }
            });
            final AlphaAnimation flickerAnimation = new AlphaAnimation(1.0f, 0.4f);
            flickerAnimation.setDuration(250);
            flickerAnimation.setRepeatCount(5);
            flickerAnimation.setRepeatMode(Animation.REVERSE);
            play.startAnimation(flickerAnimation);
        }
    }

    void checkResolution() {
        if (!Utility.isSuitableResolution(this)) {
            Dialog dialog = DialogFactory.createResolutionDialog(this);
            dialog.show();
        }
    }
}
