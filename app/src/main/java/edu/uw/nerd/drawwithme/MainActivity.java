package edu.uw.nerd.drawwithme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements DrawingSurfaceView.getDrawing {
    private static final String TAG = "Main";

    private DrawingSurfaceView view;
    private float x, y;
    private GestureDetector detector;
    private ArrayList<Line> drawing;
    private Line tempLine;

    public File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File root = new File(dir, "Draw With Me");
        if(!root.exists()){
            root.mkdirs();
        }
        dir = root;

        view = (DrawingSurfaceView)findViewById(R.id.drawingView);

        detector = new GestureDetector(this, new MyGestureListener());
        tempLine = new Line();
        drawing = new ArrayList<Line>();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.container, new SelectDrawingFragment(), "SelectDrawingFragment");
//        ft.commit();

        x = view.pointer.getX();
        y = view.pointer.getY();

        ObjectAnimator xAnim = ObjectAnimator.ofFloat(view.pointer, "x", x);
        xAnim.setDuration(2000);
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(view.pointer, "y", y);
        yAnim.setDuration(3000);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(yAnim, xAnim);
        set.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.canvas_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_save:
                saveCanvas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private Uri drawingUri;
    //helper method for saving the current drawing
    private void saveCanvas(){
        if(isExternalStorageWriteable()){
            try {
                Log.v(TAG, "Saving.. hopefully..");
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File file = new File(dir, "drawing_" + timestamp);
                file.createNewFile();

                drawingUri = Uri.fromFile(file);

                view.setDrawingCacheEnabled(true);
                view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getWidth(), view.getHeight());

                view.buildDrawingCache(true);
                Bitmap bitmap = view.getDrawingCache();

                FileOutputStream stream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
            }
            catch(IOException io){
                Log.d(TAG, Log.getStackTraceString(io));
            }


            //set image to the picture
            //imageView.setImageURI(pictureUri);
        }
    }

    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY() - getSupportActionBar().getHeight();

        int action = MotionEventCompat.getActionMasked(event);


        switch(action) {
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG,"Touch!");
                // draw current point
                view.pointer.isDown = true;
                view.pointer.setXY(x,y);
                tempLine.addPoint(view.pointer.cx,view.pointer.cy);
                drawing.add(tempLine);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v(TAG, "Another finger!");
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                Log.v(TAG,"A finger left");
                return true;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "Last finger left");
                view.pointer.isDown = false;
                //drawing.add(tempLine);
                //refresh tempLine
                tempLine = new Line();
                return true;
            case (MotionEvent.ACTION_MOVE) : //move
                // draw current point
                view.pointer.isDown = true;
                view.pointer.setXY(x,y);
                tempLine.addPoint(view.pointer.cx,view.pointer.cy);
                drawing.add(tempLine);
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    @Override
    public ArrayList<Line> getDrawing() {
        return drawing;
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
            //return super.onDown(e);
        }

    }
}
