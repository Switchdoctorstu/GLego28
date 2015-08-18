package com.example.stu.glego28;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 23/07/2015.
 */
public class Submarine extends Primitive{
   // Context context;   // Application's context

    Cylinder cl1;
    Cylinder cl2;
    Sphere s1;
    Sphere s2;
    int myglTextureID;

    public void setGLTextureID(int GLid){
        cl1.setMygltextureid(GLid);
        cl2.setMygltextureid(GLid);
        s1.setGLtextureid(GLid);
        s2.setGLtextureid(GLid);
       myglTextureID=GLid;

    }

    public Submarine(){
      super();
        cl1 = new Cylinder(0.5f,0.5f,2,12);  // Hull
        Vertex3f cl1trans = new Vertex3f(0f,0f,0f);
        cl1.setTranslation(cl1trans);
       // cl1.setTy(0f);
        s1=new Sphere(0.5f,4);
        s2=new Sphere(0.5f,4);
        s1.setTranslation(new Vertex3f(0f,0f,-1f));
        s2.setTranslation(new Vertex3f(0f,0f,1f));

        cl2 = new Cylinder(0.25f,0.25f,0.5f,6); // tower
        Vertex3f cl2trans = new Vertex3f(0.5f,0.5f,0f);
        cl2.setTranslation(cl2trans);
        cl2.setRotation(new Vertex3f((float)Maths.NINETY_DEGREES,0f,0f));

    }

    public void setTexture(TextureManager tm, int resourceID){
        Log.i( "loadTexture ","Loading Submarine textures");
        Log.i("Texture = ", "ID:" + resourceID);
        int glid =tm.getTexID(resourceID);
        setGLTextureID(glid);
    }

    public void draw(GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(this.translation.Vx, this.translation.Vy, this.translation.Vz);   // Translate into the screen
        //gl.glTranslatef(0f,0f,-6f);
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate z

        gl.glScalef(this.scale.Vx, this.scale.Vy, this.scale.Vz);      // Scale
        cl1.draw(gl);
        cl2.draw(gl);
        s1.draw(gl);
        s2.draw(gl);
        // reset the matrix
        gl.glPopMatrix();

    }


}
