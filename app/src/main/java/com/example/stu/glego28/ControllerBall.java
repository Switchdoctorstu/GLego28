package com.example.stu.glego28;

/**
 * Created by Stu on 8/17/2015.
 */
public class ControllerBall extends Sphere{
int px,py,mx,my,mz,dx,dy;
    boolean down;
    public ControllerBall(float radius, int depth) {
        super(radius, depth);

    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public int getMx() {
        return mx;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }

    public int getMz() {
        return mz;
    }

    public void setMz(int mz) {
        this.mz = mz;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
