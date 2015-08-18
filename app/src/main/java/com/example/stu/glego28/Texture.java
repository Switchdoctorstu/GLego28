package com.example.stu.glego28;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/14/2015.
 */
public class Texture {
    public int resourceID;
    public int textureID;
    public int width;
    public int height;
    /** The texture pointer. */
    private final int[] mTextures = new int[1];

    Texture(int resID){
        resourceID=resID;
        width=height=0;
    }

    // Load the texture based on the resourceID
    // returns the GL texture ID assigned
    int loadResource(GL10 gl,Context context){

        // Generate one texture pointer, and bind it to the texture array.
        gl.glGenTextures(1, this.mTextures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.mTextures[0]);

        // Create nearest filtered texture.
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap.
        //final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture);
        //GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        //bitmap.recycle();

        // Construct an input stream to texture image
        InputStream istream = context.getResources().openRawResource(resourceID);

        Bitmap bitmap;
        try {
            // Read and decode input as bitmap
            bitmap = BitmapFactory.decodeStream(istream);
        } finally {
            try {
                istream.close();
            } catch(IOException e) { }
        }

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

    textureID=mTextures[0];
    return textureID;
    }
}
