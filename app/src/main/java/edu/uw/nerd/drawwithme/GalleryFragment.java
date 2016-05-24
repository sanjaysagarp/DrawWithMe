package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by DANG on 5/23/2016.
 */
// In this case, the fragment displays simple text based on the page
public class GalleryFragment extends Fragment {
    public static final String GALLERY_PAGE = "GALLERY_PAGE";

    private int page;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gallery, container, false);


        return view;
    }
}