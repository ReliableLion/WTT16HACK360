package com.example.matti.myapplication.sensor;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class SensingService extends IntentService implements GoogleApiClient.ConnectionCallbacks {

    /**
     * Heart Rate sensor listener
     */
    private SensorEventListener mHeartRateListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float rate = sensorEvent.values[0];

            // Send information to the connected node
            requestSensing(wtt.wtt16hack.Utils.floatToByte(rate), heartRateNodeId, HEART_RATE_SENSING_CAPABILITY);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    /**
     * Accelerometer sensor listener
     */
    private SensorEventListener mAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float accX = sensorEvent.values[0];
            float accY = sensorEvent.values[1];
            float accZ = sensorEvent.values[2];

            // Send information to the connected node
            byte[] x = wtt.wtt16hack.Utils.floatToByte(accX);
            byte[] y = wtt.wtt16hack.Utils.floatToByte(accY);
            byte[] z = wtt.wtt16hack.Utils.floatToByte(accZ);

            byte[] payload = new byte[3*x.length];

            for(int i = 0; i < payload.length; i++) {
                if(i < 3) payload[i] = x[i];
                if(i >= 3 && i < 6) payload[i] = y[i % y.length];
                if(i >= 6 && i < 9) payload[i] = z[i % (2*z.length)];
            }

            requestSensing(payload, accelerometerNodeId, ACCELEROMETER_SENSING_CAPABILITY);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private static final String ACCELEROMETER_SENSING_CAPABILITY = "acceleration_sensing";
    private static final String HEART_RATE_SENSING_CAPABILITY = "heart_rate_sensing";

    // Nodes (smartphone) to which we need to send the sensed values
    private String accelerometerNodeId = null;
    private String heartRateNodeId = null;

    private GoogleApiClient mGoogleApiClient;

    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;
    private Sensor mAccelerometer;

    public SensingService() {
        super("SensingService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize connection with smartphone
        initGoogleApiClient();

        // Register sensor listeners
        if(mHeartRateSensor != null)
            mSensorManager.registerListener(mHeartRateListener, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        if(mAccelerometer != null)
            mSensorManager.registerListener(mAccelerometerListener, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mHeartRateSensor != null)
            mSensorManager.unregisterListener(mHeartRateListener);
        if(mAccelerometer != null)
            mSensorManager.unregisterListener(mAccelerometerListener);
    }

    /**
     * Initialize Google Api Client
     */
    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        accelerometerNodeId = setupSensingCapability(ACCELEROMETER_SENSING_CAPABILITY);
        heartRateNodeId = setupSensingCapability(HEART_RATE_SENSING_CAPABILITY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private String setupSensingCapability(String capability) {
        CapabilityApi.GetCapabilityResult result =
                Wearable.CapabilityApi.getCapability(
                        mGoogleApiClient, capability,
                        CapabilityApi.FILTER_REACHABLE).await();

        return updateCapabilities(result.getCapability());
    }

    private String updateCapabilities(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();

        return pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    private void requestSensing(byte[] data, String transcriptionNodeId, String capability) {
        if (transcriptionNodeId != null) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, transcriptionNodeId,
                    capability, data).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if(!sendMessageResult.getStatus().isSuccess()) {
                                // There's been an error
                            }
                        }
                    }
            );
        } else {
            // Unable to retrieve node with transcription capability
        }
    }

}
