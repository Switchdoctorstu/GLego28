package com.example.stu.glego28;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 28/07/2015.
 */
public class Cylinder extends Primitive{

   private static final int NUM_FLOATS_PER_VERTEX = 3;// * Each vertex is a 3D coordinate.
    private static final int NUM_FLOATS_PER_TEXTURE = 2; // * Each texture is a 2D coordinate.
    private static final int AMOUNT_OF_NUMBERS_PER_VERTEX_POINT = 3;//* Each vertex is made up of 3 points, x, y, z.
    private static final int AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT = 2; //* Each texture point is made up of 2 points, x, y (in reference to the texture being a 2D image).

   private final List<FloatBuffer> mVertexBuffer = new ArrayList<>();// * Buffer holding the vertices.
   private final List<float[]> mVertices = new ArrayList<>(); // * The vertices for the cylinder.
    private final List<FloatBuffer> mTextureBuffer = new ArrayList<>();//  * Buffer holding the texture coordinates.

   private final int[] textureIDs = new int[1];//  * The texture pointer.

    /**
     * draws a cylinder with top diameter r1 , bottom diameter r2, and
     * height  and with texture coords.  The cylinder has its base
     * in the xy-plane and its axis along the positive z-axis.
     */
    public Cylinder( float r1,  float r2, float height, int slices) {
        super();
        if(slices<2){
            slices=2;
        }
        int totalvertices = slices * 6;
        final float[] vertices = new float[totalvertices * NUM_FLOATS_PER_VERTEX];
        final float[] texturePoints = new float[totalvertices * NUM_FLOATS_PER_TEXTURE];
        int vertexPos = 0;
        int texturePos = 0;
        double x,y;
        double twopi =  Maths.THREE_SIXTY_DEGREES;
        double r;
        double radperslice = twopi / slices;

        float topz=height/2;
        float basez=0-topz;

        // draw it

            for (r = 0; r < twopi; r +=  radperslice) {
                // 1 top point
                x=r1 * Math.sin(r);
                y=r1 * Math.cos(r);

                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] =  topz;
                texturePoints[texturePos++] = (float) 0;
                texturePoints[texturePos++] = (float) 1;
                // 2 bottom
                x=r2 * Math.sin(r);
                y=r2 * Math.cos(r);
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] =  basez;

                texturePoints[texturePos++] = (float) 0;
                texturePoints[texturePos++] = (float) 0;


                // 3 next bottom
                x=r2 * Math.sin(r+radperslice);
                y=r2 * Math.cos(r+radperslice);

                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) basez;

                texturePoints[texturePos++] = (float) 1;
                texturePoints[texturePos++] = (float) 0;
                // 4 top point
                x=r1 * Math.sin(r);
                y=r1 * Math.cos(r);

                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] =  topz;
                texturePoints[texturePos++] = (float) 0;
                texturePoints[texturePos++] = (float) 1;
                // 5 last up
                x=r1 * Math.sin(r+(radperslice));
                y=r1 * Math.cos(r+(radperslice));

                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] =  topz;
                texturePoints[texturePos++] = (float) 1;
                texturePoints[texturePos++] = (float) 1;

            }

        // now load the buffers
        final List<float[]> texture = new ArrayList<>();

            this.mVertices.add(vertices);
            texture.add(texturePoints);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(totalvertices * NUM_FLOATS_PER_VERTEX * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            FloatBuffer fb = byteBuffer.asFloatBuffer();
            fb.put(this.mVertices.get(0));
            fb.position(0);
            this.mVertexBuffer.add(fb);
            // Setup texture.
            byteBuffer = ByteBuffer.allocateDirect(totalvertices * NUM_FLOATS_PER_TEXTURE * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            fb = byteBuffer.asFloatBuffer();
            fb.put(texture.get(0));
            fb.position(0);
            this.mTextureBuffer.add(fb);

    }

    void setMygltextureid(int texID){
        textureIDs[0]=texID;
    }

    /**
     * The draw method for the cylinder with the GL context.
     *
     * @param gl Graphics handle.
     */
    public void draw(final GL10 gl) {

        gl.glPushMatrix();
        // gl.glLoadIdentity();                // Reset the model-view matrix
        gl.glTranslatef(this.translation.Vx,this.translation.Vy, this.translation.Vz);   // Translate into the screen
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate y

        gl.glScalef(this.scale.Vx, this.scale.Vy, this.scale.Vz);      // Scale down
        // bind the previously generated texture.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);

        // Point to our buffers.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Set the face rotation, clockwise in this case.
        gl.glFrontFace(GL10.GL_CW);

        // Point to our vertex buffer.
        gl.glVertexPointer(AMOUNT_OF_NUMBERS_PER_VERTEX_POINT, GL10.GL_FLOAT, 0, this.mVertexBuffer.get(0));
        gl.glTexCoordPointer(AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GL10.GL_FLOAT, 0, this.mTextureBuffer.get(0));

        // Draw the vertices as triangle strip.
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.mVertices.get(0).length / AMOUNT_OF_NUMBERS_PER_VERTEX_POINT);

        // Disable the client state before leaving.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // reset the matrix

        gl.glPopMatrix();
    }
}
