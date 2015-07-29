package com.example.stu.glego28;

/**
 * Created by Stu on 26/07/2015.
 */
import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;

public final class TextureManager {

    public static final int STATE_FRESH = 0;
    // Indicates that the texture manager has all the textures but they are not loaded.


    public static final int STATE_LOADING = 1;
/**
 * Indicates that the texture manager is loading the textures.
 */

    public static final int STATE_UNLOADING = 2;
    /**
     * Indicates that the texture manager is unloading some or all of the textures.
     */

    public static final int STATE_LOADED = 3;
/**
 * Indicates that the texture manager has finished loading the textures. In this
 * state the textures are functional.
 */
    /**
     * Contains mappings from resource identifiers to Texture objects.
     */
    private static SparseArray<Texture> texMap = new SparseArray<Texture>();

    /**
     * Contains the list of Texture objects the texture manager works with.
     */
    private static ArrayList<Texture> texList = new ArrayList<Texture>();

    /**
     * The current state of the texture manager.
     */
    private static int state;

    public static void add(int[] textureSet) {
        state = STATE_LOADING;

        for(int i = 0; i < textureSet.length; i++) {
            Texture tex = new Texture(textureSet[i]);
            texMap.put(textureSet[i], tex);
            texList.add(tex);
        }

        state = STATE_FRESH;
    }

    public static void remove(int[] textureSet) {
        state = STATE_UNLOADING;

        for(int i = 0; i < textureSet.length; i++) {
            Texture tex = texMap.get(textureSet[i]);
            tex.destroy();
            texMap.remove(textureSet[i]);
            texList.remove(tex);
        }

        state = STATE_FRESH;
    }

    public static void clear() {
        state = STATE_UNLOADING;

        for(int i = 0; i < texList.size(); i++)
            texList.get(i).destroy();

        texList.clear();
        texMap.clear();
        state = STATE_FRESH;
    }

    public static Texture getTexture(int resourceId) {
        return texMap.get(resourceId);
    }

    public static int getState() {
        return state;
    }

    public static void loadTextures(Context context) {
        if(state != STATE_FRESH)
            return;

        state = STATE_LOADING;

        final Object[] list = texList.toArray();

        for(int i = 0; i < list.length; i++) {
            Texture tex = (Texture)list[i];

            if(!tex.isLoaded())
                tex.load(context);
        }

        checkAndSetLoaded(list);
    }

    private static void checkAndSetLoaded(Object[] list) {
        for (int i = 0; i < list.length; i++) {
            Texture tex = (Texture)list[i];

            if(!tex.isLoaded()) {
                state = STATE_FRESH;
                return;
            }
        }

        state = STATE_LOADED;
    }

    public static void unload() {
        state = STATE_UNLOADING;

        final Object[] list = texList.toArray();

        for(int i = 0; i < list.length; i++) {
            Texture tex = (Texture)list[i];
            tex.destroy();
        }

        state = STATE_FRESH;
    }
}
