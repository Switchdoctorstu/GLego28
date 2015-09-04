package com.example.stu.glego22;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Creates an environment of primitives
 *
 * Created by Stu on 9/1/2015.
 */
public class Sea extends Primitive {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private FloatBuffer colorBuffer;   // Buffer for color-array
    private ShortBuffer indexBuffer;    // Buffer for index-array
    private int numOfIndecies;
    RegularPolygon p;

    public Sea() {
        int sides = 6;
        float r=1f;
        float g=0f;
        float b=0.5f;
        p = new RegularPolygon(0, 0, 0, // x,y,z of the origin
                1,  // Length of the radius
                sides, // how many sides
                r,g,b);

            p.setTranslation(new Vector3f(2f,0f,-4));
        }

    public void draw(GL10 gl) {
        p.draw(gl);

    }

}
