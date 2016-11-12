package com.example.matti.myapplication.sensor;

/**
 * Created by mario on 12/11/16.
 */

/**
 * A collection of static methods used to perform checks
 * on user events (fall, heart attack, etc)
 */
public class SensedEventsUtils {


    /**
     * Detect whether the user has fallen or not
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static boolean hasFallen(float x, float y, float z) {
        RealAccelData realAccelData = RealAccelData.getInstance();
        realAccelData.onSensorChanged(x, y, z);
        return realAccelData.getState() == RealAccelData.State.FALLEN;
    }

    /**
     * Detect whether or not the user has an heart attack
     * @param heartRate
     * @return
     */
    public static boolean hasHeartAttack(float heartRate) {
        if(heartRate == 0) // measurement is invalid
            return false;

        boolean walking = RealAccelData.getInstance().getState() == RealAccelData.State.WALKING;

        return (walking && heartRate >= 220) || (!walking && heartRate >= 120);
    }

    public static boolean hasFaint(float heartRate) {
        boolean walking = RealAccelData.getInstance().getState() == RealAccelData.State.WALKING;

        return (walking && heartRate < 60) || (!walking && heartRate < 30);
    }
}
