package com.example.danielprimo.imheredei;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

public class Mapa extends Activity implements SensorEventListener{
    private GLSurfaceView mGLView;
    private MapaConstructor cons_Map;
    public static float posX;
    public static float posY;

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mMagnetometer;
    ArrayList<Float> zList;
    TextView txt, txt2, txt3, txt4;
    int stepCounter;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private float orientation = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();

        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        zList = new ArrayList<>();
        stepCounter=0;
        posX = Float.parseFloat(intent.getStringExtra("POSITIONX"));
        posY = Float.parseFloat(intent.getStringExtra("POSITIONY"));

        //setContentView(R.layout.activity_mapa);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mGLView = new GLSurfaceView(this);
        cons_Map=new MapaConstructor(this,Float.toString(posX),Float.toString(posY));
        System.out.println("----------------------->Passei aqui!!!!-------------------");
        mGLView.setRenderer(cons_Map);
        System.out.println("----------------------->Passei aqui!!!!2222222-------------------");
        //mGLView.setKeepScreenOn(true);
        System.out.println("----------------------->Passei aqui!!!!333333333-------------------");
        setContentView(mGLView);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener( this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener( this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);


    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor source = event.sensor;
        /*txt = (TextView) findViewById(R.id.sensordata);
        txt2 = (TextView) findViewById(R.id.sensordata2);
        txt3 = (TextView) findViewById(R.id.sensordata3);
        txt4 = (TextView) findViewById(R.id.sensordata4);*/

        if (source.equals(mAccelerometer)) {
            zList.add(event.values[2]);
            if(zList.size()>2){
                if(zList.get(zList.size()-1)-zList.get(zList.size()-2)>1.2){
                    stepCounter++;
                    //txt3.setText("Step Counter: "+stepCounter);
                    //Calcular distancia percorrida
                    //if(mCurrentDegree < orientation-50 || mCurrentDegree > orientation+50)
                    orientation = mCurrentDegree;

                    posX += -1*Math.cos(orientation);
                    posY += -1*Math.sin(orientation);

                    //txt4.append("\n("+posx+","+posy+")");
                    MapaConstructor.posX=posX;
                    MapaConstructor.posY=posY;
                    ConstructionMap.posX=posX;
                    ConstructionMap.posY=posY;
                    //setContentView(mGLView);
                }

            }

            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
            //txt.setText("x: "+event.values[0]+"\ny: "+event.values[1]+"\nz: "+event.values[2]+"\n");
            System.out.println("x: "+event.values[0]+" | y: "+event.values[1]+" | z: "+event.values[2]);
        } else if (source.equals(mMagnetometer)) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            mCurrentDegree = azimuthInRadians;


        }
        //txt2.setText("orientation: "+mCurrentDegree);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
