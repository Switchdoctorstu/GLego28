package com.example.stu.glego28;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 28/07/2015.
 */
public class Cylinder {

    /**
     * Maximum allowed depth.
     */
    private static final int MAXIMUM_ALLOWED_DEPTH = 5;

    /**
     * Used in vertex strip calculations, related to properties of a icosahedron.
     */
    private static final int VERTEX_MAGIC_NUMBER = 5;

    /**
     * Each vertex is a 2D coordinate.
     */
    private static final int NUM_FLOATS_PER_VERTEX = 3;

    /**
     * Each texture is a 2D coordinate.
     */
    private static final int NUM_FLOATS_PER_TEXTURE = 2;

    /**
     * Each vertex is made up of 3 points, x, y, z.
     */
    private static final int AMOUNT_OF_NUMBERS_PER_VERTEX_POINT = 3;

    /**
     * Each texture point is made up of 2 points, x, y (in reference to the texture being a 2D image).
     */
    private static final int AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT = 2;

    /**
     * Buffer holding the vertices.
     */
    private final List<FloatBuffer> mVertexBuffer = new ArrayList<>();

    /**
     * The vertices for the cylinder.
     */
    private final List<float[]> mVertices = new ArrayList<>();

    /**
     * Buffer holding the texture coordinates.
     */
    private final List<FloatBuffer> mTextureBuffer = new ArrayList<>();

    /**
     * The texture pointer.
     */
    private final int[] mTextures = new int[1];

    /**
     * Total number of strips for the given depth.
     */
   private int mTotalNumStrips;


