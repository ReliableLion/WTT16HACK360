package com.example.matti.myapplication.sensor;

/**
 * Created by mario on 12/11/16.
 */

/**
 * Utility class used to check user's state based on acceleration value
 */
public class RealAccelData {

    public enum State {
        SITTING, FALLEN, STANDING, WALKING
    }

    private final int BUFF_SIZE = 50;
    private final double SIGMA =0.5, TH =10, TH1 =5, TH2 =2;

    private static RealAccelData instance = null;
    private double ax,ay,az;
    private double a_norm;
    private double[] window;
    private String curr_state,prev_state;

    private State state;

    public static RealAccelData getInstance() {
        if(instance == null) instance = new RealAccelData();
        return instance;
    }

    private RealAccelData() {
        window = new double[BUFF_SIZE];
        for(int i=0; i < BUFF_SIZE; i++) {
            window[i]=0;
        }
        prev_state="none";
        curr_state="none";
    }


    public void onSensorChanged(double x, double y, double z) {
        ax=x;
        ay=y;
        az=z;

        addData(ax,ay,az);

        posture_recognition(window, ay);

        systemState(curr_state,prev_state);

        if(!prev_state.equalsIgnoreCase(curr_state)) {
            prev_state=curr_state;
        }
    }

    private void posture_recognition(double[] window2, double ay2) {
        int zrc = compute_zrc(window2);

        if(zrc == 0) {
            if(Math.abs(ay2) < TH1) {
                curr_state="sitting";
            } else {
                curr_state="standing";
            }
        }
        else {
            if( zrc > TH2) {
                curr_state="walking";
            } else {
                curr_state="none";
            }
        }
    }

    private int compute_zrc(double[] window2) {

        int count=0;

        for(int i = 1; i <= BUFF_SIZE-1; i++) {
            if((window2[i]- TH) < SIGMA && (window2[i-1]- TH) > SIGMA) {
                count=count+1;
            }
        }

        return count;
    }

    private void systemState(String curr_state1,String prev_state1) {

        //Fall !!
        if(!prev_state1.equalsIgnoreCase(curr_state1)){
            if(curr_state1.equalsIgnoreCase("fall")){
                state = State.FALLEN;
            }
            if(curr_state1.equalsIgnoreCase("sitting")){
                state = State.SITTING;
            }
            if(curr_state1.equalsIgnoreCase("standing")){
                state = State.STANDING;
            }
            if(curr_state1.equalsIgnoreCase("walking")){
                state = State.WALKING;
            }
        }
    }

    private void addData(double ax2, double ay2, double az2) {
        a_norm=Math.sqrt(ax*ax+ay*ay+az*az);

        for(int i = 0; i <= BUFF_SIZE-2; i++) {
            window[i] = window[i+1];
        }

        window[BUFF_SIZE-1] = a_norm;
    }

    public State getState() {
        return state;
    }
}