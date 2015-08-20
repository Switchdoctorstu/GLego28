package com.example.stu.glego28;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Stu on 8/17/2015.
 */
public class ControllerBall extends Sphere{
    Point2D downat,upat,move;
    boolean down;
    int action, actionIndex,id;
    Point2D point;
    Point2D previous;
    Boolean camregistered;
    Camera linkedCamera;

    public ControllerBall(float radius, int depth) {
        super(radius, depth);
        point=downat=upat=move= new Point2D(0f,0f);
        previous = new Point2D(0f,0f);
        camregistered=false;
    }

    public void registerCamera(Camera cam){
        this.linkedCamera =cam;
        camregistered=true;
    }

    public void notify(int action,int actionIndex, int id, Point2D p){
        Log.i("ControllerBall ","Action"+action);
        this.action=action;
        this.actionIndex=actionIndex;
        this.id=id;
        this.point=p;
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                setMove(p.subtract(this.previous));
                //deltaY = currentY - previousY;
                if(camregistered){
                    // we've a camera linked to the control so tell it we've moved
                    linkedCamera.rotate(this.move);
                }
                //renderer.angleX += deltaY * TOUCH_SCALE_FACTOR;
                //renderer.angleY += deltaX * TOUCH_SCALE_FACTOR;
                //actionString = "MOVE";
                this.previous=p;
                break;
            case MotionEvent.ACTION_DOWN:
                //actionString = "DOWN";
                setDownat(p);
                setDown(true);
                break;
            case MotionEvent.ACTION_UP:
                //actionString = "UP";
                setDown(false);
                setUpat(p);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //actionString = "PNTR DOWN";
                setDown(true);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //actionString = "PNTR UP";
                setDown(false);
                break;
            default:
                //actionString = "";
        }
    }

    public Point2D getDownat() {
        return downat;
    }

    public void setDownat(Point2D downat) {
        this.downat = downat;
    }

    public Point2D getUpat() {
        return upat;
    }

    public void setUpat(Point2D upat) {
        this.upat = upat;
    }

    public Point2D getMove() {
        return move;
    }

    public void setMove(Point2D move) {
        this.move = move;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
