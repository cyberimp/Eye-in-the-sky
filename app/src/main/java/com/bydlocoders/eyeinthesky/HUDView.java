package com.bydlocoders.eyeinthesky;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kinzo on 18.06.2016.
 */
public class HUDView extends View implements SensorEventListener {
    Sensor magnetic;
    Sensor gravity;
    SensorManager mSensorManager;

    float[] mGravity;
    float[] mGeomagnetic;

    float[] start = new float[3];

    Paint greenPaint = new Paint();
    private float azimuth;
    private float pitch;
    private float roll;

    public HUDView(Context context) {
        super(context);
        initPaint(context);
    }


    public HUDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public HUDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    public void Calibrate() {
        start = new float[3];
        start[0] = azimuth;
        start[1] = pitch;
        start[2] = roll;
    }

    private void initPaint(Context context) {
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStyle(Paint.Style.STROKE);
        greenPaint.setAlpha(128);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravity = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawText((azimuth-start[0])+":"+(pitch-start[1])+":"+(roll-start[2]),100,100,greenPaint);
        greenPaint.setStyle(Paint.Style.STROKE);
        greenPaint.setAlpha(255);
        canvas.drawLine(100.0f, 100.0f, 100.0f + (float) (Math.cos((roll - start[2])) * 50),
                100.0f + (float) (Math.sin((roll - start[2])) * 50), greenPaint);
        canvas.drawLine(100.0f, 100.0f, 100.0f - (float) (Math.cos(roll - start[2]) * 50),
                100.0f - (float) (Math.sin(roll - start[2]) * 50), greenPaint);
        //canvas.drawCircle(100,100,50,greenPaint);
        canvas.drawRect(50f, 50f, 150f, 150f, greenPaint);
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setAlpha(128);
        //canvas.drawArc(50f,50f,150f,150f,);
        canvas.drawRect(50f, 100f - (float) (Math.sin(pitch - start[1]) * 50),
                150f, 150f, greenPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientationData[] = new float[3];
                float Rremapped[] = new float[9];
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, Rremapped);
                SensorManager.getOrientation(Rremapped, orientationData);
//                azimuth = (float) Math.round( Math.toDegrees(orientationData[0]));
//                pitch =  (float) Math.round(Math.toDegrees(orientationData[1]));
//                roll =  (float) Math.round(Math.toDegrees(orientationData[2]));
                azimuth = orientationData[0];
                pitch = orientationData[1];
                roll = orientationData[2];
            }
        }

        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
