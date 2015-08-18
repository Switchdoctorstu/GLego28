package com.example.stu.glego28;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/17/2015.
 */
public class Camera {
    Vertex3f position,looking,rotation;

    public Camera(Vertex3f pos,Vertex3f look, Vertex3f rot){
        position=pos;
        looking=look;
        rotation=rot;
    }

    public Vertex3f getPosition() {
        return position;
    }

    public void setPosition(Vertex3f position) {
        this.position = position;
    }

    public Vertex3f getLooking() {
        return looking;
    }

    public void setLooking(Vertex3f looking) {
        this.looking = looking;
    }

    public Vertex3f getRotation() {
        return rotation;
    }

    public void setRotation(Vertex3f rotation) {
        this.rotation = rotation;
    }

    void point(GL10 gl){

        gl.glTranslatef(this.position.Vx,this.position.Vy, this.position.Vz);   // Translate into the screen
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate y
    }
}
