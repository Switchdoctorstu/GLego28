package com.example.stu.glego28;

/**
 * Created by Stu on 23/07/2015.
 */
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
 * Render a sphere.
 *
 * @author Jim Cornmell
 * @since July 2013
 */
class Sphere extends Primitive {
    /** Maximum allowed depth. */
    private static final int MAXIMUM_ALLOWED_DEPTH = 5;

    /** Used in vertex strip calculations, related to properties of a icosahedron. */
    private static final int VERTEX_MAGIC_NUMBER = 5;

    /** Each vertex is a 2D coordinate. */
    private static final int NUM_FLOATS_PER_VERTEX = 3;

    /** Each texture is a 2D coordinate. */
    private static final int NUM_FLOATS_PER_TEXTURE = 2;

    /** Each vertex is made up of 3 points, x, y, z. */
    private static final int AMOUNT_OF_NUMBERS_PER_VERTEX_POINT = 3;

    /** Each texture point is made up of 2 points, x, y (in reference to the texture being a 2D image). */
    private static final int AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT = 2;

    /** Buffer holding the vertices. */
    private final List<FloatBuffer> mVertexBuffer = new ArrayList<>();

    /** The vertices for the sphere. */
    private final List<float[]> mVertices = new ArrayList<>();

    /** Buffer holding the texture coordinates. */
    private final List<FloatBuffer> mTextureBuffer = new ArrayList<>();

    /** The texture pointer. */
    private final int[] mTextures = new int[1];

    /** Total number of strips for the given depth. */
    private final int mTotalNumStrips;
    private float tx;



    /**
     * Sphere constructor.
     * @param depth integer representing the split of the sphere.
     * @param radius The spheres radius.
     */
    public Sphere(final float radius, final int depth ) {
        super();
        // Clamp depth to the range 1 to MAXIMUM_ALLOWED_DEPTH;
        final int d = Math.max(1, Math.min(MAXIMUM_ALLOWED_DEPTH, depth));

        // Calculate basic values for the sphere.
        this.mTotalNumStrips = Maths.power(2, d - 1) * VERTEX_MAGIC_NUMBER;
        final int numVerticesPerStrip = Maths.power(2, d) * 3;
        final double altitudeStepAngle = Maths.ONE_TWENTY_DEGREES / Maths.power(2, d);
        final double azimuthStepAngle = Maths.THREE_SIXTY_DEGREES / this.mTotalNumStrips;
        double x, y, z, h, altitude, azimuth;
        final List<float[]> texture = new ArrayList<>();

        for (int stripNum = 0; stripNum < this.mTotalNumStrips; stripNum++) {
            // Setup arrays to hold the points for this strip.
            final float[] vertices = new float[numVerticesPerStrip * NUM_FLOATS_PER_VERTEX];
            final float[] texturePoints = new float[numVerticesPerStrip * NUM_FLOATS_PER_TEXTURE];
            int vertexPos = 0;
            int texturePos = 0;

            // Calculate position of the first vertex in this strip.
            altitude = Maths.NINETY_DEGREES;
            azimuth = stripNum * azimuthStepAngle;

            // Draw the rest of this strip.
            for (int vertexNum = 0; vertexNum < numVerticesPerStrip; vertexNum += 2) {
                // First point - Vertex.
                y = radius * Math.sin(altitude);
                h = radius * Math.cos(altitude);
                z = h * Math.sin(azimuth);
                x = h * Math.cos(azimuth);
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;

                // First point - Texture.
                texturePoints[texturePos++] = (float) (1 - azimuth / Maths.THREE_SIXTY_DEGREES);
                texturePoints[texturePos++] = (float) (1 - (altitude + Maths.NINETY_DEGREES) / Maths.ONE_EIGHTY_DEGREES);

                // Second point - Vertex.
                altitude -= altitudeStepAngle;
                azimuth -= azimuthStepAngle / 2.0;
                y = radius * Math.sin(altitude);
                h = radius * Math.cos(altitude);
                z = h * Math.sin(azimuth);
                x = h * Math.cos(azimuth);
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;

                // Second point - Texture.
                texturePoints[texturePos++] = (float) (1 - azimuth / Maths.THREE_SIXTY_DEGREES);
                texturePoints[texturePos++] = (float) (1 - (altitude + Maths.NINETY_DEGREES) / Maths.ONE_EIGHTY_DEGREES);

                azimuth += azimuthStepAngle;
            }

            this.mVertices.add(vertices);
            texture.add(texturePoints);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(numVerticesPerStrip * NUM_FLOATS_PER_VERTEX * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            FloatBuffer fb = byteBuffer.asFloatBuffer();
            fb.put(this.mVertices.get(stripNum));
            fb.position(0);
            this.mVertexBuffer.add(fb);

            // Setup texture.
            byteBuffer = ByteBuffer.allocateDirect(numVerticesPerStrip * NUM_FLOATS_PER_TEXTURE * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            fb = byteBuffer.asFloatBuffer();
            fb.put(texture.get(stripNum));
            fb.position(0);
            this.mTextureBuffer.add(fb);
        }
        // set initial position and rotation


    }


    /**
     * Load the texture for the shape.
     *
     * @param texID GL texture identifier from texture manager
     */
    void setGLtextureid(int texID){
        mTextures[0]=texID;
    }

    /**
     * The draw method for the square with the GL context.
     *
     * @param gl Graphics handle.
     */
    public void draw(final GL10 gl) {
        // bind the previously generated texture.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.mTextures[0]);
        gl.glPushMatrix();
        // gl.glLoadIdentity();
        gl.glTranslatef(this.translation.Vx,this.translation.Vy, this.translation.Vz);   // Translate into the screen
        gl.glRotatef(this.rotation.Vx, 1.0f, 0.0f, 0.0f); // Rotate x
        gl.glRotatef(this.rotation.Vy, 0.0f, 1.0f, 0.0f); // Rotate y
        gl.glRotatef(this.rotation.Vz, 0.0f, 0.0f, 1.0f); // Rotate y

        gl.glScalef(this.scale.Vx, this.scale.Vy, this.scale.Vz);      // Scale down
        // Point to our buffers.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Set the face rotation, clockwise in this case.
        gl.glFrontFace(GL10.GL_CW);

        // Point to our vertex buffer.
        for (int i = 0; i < this.mTotalNumStrips; i++) {
            gl.glVertexPointer(AMOUNT_OF_NUMBERS_PER_VERTEX_POINT, GL10.GL_FLOAT, 0, this.mVertexBuffer.get(i));
            gl.glTexCoordPointer(AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GL10.GL_FLOAT, 0, this.mTextureBuffer.get(i));

            // Draw the vertices as triangle strip.
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.mVertices.get(i).length / AMOUNT_OF_NUMBERS_PER_VERTEX_POINT);
        }

        // Disable the client state before leaving.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // reset the matrix
        gl.glPopMatrix();
    }
}
