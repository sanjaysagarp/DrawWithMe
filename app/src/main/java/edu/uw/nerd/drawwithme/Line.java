package edu.uw.nerd.drawwithme;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sanjaysagar on 5/22/16.
 */
public class Line {
    private Vector<Float> mPoints;
    private float mLastX = Float.NaN;
    private float mLastY = Float.NaN;
    private Paint style;

    public Line() {
        this.mPoints = new Vector<Float>();
        mLastY = Float.NaN;
        mLastX = Float.NaN;
        style = new Paint(Paint.ANTI_ALIAS_FLAG);
        style.setColor(Color.BLACK);
        style.setStyle(Paint.Style.STROKE);
        style.setStrokeWidth(1);
    }

    public void setStrokeWidth(int w) {
        style.setStrokeWidth(w);
    }

    public void setColor(int c) {
        style.setColor(c);
    }

    public Paint getPaint() {
        return style;
    }
    public void addPoint(float x, float y) {
        if (mLastX == Float.NaN || mLastY == Float.NaN) {
            mLastX = x;
            mLastY = y;
        } else {
            mPoints.add(mLastX);
            mPoints.add(mLastY);
            mPoints.add(x);
            mPoints.add(y);

            mLastX = x;
            mLastY = y;
        }
    }

    public Vector<Float> getPoints() {
        return mPoints;
    }

}
