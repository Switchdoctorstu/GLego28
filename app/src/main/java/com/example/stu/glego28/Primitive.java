package com.example.stu.glego28;

/**
 * Created by Stu on 8/15/2015.
 */
public class Primitive {

    protected Vertex3f rotation;
    protected Vertex3f translation;
    protected Vertex3f scale;

    public Primitive() {
       // textureIDs=new int[1];
        translation=new Vertex3f(0f,0f,0f);
        rotation=new Vertex3f(0f,0f,0f);
        scale=new Vertex3f(1f,1f,1f);
    }

   // int[] textureIDs;  // only 0 is used


    public Vertex3f getRotation() {
        return rotation;
    }

    public void setRotation(Vertex3f rotation) {
        this.rotation = rotation;
    }

    public Vertex3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vertex3f translation) {
        this.translation = translation;
    }

    public Vertex3f getScale() {
        return scale;
    }

    public void setScale(Vertex3f scale) {
        this.scale = scale;
    }
}
