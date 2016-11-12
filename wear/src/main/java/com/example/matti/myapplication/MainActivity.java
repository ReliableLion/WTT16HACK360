package com.example.matti.myapplication;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setAmbientEnabled(); enable if ambient mode is needed

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);
        updateDisplay(); // comment this if ambient mode is needed
    }


    /*  enable if ambient mode is on
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }
    */
    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    @Override /* KeyEvent.Callback, called when a gesture is detected by android wear os */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                Toast.makeText(getApplicationContext(), "NAVIGATE_NEXT!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                Toast.makeText(getApplicationContext(), "NAVIGATE_PREV!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_IN:
                Toast.makeText(getApplicationContext(), "NAVIGATE_IN!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_OUT:
                Toast.makeText(getApplicationContext(), "NAVIGATE_OUT!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "ANYTHING ELSE!", Toast.LENGTH_SHORT).show();
                return super.onKeyDown(keyCode, event);
        }
    }
}
