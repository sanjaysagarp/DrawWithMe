package edu.uw.nerd.drawwithme;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Andre on 5/21/2016.
 */
public class NewDrawingFragment extends Fragment implements SensorEventListener{

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private String TAG = "NewDrawingFragment";

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.v(TAG, "New drawing fragment");
//        View rootView = inflater.inflate(R.layout.new_drawing, container, false);
//
//        return rootView;
//    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
