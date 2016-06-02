package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DANG on 6/2/2016.
 */
public class PostTask extends AsyncTask<Bitmap, Void, String> {

    public static final String TAG = "PostTask";
    public static final String CLIENT_ID = "76c244ffc9d4c9c";

    @Override
    protected String doInBackground(Bitmap... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String imageLink = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            params[0].compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();

            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
            String data = URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(encodedImage, "UTF-8");

            URL url = new URL("https://api.imgur.com/3/image");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data);
            wr.flush();


            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            // grab each line from the input stream and append onto the buffer

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            // implies there's nothing to read to begin with
            if (buffer.length() == 0) {
                return null;
            }

            imageLink = buffer.toString();

            try {
                // finds the url in the given json and add it onto list of url images
                String resultParsed = new JSONObject(imageLink).getJSONObject("data").getString("link");
                imageLink = resultParsed;
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
        return imageLink;
    }


    // sends the image off once computed into base64
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
