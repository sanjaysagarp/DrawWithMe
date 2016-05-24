package edu.uw.nerd.drawwithme;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by DANG on 5/23/2016.
 */
// In this case, the fragment displays simple text based on the page
public class InboxFragment extends Fragment {
    public static final String INBOX_PAGE = "INBOX_PAGE";

    private File dir;

    public static InboxFragment newInstance(File dir) {
        Bundle args = new Bundle();

        args.putSerializable(INBOX_PAGE, dir);
        InboxFragment fragment = new InboxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_inbox, container, false);
        dir = (File) getArguments().getSerializable(INBOX_PAGE);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);


//        File root = new File(dir, "draw_received");
//        if(!root.exists()){
//            root.mkdirs();
//        }
//        dir = root;


//        File[] fileList = dir.listFiles();
//        Uri[] uriList = new Uri[fileList.length];
//        for (int i = 0; i < fileList.length; i++) {
//            uriList[i] = Uri.fromFile(fileList[i]);
//        }

        Integer[] uriList = {R.drawable.derp, R.drawable.kitty, R.drawable.reply_after_right};

        gridview.setAdapter(new ImageAdapter(container.getContext(), uriList));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.v("TESTING", "lelelel");
            }
        });

        return rootView;
    }
}