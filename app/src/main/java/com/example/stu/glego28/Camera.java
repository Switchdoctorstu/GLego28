package com.example.stu.glego28;
import android.util.Log;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/17/2015.
 */
public class Camera extends Primitive{
    Vertex3f target;
    Quaternion camquat;

    public Camera(Vertex3f pos, Vertex3f look, Vertex3f up){
        super();
        this.target=look;
        this.translation=pos;
        this.rotation=up;

    }

    public Vertex3f getTarget() {
        return target;
    }

    public void setTarget(Vertex3f look) {
        this.target = look;

    }


    public void rotate(Point2D turn){
        // we've been given a turn in the form of x,y =long,lat
        // need to convert to a quaternion then apply to camera position
        // as a rotation around look
        // look is centre of rotation
        // pos is radius

        // start with vector from pos to look
        Log.i("Camera" , "Turn X:"+turn.x+" Y:"+turn.y);

    }


}
