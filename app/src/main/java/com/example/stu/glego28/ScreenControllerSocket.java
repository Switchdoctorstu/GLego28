package com.example.stu.glego28;

/**
 * Created by Stu on 8/18/2015.
 * Socket defines an area on the screen that can be assigned to a controller
 */
public class ScreenControllerSocket {
    Point2D lowPoint,highpoint;
    int id;

    public ScreenControllerSocket(Point2D lowBounds, Point2D highBounds) {
        this.lowPoint = lowBounds;
        this.highpoint = highBounds;
    }

    public Point2D getLowPoint() {
        return lowPoint;
    }

    public void setLowPoint(Point2D lowPoint) {
        this.lowPoint = lowPoint;
    }

    public Point2D getHighpoint() {
        return highpoint;
    }

    public void setHighpoint(Point2D highpoint) {
        this.highpoint = highpoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
