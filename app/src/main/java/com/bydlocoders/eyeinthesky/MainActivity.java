package com.bydlocoders.eyeinthesky;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        TextView text = (TextView)findViewById(R.id.hello);
        for (Sensor t: deviceSensors) {
            text.append(t.getName());
            text.append(":"+t.getType());
            text.append("\r\n");
        }
    }
}
