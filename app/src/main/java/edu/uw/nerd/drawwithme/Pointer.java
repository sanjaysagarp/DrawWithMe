package edu.uw.nerd.drawwithme;

/**
 * Created by sanjaysagar on 5/22/16.
 */

//Circular pointer!
public class Pointer {
    public float cx; //center
    public float cy;
    public float radius; //radius
    public boolean isDown;

    public Pointer(float radius) {
        this.radius = radius;
        this.isDown = false;
    }

    public Pointer(float cx, float cy, float radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.isDown = true;
    }

    public boolean isDown() { return isDown; }
    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius){
        this.radius = radius;
    }

    public void setXY(float cx, float cy){
        this.cx = cx;
        this.cy = cy;
        this.isDown = true;
    }

    public float getX(){
        return this.cx;
    }

    public float getY() {
        return this.cy;
    }


}
