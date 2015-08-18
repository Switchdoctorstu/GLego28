package com.example.stu.glego28;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/16/2015.
 */
public class Torpedo extends Primitive{
    // Context context;   // Application's context

    Cylinder cl1;
    Sphere s1;
    Disc d1;
    int myglTextureID;

    public void setGLTextureID(int GLid){
        cl1.setMygltextureid(GLid);
        d1.setMygltextureid(GLid);
        s1.setGLtextureid(GLid);
        myglTextureID=GLid;

    }

    public Torpedo(){
       super();
        cl1 = new Cylinder(0.5f,0.5f,2,12);  // Hull
        Vertex3f cl1trans = new Vertex3f(0f,0f,0f);
        cl1.setTranslation(cl1trans);
        // cl1.setTy(0f);
        s1=new Sphere(0.5f,4);
        d1=new Disc(0.5f,6);

        s1.setTranslation(new Vertex3f(0f,0f,-1f));
        d1.setTranslation(new Vertex3f(0f,0f,1f));
    }

    public void setTexture(TextureManager tm, int resourceID){
        Log.i("loadTexture ", "Loading Submarine textures");
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
        d1.draw(gl);
        s1.draw(gl);
        // reset the matrix
        gl.glPopMatrix();

    }


}
