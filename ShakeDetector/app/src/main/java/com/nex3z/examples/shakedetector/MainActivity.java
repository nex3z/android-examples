package com.nex3z.examples.shakedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final long TIME_THRESHOLD = 500;
    private static final long SPEED_THRESHOLD = 200;
    private static final float FORCE_THRESHOLD = 2.0f;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long mLastUpdate;
    private float mLastX;
    private float mLastY;
    private float mLastZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long current = System.currentTimeMillis();
        long diff = current - mLastUpdate;
        if (diff > TIME_THRESHOLD) {
            mLastUpdate = current;
            float speed = Math.abs(x + y + z - mLastX - mLastY - mLastZ) / diff * 10000;
            if (speed > SPEED_THRESHOLD) {
                Log.v(LOG_TAG, "onSensorChanged(): speed = " + speed);
            }
            mLastX = x;
            mLastY = y;
            mLastZ = z;
        }

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;
        float force = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        if (force > FORCE_THRESHOLD) {
            Log.v(LOG_TAG, "onSensorChanged(): x = " + x + ", y = " + y + ", z = " + z +
                    ", gF = " + force);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
