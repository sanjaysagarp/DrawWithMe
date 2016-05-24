package edu.uw.nerd.drawwithme;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;

/**
 * Created by sanjaysagar on 5/20/16.
 */

public class SelectDrawingFragment extends Fragment{

    private String TAG = "SelectDrawing";

    private ImageButton[] drawings;

    private ImageButton createDrawingButton;

    public File dir;

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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new NewDrawingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Draw With Me");
        if(dir.exists()){
            File[] drawings = dir.listFiles();
            //load up images
        }

        return rootView;
    }
}
