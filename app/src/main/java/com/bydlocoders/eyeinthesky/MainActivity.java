package com.bydlocoders.eyeinthesky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(new CameraView(this));
        //    ((CameraView)findViewById(R.id.view)).SetDisplay((Button)findViewById(R.id.button));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void calibrate(View v) {
        ((HUDView) findViewById(R.id.view2)).Calibrate();
    }
}
