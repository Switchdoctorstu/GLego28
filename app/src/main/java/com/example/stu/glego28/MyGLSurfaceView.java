package com.example.stu.glego28;

/**
 * Created by Stu on 23/07/2015.
 */
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
/*
 * Custom GL view by extending GLSurfaceView so as
 * to override event handlers such as onKeyUp(), onTouchEvent()
 */
public class MyGLSurfaceView extends GLSurfaceView {
    MyGLRenderer renderer;    // Custom GL Renderer

    // For touch event
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;
    private float previousX;
    private float previousY;

    // Constructor - Allocate and set the renderer
    public MyGLSurfaceView(Context context) {
        super(context);
        renderer = new MyGLRenderer(context);
        this.setRenderer(renderer);
        // Request focus, otherwise key/button won't react
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    // Handler for key event
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:   // Decrease Y-rotational speed
                renderer.speedY -= 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  // Increase Y-rotational speed
                renderer.speedY += 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:     // Decrease X-rotational speed
                renderer.speedX -= 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   // Increase X-rotational speed
                renderer.speedX += 0.1f;
                break;
            case KeyEvent.KEYCODE_A:           // Zoom out (decrease z)
                renderer.z -= 0.2f;
                break;
            case KeyEvent.KEYCODE_Z:           // Zoom in (increase z)
                renderer.z += 0.2f;
                break;
        }
        return true;  // Event handled
    }

    // Handler for touch event
    @Override
    public boolean onTouchEvent(final MotionEvent evt) {
        float currentX = evt.getX();
        float currentY = evt.getY();
        float deltaX, deltaY;
        int pointerCount = evt.getPointerCount();

        String actionString;

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) evt.getX(i);
            int y = (int) evt.getY(i);
            int id = evt.getPointerId(i);
            int action = evt.getActionMasked();
            int actionIndex = evt.getActionIndex();
            actionString="Initialising";

            switch (evt.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // Modify rotational angles according to movement
                    deltaX = currentX - previousX;
                    deltaY = currentY - previousY;
                    renderer.angleX += deltaY * TOUCH_SCALE_FACTOR;
                    renderer.angleY += deltaX * TOUCH_SCALE_FACTOR;
                    actionString = "MOVE";
                    break;
                case MotionEvent.ACTION_DOWN:
                    actionString = "DOWN";
                    break;
                case MotionEvent.ACTION_UP:
                    actionString = "UP";
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    actionString = "PNTR DOWN";
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    actionString = "PNTR UP";
                    break;

                default:
                    actionString = "";

            }
            String touchStatus = "Action: " + actionString + " Index: "
                    + actionIndex + " ID: " + id + " X: " + x + " Y: " + y;
            renderer.inputHandler.checkcontrollerballs(action, actionIndex,id,x,y);
            // so we have a pointer action



        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;
        return true;  // Event handled
    }
}
