package com.example.stu.glego22;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * plots a fractal surface 1x1x1 wide
 * Created by Stu on 8/28/2015.
 */
public class Surface extends Primitive{


    private final List<FloatBuffer> mColourBuffer = new ArrayList<>();  /** Buffer holding the colours. */
    private final List<FloatBuffer> mVertexBuffer = new ArrayList<>();   /** Buffer holding the vertices. */
    private final List< ByteBuffer>  mIndexBuffer = new ArrayList<>();    // Buffer for index-array

    /** The vertices for the sphere. */
    private final List<float[]> mVertices = new ArrayList<>();
    private final List<float[]> mColors = new ArrayList<>();
    private final List<byte[]> mIndices = new ArrayList<>();
    /** Each vertex is a 2D coordinate. */

    private static final int NUM_FLOATS_PER_VERTEX = 3;
    private static final int NUM_COLOURS_PER_VERTEX = 4;
    private static final int INDICES_PER_GRIDPOINT = 6;
    private static final int NUM_VERTEX_PER_GRIDPOINT = 2;


    private static int extents= 20; // size of grid

    // private float[] colors =new float[20*4*2];
    // private byte[] indices = new byte[20*6]; // two triangles per step

    public Surface(){
        super();
        float max =400;

        float startx=-2f;
        float startz=-2f;
        float scalex=4f/(float)this.extents;
        float scalez=4f/(float)this.extents;
        Log.i("Surface","Extents:"+extents+" Scale:"+scalex);
        float x,y,z;
        float step =4f/(float)this.extents;
        int xi,yi,zi=0;
        for(xi=0;xi<extents;xi++){
            final float[] vertices = new float[extents * NUM_VERTEX_PER_GRIDPOINT * NUM_FLOATS_PER_VERTEX];
            final float[] colours = new float[extents*4*2];
            final byte[] indices=new byte[extents*6/2];
            int vertexcount=0;
            int colourcount=0;
            for(zi=0;zi<extents;zi++){
             //   Log.i("Surface", " Strip "+xi+" Vertex:"+vertexcount+" z:"+zi+" Scale:"+scalex );
                x=startx+(float)xi*scalex;
                z=startz+(float)zi*scalez;
                y=(float) Mandlebrot.mandel(x,z,max);
                String sx=String.valueOf(scalex);
                Log.i("Surface"," Building - x:"+x+" y:"+y+" z:"+z+" Scale:"+sx);
            // add next  vertices to strip
            vertices[vertexcount]=x;
            vertexcount++;
            vertices[vertexcount]=y;
            vertexcount++;
            vertices[vertexcount]=z;
            vertexcount++;
            colours[colourcount]=0f; // red
            colourcount++;
            colours[colourcount]=y/max; // G
            colourcount++;
            colours[colourcount]=0.5f; // B
            colourcount++;
            colours[colourcount]=1.0f; // A
            colourcount++;

            vertices[vertexcount]=x;
            vertexcount++;
            vertices[vertexcount]=y;
            vertexcount++;
            vertices[vertexcount]=z+step;
            vertexcount++;
            // colour in the vertex
            colours[colourcount]=0f; // red
            colourcount++;
            colours[colourcount]=y/max; // G
            colourcount++;
            colours[colourcount]=0.5f; // red
            colourcount++;
            colours[colourcount]=1.0f; // A
            colourcount++;
        }
        // build indices
        int indicescount=0;
        for(int p=0;p<extents;p+=2){
            indices[indicescount]=(byte)p;
            indicescount++;
            indices[indicescount]=(byte)(p+1);
            indicescount++;
            indices[indicescount]=(byte)(p+2);
            indicescount++;
            indices[indicescount]=(byte)(p+1);
            indicescount++;
            indices[indicescount]=(byte)(p+2);
            indicescount++;
            indices[indicescount]=(byte)(p+3);
            indicescount++;
        }
        // finished the strip so add it to our lists
            this.mVertices.add(vertices);
            this.mIndices.add(indices);
            this.mColors.add(colours);

    }

        // finished surface is now in array lists
        for (int row=0;row<extents;row++) {
            // put the vertices
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(extents * NUM_FLOATS_PER_VERTEX * NUM_VERTEX_PER_GRIDPOINT * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            FloatBuffer fb = byteBuffer.asFloatBuffer();
            fb.put(this.mVertices.get(row));
            fb.position(0);
            this.mVertexBuffer.add(fb);
            // put the colours
            byteBuffer = ByteBuffer.allocateDirect(extents * NUM_COLOURS_PER_VERTEX * NUM_VERTEX_PER_GRIDPOINT * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            fb = byteBuffer.asFloatBuffer();
            fb.put(this.mColors.get(row));
            fb.position(0);
            this.mColourBuffer.add(fb);
            // put the indices
            byteBuffer = ByteBuffer.allocateDirect(mIndices.get(row).length);
            //byteBuffer = ByteBuffer.allocateDirect(extents * INDICES_PER_GRIDPOINT * Byte.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            byteBuffer.put(this.mIndices.get(row));
            byteBuffer.position(0);
            this.mIndexBuffer.add(byteBuffer);
        }
}

    // Draw the shape
    public void draw(GL10 gl) {
        gl.glPushMatrix();
        // gl.glLoadIdentity();
        // locate us in space
        this.locate(gl);
        gl.glFrontFace(GL10.GL_CCW);  // Front face in counter-clockwise orientation

        // Enable arrays and define their buffers

        for (int strip=0;strip<this.extents;strip++) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0,this.mVertexBuffer.get(strip));
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0,this.mColourBuffer.get(strip));
           // gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, this.mVertices.get(strip).length / NUM_FLOATS_PER_VERTEX);

            gl.glDrawElements(GL10.GL_TRIANGLES, this.extents*INDICES_PER_GRIDPOINT/2, GL10.GL_UNSIGNED_BYTE,
                   this.mIndexBuffer.get(strip));
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }


        gl.glPopMatrix();
    }
}
