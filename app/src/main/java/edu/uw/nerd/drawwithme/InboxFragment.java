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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        dir = (File)getArguments().get(INBOX_PAGE);

        ImageTask imgTask = new ImageTask();
        imgTask.execute("https://api.imgur.com/3/image/I5yPFUD");


//        GridView grid = (GridView) rootView.findViewById(R.id.gridview);
//
//        // TODO: ideally should be Uri but issue with DrawingSurfaceView results in temp files...
////        File root = new File(dir, "draw_received");
////        if(!root.exists()){
////            root.mkdirs();
////        }
////        dir = root;
//
//
////        File[] fileList = dir.listFiles();
////        Uri[] uriList = new Uri[fileList.length];
////        for (int i = 0; i < fileList.length; i++) {
////            uriList[i] = Uri.fromFile(fileList[i]);
////        }
//
//
//
//
//        final Integer[] uriList = {R.drawable.derp, R.drawable.kitty, R.drawable.reply_after_right};
//
//        grid.setAdapter(new ImageAdapter(container.getContext(), uriList));
//
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Log.v("TESTING", "image has been clicked");
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra(DETAIL_INTENT, uriList[position]);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }



    private class ImageTask extends AsyncTask<String, Void, List<Bitmap>> {

        public static final String CLIENT_ID = "76c244ffc9d4c9c";
        public static final String CLIENT_SECRET = "8dde9266ec353e305169e658957c68099ec229d2";

        @Override
        protected List<Bitmap> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<Bitmap> images = new ArrayList<Bitmap>();

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
                    // finds the url in the given json and add it onto list of url images
                    String resultParsed = new JSONObject(results).getJSONObject("data").getString("link");
                    Log.v("did i ?", resultParsed.toString());

                    URL imgLink = new URL(resultParsed);
                    Bitmap img = BitmapFactory.decodeStream(imgLink.openConnection().getInputStream());
                    images.add(img);

                } catch (JSONException jsonEx) {
                    Log.v("ERROR", jsonEx.toString());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            finally {
                // after reading from api, disconnect the url
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        // close the Buffer reader
                        reader.close();
                    } catch(IOException e) {
                        // reader isn't null but cannot be closed..
                    }
                }
            }

            return images;
        }


        // loads images once they have been grabbed
        protected void onPostExecute(List<Bitmap> result) {
            super.onPostExecute(result);
            // take each string url of images and display em on the grid here
            Log.v("bmp list: ", result.toString());
            GridView grid = (GridView) getActivity().findViewById(R.id.gridview);

            // TODO: ideally should be Uri but issue with DrawingSurfaceView results in temp files...

//        File[] fileList = dir.listFiles();
//        Uri[] uriList = new Uri[fileList.length];
//        for (int i = 0; i < fileList.length; i++) {
//            uriList[i] = Uri.fromFile(fileList[i]);
//        }
            try {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                File file = new File(dir, "drawing_" + timestamp);

    //            if(title == null) {
    //                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    //                file = new File(dir, "drawing_" + timestamp);
    //            }
    //            else{
    //                file = new File(dir, title);
    //            }
                file.createNewFile();

                try {

                    // show loop through and do it for all
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    result.get(0).compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            } catch (IOException io) {
                Log.d(TAG, Log.getStackTraceString(io));
            }

            for (int i = 0; i < result.size(); i++) {

            }

            //final Integer[] uriList = {R.drawable.derp, R.drawable.kitty, R.drawable.reply_after_right};
            final File[] fileList = dir.listFiles();
//            Uri[] uriList = new Uri[fileList.length];
//            for (int i = 0; i < fileList.length; i++) {
//                uriList[i] = Uri.fromFile(fileList[i]);
//            }
            grid.setAdapter(new ImageAdapter(getContext(), fileList));

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Log.v("TESTING", "image has been clicked");
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(DETAIL_INTENT, fileList[position]);
                    startActivity(intent);
                }
            });




        }
    }


}