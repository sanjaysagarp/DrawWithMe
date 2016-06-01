package edu.uw.nerd.drawwithme;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
//    private Uri[] uriList;
    private Integer[] uriList;
    private File[] fileList;
    private String TAG = "ImageAdapter";

//    public ImageAdapter(Context c, Uri[] uriList) {
//        mContext = c;
//        this.uriList = uriList;
//
//    }
    public ImageAdapter(Context c, Integer[] uriList) {
        mContext = c;
        this.uriList = uriList;

    }

    public ImageAdapter(Context c, File[] fileList) {
        mContext = c;
        this.fileList = fileList;

    }

    public int getCount() {
        if(uriList!=null) {
            return uriList.length;
        }
        else{
            Log.v(TAG, "length: " + fileList.length);
            return fileList.length;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int width = mContext.getResources().getDisplayMetrics().widthPixels / 2;
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        if(uriList != null) {
            imageView.setImageResource(uriList[position]);
        }
        else{
            imageView.setImageURI(Uri.parse(fileList[position].getAbsolutePath()));
        }
//        imageView.setImageURI(uriList[position]);
        return imageView;
    }

    public void setFileList(File[] files){
        fileList = files;
    }

    // references to images
//    File[] fileList = MainActivity.dir.listFiles();
//    Uri[] uriList = new Uri[fileList.length];

//    private Integer[] mThumbIds = MainActivity.dir.listFiles();
}