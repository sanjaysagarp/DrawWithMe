package edu.uw.nerd.drawwithme;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANG on 5/23/2016.
 */
// In this case, the fragment displays simple text based on the page
public class InboxFragment extends Fragment {
    public static final String TAG = "INBOX_FRAGMENT";
    public static final String INBOX_PAGE = "INBOX_PAGE";
    public static final String DETAIL_INTENT = "DETAIL_INTENT";

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

        ImageTask imgTask = new ImageTask();
        imgTask.execute("https://api.imgur.com/3/image/I5yPFUD");


        dir = (File) getArguments().getSerializable(INBOX_PAGE);
        GridView grid = (GridView) rootView.findViewById(R.id.gridview);

        // TODO: ideally should be Uri but issue with DrawingSurfaceView results in temp files...
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

        final Integer[] uriList = {R.drawable.derp, R.drawable.kitty, R.drawable.reply_after_right};

        grid.setAdapter(new ImageAdapter(container.getContext(), uriList));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.v("TESTING", "image has been clicked");
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DETAIL_INTENT, uriList[position]);
                startActivity(intent);
            }
        });

        return rootView;
    }



    private class ImageTask extends AsyncTask<String, Void, List<String>> {

        public static final String CLIENT_ID = "76c244ffc9d4c9c";
        public static final String CLIENT_SECRET = "8dde9266ec353e305169e658957c68099ec229d2";


        @Override
        protected List<String> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<String> images = new ArrayList<String>();

            // need to grab a list of all ID associated with this user............


            try {
                URL url = new URL(params[0]);

                // attempt to connect to the url
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);

                int status = urlConnection.getResponseCode();
                Log.v("STATUS", "" +status);

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }


                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader((inputStream)));

                // grab each line from the input stream and append onto the buffer
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }

                // implies there's nothing to read to begin with
                if (buffer.length() == 0) {
                    return null;
                }

                String results = buffer.toString();
                try {
                    JSONArray resultParsed = new JSONObject(results).getJSONArray("data");
                    for (int i = 0; i < resultParsed.length(); i++) {
                        JSONObject resultObj = resultParsed.getJSONObject(i);

                        Log.v("I WONDER", resultObj.toString());
//                        if (s.startsWith("link")) {
//                            results = s;
//                        }

                    }
                } catch (JSONException jsonEx) {
                    Log.v("ERROR", jsonEx.toString());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return images;
        }



        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            // take each string ID(?) of images and display em on the grid here
        }
    }


}