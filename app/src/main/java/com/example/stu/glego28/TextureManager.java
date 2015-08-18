package com.example.stu.glego28;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLUtils;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Stu on 8/13/2015.
 * maps resouceIDs to loaded textureIDs
 */
public class TextureManager {


    private static int MAXTEXTURES = 6;

    private SparseArray<Texture> textureSparseArray;
    private int[][] textureMap;
    private static int state; // * The current state of the texture manager.
    private int pointer; // cursor for loaded textures
    boolean found;

    private int numtextures;

    TextureManager() {
        pointer = 0;
        numtextures = 0;
        textureMap = new int[MAXTEXTURES][3];  // 0 = resID, 1 = GLID , 2 = state
        state = STATE_EMPTY;
        clearMAP();
    }

    public static final int STATE_EMPTY = 0;// Indicates that t
    public static final int STATE_FRESH = 1;// Indicates that the texture manager has new textures but they are not loaded.
    public static final int STATE_LOADING = 2;//* Indicates that the texture manager is loading the textures.
    public static final int STATE_LOADED = 3;//* Indicates that the texture manager is unloading some or all of the textures.
    public static final int STATE_UNLOADING = 4;// * Indicates that the texture manager has finished loading the textures. In this state the textures are functional.

    public void add(int resID) {
        Log.i("TextureManager:", "Add " + resID);
        found = false;
        for (int j = 0; j < numtextures; j++) {
            if (textureMap[j][0] == resID) found = true;
        }
        if (found == false) {
            state = STATE_FRESH;
            // add new texture
            textureMap[pointer][0] = resID;
            textureMap[pointer][1] = 0; // no GLid until loaded
            textureMap[pointer][2] = state; // state
            pointer++;
            numtextures++;
        }
    }

    public int getNumtextures() {
        return numtextures;
    }

    /**
     * Generates a new OpenGL ES texture name (identifier).
     *
     * @return The newly generated texture name.
     */
    private static int newTextureID() {
        int[] temp = new int[1];
        GLES10.glGenTextures(1, temp, 0);
        return temp[0];
    }

    public void remove(int resID) {
        state = STATE_UNLOADING;
        found = false;

        // find the element
        for(int j=0;j<numtextures;j++){
            if(textureMap[j][0]==resID){
                // found it so move rest down and over
                found=true;
                textureMap[j][0]=STATE_EMPTY;
                textureMap[j][2]=STATE_EMPTY;
                // move others down
                for(int k=j;k<numtextures+1;k++){
                    textureMap[k][0]=textureMap[k+1][0];
                    textureMap[k][1]=textureMap[k+1][1];
                    textureMap[k][2]=textureMap[k+1][2];
                }
            }
        }
        numtextures--;
        state = STATE_FRESH;
    }

    public static int getState() {
        return state;
    }

    public void loadTextures(GL10 gl, Context context) {
        state = STATE_LOADING;
        int[] textureIDs = new int[1];
        for (int j = 0; j < numtextures; j++) {
            if (textureMap[j][2] == STATE_FRESH) {
                // Generate one texture pointer, and bind it to the texture array.
                gl.glGenTextures(1, textureIDs, 0);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
                textureMap[j][1] = textureIDs[0]; // save the new GLid

                // Create nearest filtered texture.
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                int textureResID = textureMap[j][0];
                // Construct an input stream to texture image
                InputStream istream = context.getResources().openRawResource(textureResID);
                Bitmap bitmap;
                try {
                    // Read and decode input as bitmap
                    bitmap = BitmapFactory.decodeStream(istream);
                } finally {
                    try {
                        istream.close();
                    } catch (IOException e) {
                    }
                    textureMap[j][2] = STATE_LOADED;
                }
                // Build Texture from loaded bitmap for the currently-bind texture ID
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
                bitmap.recycle();
            }
        }
        state = STATE_LOADED;
    }

public int getTexID(int resID){
    int gltextureid=0;
    for(int j=0;j<numtextures;j++){
        if(textureMap[j][0]==resID){
            gltextureid= textureMap[j][1];
        }
    }
    return gltextureid;
}

    /* unloads ALL textures from the GL and resets the lists */
    public void unloadALL(GL10 gl) {
        state = STATE_UNLOADING;
        int[] textureIds = new int[1];
        for (int j = 0; j < numtextures; j++) {
            textureIds[0] = textureMap[j][1]; // GLID
            textureMap[j][2]=state;
            gl.glDeleteTextures(textureIds.length, textureIds, 0);
            textureMap[j][0] = 0;
            textureMap[j][1] = 0;
            textureMap[j][2] = STATE_EMPTY;
        }
        gl.glFlush();
        // reset the pointers
        numtextures = 0;
        pointer = 0;
        // clear the map
        state=STATE_EMPTY;
    }

    void clearMAP() {
        for (int j = 0; j < MAXTEXTURES; j++) {
            textureMap[j][0] = 0;
            textureMap[j][1] = 0;
            textureMap[j][2] = STATE_EMPTY;
        }
    }
}