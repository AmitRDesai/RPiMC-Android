package com.rpimc.hari.rpimc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MotionControl extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    SensorManager sm;
    Button button;
    TextView status;
    boolean st = false;
    CommandSender commandSender;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_control);
        status = (TextView) findViewById(R.id.motion_text);
        button = (Button) findViewById(R.id.motion_button);
        pb = (ProgressBar) findViewById(R.id.motion_progressBar);
        button.setOnClickListener(this);
        commandSender = new CommandSender(Logo.con);
    }

    @Override
    public void onClick(View v) {
        if (!st) {
            st = true;
            pb.setVisibility(View.VISIBLE);
            button.setText("stop");
            status.setText("Recognizing");
            sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
                Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
            }
        } else {
            sm.unregisterListener(this);
            commandSender.send(new Command(0, 0, 0, 0, 1));
            st = false;
            pb.setVisibility(View.INVISIBLE);
            button.setText("start");
            status.setText("Stopped");
        }
    }

    @Override
    public void onBackPressed() {
        try {
            sm.unregisterListener(this);
        } catch (Exception e) {
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            sm.unregisterListener(this);
        } catch (Exception e) {
        }
        commandSender.send(new Command(0, 0, 0, 0, 1));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0], y = event.values[1];
        if (x < -5) {
            commandSender.send(new Command(0, 0, 0, 1, 0));
        } else if (x > 5) {
            commandSender.send(new Command(0, 0, 1, 0, 0));
        } else if (y < -5) {
            commandSender.send(new Command(1, 0, 0, 0, 0));
        } else if (y > 5) {
            commandSender.send(new Command(0, 1, 0, 0, 0));
        } else {
            commandSender.send(new Command(0, 0, 0, 0, 1));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
