package com.example.stu.glego28;

import android.content.Context;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 23/07/2015.
 */
public class Submarine {
    Context context;   // Application's context
    // global buffers

    FloatBuffer mVertexBuffer;
    FloatBuffer mTextureBuffer;
    FloatBuffer mNormalBuffer;
    ShortBuffer mIndices;

    public Submarine(Context context){
        Model model = new Model(R.raw.submarine1, context);

        mVertexBuffer = model.getVertices();
        mTextureBuffer = model.getTexCoords();
        mNormalBuffer = model.getNormals();
        mIndices = model.getIndices();

    }
    public void draw(GL10 gl) {

        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);

        gl.glEnable(GL10.GL_TEXTURE_2D); // workaround bug 3623
        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
        gl.glNormalPointer(1, GL10.GL_FIXED, mNormalBuffer);
        // gl.glColor4f(1, 1, 1, 1);
        // gl.glNormal3f(0, 0, 1);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, mVertexBuffer.limit());

    }


}