    /**
     * Calls uvCylinder(gl,0.5,1,32,10,5,true); to draw a cylinder with diameter and
     * height both equal to 1 and with texture coords.  The cylinder has its base
     * in the xy-plane and its axis along the positive z-axis.
     */
    public Cylinder(GL10 gl, float r1,  float r2, float height, int slices) {
        if(slices<2){slices=2;}
        final int numVerticesPerStrip=slices*2;
        final float[] vertices = new float[numVerticesPerStrip * NUM_FLOATS_PER_VERTEX];
        final float[] texturePoints = new float[numVerticesPerStrip * NUM_FLOATS_PER_TEXTURE];
        int vertexPos = 0;
        int texturePos = 0;
        double x,y,z,r;
        float twopi = (float) Maths.THREE_SIXTY_DEGREES;
        // ok so we have to draw 3 parts
        float radperslice = twopi / slices;

        int stripnum = 0; // number of strips created
        mTotalNumStrips=0;

        // if top is bigger than zero then draw it
        if(r1!=0) {


            for (r = 0; r < twopi; r += 2* (twopi / slices)) {
                // outer point
                x=r1 * Math.sin(r);
                y=r1 * Math.cos(r);
                z=height;
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;
                texturePoints[texturePos++] = (float) 0.5;
                texturePoints[texturePos++] = (float) 1;
                //centre
                vertices[vertexPos++] = (float) 0;
                vertices[vertexPos++] = (float) 0;
                vertices[vertexPos++] = (float) height;

                texturePoints[texturePos++] = (float) 0.5;
                texturePoints[texturePos++] = (float) 0.5;


                // next outer
                x=r1 * Math.sin(r+radperslice);
                y=r1 * Math.cos(r+radperslice);
                z=height;
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;

                texturePoints[texturePos++] = (float) 0.75;
                texturePoints[texturePos++] = (float) 0.75;

                // next outer
                x=r1 * Math.sin(r+(2*radperslice));
                y=r1 * Math.cos(r+(2*radperslice));
                z=height;
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;
                texturePoints[texturePos++] = (float) 0.5;
                texturePoints[texturePos++] = (float) 1;
            }
        mTotalNumStrips++;
        }
        // Cylinder


        // Bottom


        final List<float[]> texture = new ArrayList<>();

        for (int thisstrip = 0; thisstrip < stripnum; thisstrip++) {

            this.mVertices.add(vertices);
            texture.add(texturePoints);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(numVerticesPerStrip * NUM_FLOATS_PER_VERTEX * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            FloatBuffer fb = byteBuffer.asFloatBuffer();
            fb.put(this.mVertices.get(thisstrip));
            fb.position(0);
            this.mVertexBuffer.add(fb);

            // Setup texture.
            byteBuffer = ByteBuffer.allocateDirect(numVerticesPerStrip * NUM_FLOATS_PER_TEXTURE * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            fb = byteBuffer.asFloatBuffer();
            fb.put(texture.get(thisstrip));
            fb.position(0);
            this.mTextureBuffer.add(fb);
        }



    }


    /**
     * Draw a cylinder with a given radius, number of slices, number of stacks, and
     * number of rings.  The number of slices is the number of divisions parallel to the
     * axis (like the slices of an orange).  The number of stacks is the number
     * of divisions perpendicular the axis.  If the number or rings is less than or
     * equal to zero, then the top and bottom caps are not drawn.  If the number of
     * rings is positive, then the top and bottom caps are drawn, and they are divided
     * radially into the specified number of rings for drawing; the number of axial
     * divisions of the caps is the same as the number of slices.  The last parameter
     * tells whether to generate texture coordinates for the cylinder.  The texture
     * will be wrapped once around the cylinder.  For the top an bottom caps, a circular
     * cutout from the texture is used.  The cylinder has its base in the xy-plane, with
     * center at (0,0,0), and its axis lies along the positive direction of the z-axis.
     */
    /*
    public static void uvCylinder(GL10 gl, double radius, double height,
                                  int slices, int stacks, int rings, boolean makeTexCoords) {
        if (radius <= 0)
            throw new IllegalArgumentException("Radius must be positive.");
        if (height <= 0)
            throw new IllegalArgumentException("Height must be positive.");
        if (slices < 3)
            throw new IllegalArgumentException("Number of slices must be at least 3.");
        if (stacks < 2)
            throw new IllegalArgumentException("Number of stacks must be at least 2.");
        for (int j = 0; j < stacks; j++) {
            double z1 = (height / stacks) * j;
            double z2 = (height / stacks) * (j + 1);
            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int i = 0; i <= slices; i++) {
                double longitude = (2 * Math.PI / slices) * i;
                double sinLong = Math.sin(longitude);
                double cosLong = Math.cos(longitude);
                double x = cosLong;
                double y = sinLong;
                gl.glNormal3d(x, y, 0);
                if (makeTexCoords)
                    gl.glTexCoord2d(1.0 / slices * i, 1.0 / stacks * (j + 1));
                gl.glVertex3d(radius * x, radius * y, z2);
                if (makeTexCoords)
                    gl.glTexCoord2d(1.0 / slices * i, 1.0 / stacks * j);
                gl.glVertex3d(radius * x, radius * y, z1);
            }
            gl.glEnd();
        }
        if (rings > 0) { // draw top and bottom
            gl.glNormal3d(0, 0, 1);
            for (int j = 0; j < rings; j++) {
                double d1 = (1.0 / rings) * j;
                double d2 = (1.0 / rings) * (j + 1);
                gl.glBegin(GL2.GL_QUAD_STRIP);
                for (int i = 0; i <= slices; i++) {
                    double angle = (2 * Math.PI / slices) * i;
                    double sin = Math.sin(angle);
                    double cos = Math.cos(angle);
                    if (makeTexCoords)
                        gl.glTexCoord2d(0.5 * (1 + cos * d1), 0.5 * (1 + sin * d1));
                    gl.glVertex3d(radius * cos * d1, radius * sin * d1, height);
                    if (makeTexCoords)
                        gl.glTexCoord2d(0.5 * (1 + cos * d2), 0.5 * (1 + sin * d2));
                    gl.glVertex3d(radius * cos * d2, radius * sin * d2, height);
                }
                gl.glEnd();
            }
            gl.glNormal3d(0, 0, -1);
            for (int j = 0; j < rings; j++) {
                double d1 = (1.0 / rings) * j;
                double d2 = (1.0 / rings) * (j + 1);
                gl.glBegin(GL2.GL_QUAD_STRIP);
                for (int i = 0; i <= slices; i++) {
                    double angle = (2 * Math.PI / slices) * i;
                    double sin = Math.sin(angle);
                    double cos = Math.cos(angle);
                    if (makeTexCoords)
                        gl.glTexCoord2d(0.5 * (1 + cos * d2), 0.5 * (1 + sin * d2));
                    gl.glVertex3d(radius * cos * d2, radius * sin * d2, 0);
                    if (makeTexCoords)
                        gl.glTexCoord2d(0.5 * (1 + cos * d1), 0.5 * (1 + sin * d1));
                    gl.glVertex3d(radius * cos * d1, radius * sin * d1, 0);
                }
                gl.glEnd();
            }
        }
    } // end uvCylinder

  */

    /**
     * Load the texture for the shape.
     *
     * @param gl Handle.
     * @param context Handle.
     * @param texture Texture map for the sphere.
     */
    public void loadGLTexture(final GL10 gl, final Context context, final int texture) {
        // Generate one texture pointer, and bind it to the texture array.
        gl.glGenTextures(1, this.mTextures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.mTextures[0]);

        // Create nearest filtered texture.
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap.
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    /**
     * The draw method for the cylinder with the GL context.
     *
     * @param gl Graphics handle.
     */
    public void draw(final GL10 gl) {
        // bind the previously generated texture.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.mTextures[0]);

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
    }
}
