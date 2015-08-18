package com.example.stu.glego28;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/15/2015.
 */
public class Hud extends Primitive {
    // Context context;   // Application's context
    ControllerBall ctlrball1;
    ControllerBall ctlrball2;
    int myglTextureID;


    float nearZ,farZ,width,height;

    public Hud(InputHandler inputHandler){
        super();
        ctlrball1=new ControllerBall(1f,3);
        inputHandler.addcontrollerball(ctlrball1);
        ctlrball2=new ControllerBall(1f,3);
        inputHandler.addcontrollerball(ctlrball2);
        myglTextureID=0;
        nearZ= 0.1f;
        farZ=50f;
        width=100;
        height=100;
       // remap(); // position objects in space
    }

    public void setGLTextureid(int glid){
        ctlrball1.setGLtextureid(glid);
        ctlrball2.setGLtextureid(glid);
    }

    public void remap(){
        float aspect=this.width/this.height;
        float aspectdiv=aspect/400;
        float aspectS=0.05f;
        Log.i("HUD Map"," aspect"+aspect);
        ctlrball1.setTranslation(new Vertex3f(0.15f*aspect,-0.1f,-0.25f));
        ctlrball1.setScale(new Vertex3f(aspectS,aspectS,aspectS));
        ctlrball2.setTranslation(new Vertex3f(-0.15f*aspect,-0.1f,-0.25f));
        ctlrball2.setScale(new Vertex3f(aspectS,aspectS, aspectS));
    }

    public void draw(GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(this.translation.Vx,this.translation.Vy, this.translation.Vz);   // Translate into the screen
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate y
        gl.glScalef(this.scale.Vx, this.scale.Vy, this.scale.Vz);      // Scale down
        ctlrball1.draw(gl);
        ctlrball2.draw(gl);
        // reset the matrix
        gl.glPopMatrix();
    }

    public float getNearZ() {
        return nearZ;
    }

    public void setNearZ(float nearZ) {
        this.nearZ = nearZ;
        remap();
    }

    public float getFarZ() {
        return farZ;
    }

    public void setFarZ(float farZ) {
        this.farZ = farZ;
        remap();
    }

    public float getWidth() {
        return width;
    }



    public float getHeight() {
        return height;
    }

    public void setHW(float height,float width) {
        this.height = height;
        this.width=width;
        remap();
    }
}
