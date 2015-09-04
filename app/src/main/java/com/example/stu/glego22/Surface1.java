package com.example.stu.glego22;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/31/2015.
 */
public class Surface1 extends Primitive{

    /** Buffer holding the vertices. */
    private final List<FloatBuffer> mVertexBuffer = new ArrayList<>();
    private final List<float[]> mVertices = new ArrayList<>();
    /** Buffer holding the colours. */
    private final List<FloatBuffer> mColourBuffer = new ArrayList<>();
    private final List<float[]> mColours = new ArrayList<>();
    /** Buffer holding the indices. */
    private final List<ByteBuffer> mIndicesBuffer = new ArrayList<>();
    private final List<Byte[]> mIndices = new ArrayList<>();

    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private FloatBuffer colorBuffer;   // Buffer for color-array
    private ByteBuffer indexBuffer;    // Buffer for index-array
    private float[][] grid;
    private static int GRIDSIZE=10;

    private float[] vertices = { // 5 vertices of the pyramid in (x,y,z)
            -0.5f, -0.5f, -0.5f,  // 0. left-bottom-back
            0.5f, -0.5f, -0.5f,  // 1. right-bottom-back
            0.5f, -0.5f,  0.5f,  // 2. right-bottom-front
            -0.5f, -0.5f,  0.5f,  // 3. left-bottom-front
            0.0f,  0.5f,  0.0f   // 4. top
    };

    private float[] colors = {  // Colors of the 5 vertices in RGBA
            0.0f, 0.0f, 1.0f, 1.0f,  // 0. blue
            0.0f, 1.0f, 0.0f, 1.0f,  // 1. green
            0.0f, 0.0f, 1.0f, 1.0f,  // 2. blue
            0.0f, 1.0f, 0.0f, 1.0f,  // 3. green
            1.0f, 0.0f, 0.0f, 1.0f   // 4. red
    };

    private byte[] indices = { // Vertex indices of the 4 Triangles
            2, 4, 3,   // front face (CCW)
            1, 4, 2,   // right face
            0, 4, 1,   // back face
            4, 0, 3    // left face
    };

    // Constructor - Set up the buffers
    public Surface1() {
        super();

        float max =400;

        float startx=-2f;
        float startz=-2f;
        float scalex=4f/(float)this.GRIDSIZE;
        float scalez=4f/(float)this.GRIDSIZE;
        Log.i("Surface", "Extents:" + GRIDSIZE + " Scale:" + scalex);
        float x,y,z;
        float step =4f/(float)this.GRIDSIZE;
        float[][] grid = new float[this.GRIDSIZE][this.GRIDSIZE];


// draw out the fractal set
        for(int xi=0;xi<this.GRIDSIZE;xi++){
            for(int zi=0;zi<this.GRIDSIZE;zi++) {
                x=startx+(float)xi*scalex;
                z=startz+(float)zi*scalez;
                y=(float) Mandlebrot.mandel(x,z,max);
                grid[xi][zi]=y;
                String sx=String.valueOf(scalex);
                Log.i("Surface"," Building - x:"+x+" y:"+y+" z:"+z+" Scale:"+sx);
                // set height as fractal value
                vertices[13]=y;
                // set colour to fractal value
                colors[16]=y/max; // R
                colors[17]=(float) 1f-(y/max); // G
                colors[18]=y/max; // B



                // set the x offset
                vertices[0]=(float) xi-1;
                vertices[3]=(float)xi+1;
                vertices[6]=(float)xi+1;
                vertices[9]=(float)xi-1;
                vertices[12]=(float)xi;
                // and the z offset
                vertices[2]=(float)zi-1;
                vertices[5]=(float)zi-1;
                vertices[8]=(float)zi+1;
                vertices[11]=(float)zi+1;
                vertices[14]=(float)zi;



                // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder()); // Use native byte order
            vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
            vertexBuffer.put(vertices);         // Copy data into buffer
            vertexBuffer.position(0);           // Rewind

            // Setup color-array buffer. Colors in float. An float has 4 bytes
            ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
            cbb.order(ByteOrder.nativeOrder());
            colorBuffer = cbb.asFloatBuffer();
            colorBuffer.put(colors);
            colorBuffer.position(0);

            // Setup index-array buffer. Indices in byte.
            indexBuffer = ByteBuffer.allocateDirect(indices.length);
            indexBuffer.put(indices);
            indexBuffer.position(0);
        
                // add arrays to final model
                mVertexBuffer.add(vertexBuffer);
                mColourBuffer.add(colorBuffer);
                mIndicesBuffer.add(indexBuffer);
            }

        }
    }

    // Draw the shape
    public void draw(GL10 gl) {
        gl.glPushMatrix();
        // gl.glLoadIdentity();
        // locate us in space
        this.locate(gl);
        gl.glFrontFace(GL10.GL_CCW);  // Front face in counter-clockwise orientation

        for(int xi=0;xi<mVertexBuffer.size();xi++) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
                // Enable arrays and define their buffers

                gl.glVertexPointer(3, GL10.GL_FLOAT, 0,this.mVertexBuffer.get(xi));

                gl.glColorPointer(4, GL10.GL_FLOAT, 0, this.mColourBuffer.get(xi));
                gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE,
                        this.mIndicesBuffer.get(xi));
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        }

        gl.glPopMatrix();
    }
}
