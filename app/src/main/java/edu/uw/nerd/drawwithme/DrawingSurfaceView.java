package edu.uw.nerd.drawwithme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by sanjaysagar on 5/18/16.
 */
public class DrawingSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = "SurfaceView";

    private int viewWidth, viewHeight; //size of the view

    private SurfaceHolder mHolder; //the holder we're going to post updates to
    private DrawingRunnable mRunnable; //the code that we'll want to run on a background thread
    private Thread mThread; //the background thread

    public Pointer pointer;
    private Bitmap bmp;
    public List<Float> floatList;
    public float[] floatArray;
    public Paint defaultPaint;
    public int defaultBackground;
    public int width;

    public getDrawing callback;
    public ArrayList<Line> drawing;
    public DrawingSurfaceView(Context context) {
        this(context, null);
    }

    public DrawingSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingSurfaceView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        viewWidth = 1; viewHeight = 1; //positive defaults; will be replaced when #surfaceChanged() is called

        // register our interest in hearing about changes to our surface
        mHolder = getHolder();
        mHolder.addCallback(this);

        mRunnable = new DrawingRunnable();

        defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setStrokeWidth(width);
        defaultPaint.setColor(Color.BLACK);

        defaultBackground = Color.rgb(255,255,255);

        callback = (getDrawing) context;

        init();

    }

    public interface getDrawing {
        public ArrayList<Line> getDrawing();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //create and start the background updating thread
        Log.d(TAG, "Creating new drawing thread");
        mThread = new Thread(mRunnable);
        mRunnable.setRunning(true); //turn on the runner
        mThread.start(); //start up the thread when surface is created

        init();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        synchronized (mHolder) { //synchronized to keep this stuff atomic
            viewWidth = width;
            viewHeight = height;
            bmp = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888); //new buffer to draw on
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        mRunnable.setRunning(false); //turn off
        boolean retry = true;
        while(retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //will try again...
            }
        }
        Log.d(TAG, "Drawing thread shut down");
    }

    public void init(){
        //make ball
        pointer = new Pointer(20); // initialize radius on canvas creation
    }

    public void update(){
        //update the "game state" here (move things around, etc.)


    }

    public synchronized void render(Canvas canvas){
        if(canvas == null) return; //if we didn't get a valid canvas for whatever reason
        if(getBackground()!=null){
            canvas.drawBitmap(((BitmapDrawable)getBackground()).getBitmap(), 0, 0, null);
        }
        else {
            canvas.drawColor(defaultBackground); //white out the background
        }
        //need to redraw all previous points
        drawing = callback.getDrawing();
        for(int i = 0; i < drawing.size(); i++) {

            floatList = drawing.get(i).getPoints();
            floatArray = new float[floatList.size()];
            for(int j = 0; j < floatArray.length; j++) {
                floatArray[j] = (floatList.get(j) != null ? floatList.get(j) : Float.NaN);
            }
            canvas.drawLines(floatArray, drawing.get(i).getPaint());
        }

        if(pointer.isDown()) {
            canvas.drawCircle(pointer.cx, pointer.cy, pointer.radius, defaultPaint);
        }

    }

    public class DrawingRunnable extends Thread implements Runnable {

        private boolean isRunning; //whether we're running or not (so we can "stop" the thread)

        public void setRunning(boolean running){
            this.isRunning = running;
        }

        public void run() {
            Canvas canvas;
            Canvas bmc;
            while(isRunning)
            {
                canvas = null;
                try {
                    canvas = mHolder.lockCanvas(); //grab the current canvas
                    synchronized (mHolder) {
                        update(); //update the game
                        bmc = new Canvas();
                        bmc.setBitmap(bmp);
                        render(bmc); //redraw the screen on the buffer
                    }
                }
                finally { //no matter what (even if something goes wrong), make sure to push the drawing so isn't inconsistent
                    if (canvas != null) {
                        canvas.drawBitmap(bmp, 0, 0, null);
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }

                try {
                    sleep(80); //"refresh rate"
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }

    public Bitmap getBmp(){
        return bmp;
    }
}
