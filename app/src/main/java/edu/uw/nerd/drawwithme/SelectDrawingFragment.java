package edu.uw.nerd.drawwithme;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by sanjaysagar on 5/20/16.
 */

public class SelectDrawingFragment extends Fragment{

    private String TAG = "SelectDrawing";

    private ImageButton[] drawings;

    private ImageButton createDrawingButton;

    public SelectDrawingFragment(){
        //Leave blank
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.select_drawing, container, false);

        createDrawingButton = (ImageButton) rootView.findViewById(R.id.newDrawing);

        createDrawingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Create new drawing button pressed!");

                //need to navigate to drawing page
            }
        });

        return rootView;
    }
}
