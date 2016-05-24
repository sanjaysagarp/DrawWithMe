package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

/**
 * Created by DANG on 5/23/2016.
 */
// In this case, the fragment displays simple text based on the page
public class GalleryFragment extends Fragment {
    public static final String GALLERY_PAGE = "GALLERY_PAGE";

    public static final String TAG = "GalleryFragment";

    private int page;

    private File dir;

    private View view;

    public static GalleryFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(GALLERY_PAGE, page);
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(GALLERY_PAGE);
    }

    @Override
    public void onResume() {
        if(view!=null){
            Log.v(TAG, "refreshing gallery");
            dir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File root = new File(dir, "Draw With Me");
            if(!root.exists()){
                root.mkdirs();
            }
            dir = root;

            File[] savedDrawings = dir.listFiles();
            ImageButton image = (ImageButton)view.findViewById(R.id.image_btn);
            if(savedDrawings.length != 0){
                image.setImageURI(Uri.parse(savedDrawings[0].getAbsolutePath()));
            }
            else{
                image.setBackgroundColor(Color.BLUE);
            }
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gallery, container, false);

        dir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        dir = new File(dir, "Draw With Me");
        if(!dir.exists()){
            dir.mkdirs();
        }

        File[] savedDrawings = dir.listFiles();
        ImageButton image = (ImageButton)view.findViewById(R.id.image_btn);
        if(savedDrawings.length != 0){
            Log.v(TAG, savedDrawings[0].toString());
            image.setImageURI(Uri.parse(savedDrawings[0].getAbsolutePath()));
        }
        else{
            image.setBackgroundColor(Color.BLUE);
        }

        return view;
    }
}