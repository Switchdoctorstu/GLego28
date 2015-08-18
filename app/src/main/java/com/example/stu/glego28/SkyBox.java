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

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/15/2015.
    skybox is different to texturecube as the texture pointers allow for different mappings.

 */
public class SkyBox extends Primitive{
    Context context;


    private FloatBuffer vertexBuffer; // Buffer for vertex-array
    private FloatBuffer texBuffer;    // Buffer for texture-coords-array (NEW)

    private float[] vertices = {  // Vertices of the 6 faces
            // FRONT
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            // BACK
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            // LEFT
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            // RIGHT
            1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            // TOP
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            // BOTTOM
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            1.0f, -1.0f,  1.0f   // 1. right-bottom-front
    };

    float[] texCoords = { // Texture coords for the above face (NEW)
            //front
            0.25f, 0.33f,  // A. left-bottom (NEW)
            0.5f, 0.33f,  // B. right-bottom (NEW)
            0.25f, 0.66f,  // C. left-top (NEW)
            0.5f, 0.66f,   // D. right-top (NEW)
            //back
            0.75f, 0.33f,  // A. left-bottom (NEW)
            1.0f, 0.33f,  // B. right-bottom (NEW)
            0.75f, 0.66f,  // C. left-top (NEW)
            1.0f, 0.66f,   // D. right-top (NEW)
            //left
            0.0f, 0.33f,  // A. left-bottom (NEW)
            0.25f, 0.33f,  // B. right-bottom (NEW)
            0.0f, 0.66f,  // C. left-top (NEW)
            0.25f, 0.66f,   // D. right-top (NEW)
            //right
            0.5f, 0.33f,  // A. left-bottom (NEW)
            0.75f, 0.33f,  // B. right-bottom (NEW)
            0.5f, 0.66f,  // C. left-top (NEW)
            0.75f, 0.66f,   // D. right-top (NEW)
            //top
            0.25f, 0.66f,  // A. left-bottom (NEW)
            0.5f, 0.66f,  // B. right-bottom (NEW)
            0.25f, 1.0f,  // C. left-top (NEW)
            0.5f, 1.0f,   // D. right-top (NEW)
            //bottom
            0.25f, 0.0f,  // A. left-bottom (NEW)
            0.5f, 0.0f,  // B. right-bottom (NEW)
            0.25f, 0.33f,  // C. left-top (NEW)
            0.5f, 0.33f   // D. right-top (NEW)
    };
    int[] textureIDs = new int[1];   // Array for 1 texture-ID (NEW)

    // Constructor - Set up the buffers
    public SkyBox() {
        super();
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        // Setup texture-coords-array buffer, in float. An float has 4 bytes (NEW)
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);

    }

    public void setGLtextureID(int glID){
        textureIDs[0]=glID;
    }

    // Draw the shape
    public void draw(GL10 gl) {
        // bind the previously generated texture.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textureIDs[0]);
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)

        // draw our strip
        gl.glPushMatrix();
        gl.glTranslatef(this.translation.Vx,this.translation.Vy, this.translation.Vz);   // Translate into the screen
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate y
        gl.glScalef(this.scale.Vx, this.scale.Vy, this.scale.Vz);      // Scale down

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 24);
        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
     //   gl.glDisable(GL10.GL_CULL_FACE);
    }


}
