package edu.uw.nerd.drawwithme;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sanjaysagar on 5/22/16.
 */
public class Line {
    private Vector<Float> mPoints;
    private float mLastX = Float.NaN;
    private float mLastY = Float.NaN;

    public Line() {
        this.mPoints = new Vector<Float>();
        mLastY = Float.NaN;
        mLastX = Float.NaN;
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
