package edu.uw.nerd.drawwithme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DrawingSurfaceView.getDrawing {

    private static final String TAG = "Main";

    private DrawingSurfaceView view;
    private float x, y;
    private GestureDetector detector;
    private ArrayList<Line> drawing;
    private Line tempLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        inflater.inflate(R.menu.draw_menu, menu);
        return true;    }

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
