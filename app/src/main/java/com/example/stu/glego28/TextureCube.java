package com.example.stu.glego28;

/**
 * Created by Stu on 23/07/2015.
 */
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
/*
 * A cube with texture.
 * Define the vertices for only one representative face.
 * Render the cube by translating and rotating the face.
 */
public class TextureCube extends Primitive {
    Context context;
    private FloatBuffer vertexBuffer; // Buffer for vertex-array
    private FloatBuffer texBuffer;    // Buffer for texture-coords-array (NEW)

    private float[] vertices = { // Vertices for a face
            -1f, -1f, 0.0f,  // 0. left-bottom-front
            1f, -1f, 0.0f,  // 1. right-bottom-front
            -1f, 1f, 0.0f,  // 2. left-top-front
            1f, 1f, 0.0f   // 3. right-top-front
    };

    float[] texCoords = { // Texture coords for the above face (NEW)
            0.0f, 1.0f,  // A. left-bottom (NEW)
            1.0f, 1.0f,  // B. right-bottom (NEW)
            0.0f, 0.0f,  // C. left-top (NEW)
            1.0f, 0.0f   // D. right-top (NEW)
    };

    /**
     * The texture pointer.
     */
    private final int[] mTextures = new int[1];

    // Constructor - Set up the buffers
    public TextureCube() {
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        super();
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

    /**
     * Load the texture for the shape.
     *
     * @param texID GL texture identifier from texture manager
     */
    void setMygltextureid(int texID) {
        mTextures[0] = texID;
    }

    // Draw the shape
    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.mTextures[0]);
        // front
        gl.glPushMatrix();
        gl.glTranslatef(this.translation.Vx, this.translation.Vy, this.translation.Vz);   // Translate into the screen
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate y

        gl.glScalef(this.scale.Vx, this.scale.Vy, this.scale.Vz);      // Scale down

        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // left
        gl.glPushMatrix();
        gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // back
        gl.glPushMatrix();
        gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // right
        gl.glPushMatrix();
        gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // top
        gl.glPushMatrix();
        gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // bottom
        gl.glPushMatrix();
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

}