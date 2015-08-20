package com.example.stu.glego28;

import android.util.FloatMath;

/**
 * Created by Stu on 8/19/2015.
 */
public class Vector3f {
    /*** Simple 3-component vector class
     * We use this to store vertex info, specify object positions, etc.
     * @author oscarblasco
     */


        // Global ShadingZen's axis vectors
        public static final Vector3f vectorRight = new Vector3f(1.f, 0.f, 0.f);
        public static final Vector3f vectorUp = new Vector3f(0.f, 1.f, 0.f);
        public static final Vector3f vectorFront = new Vector3f(0.f, 0.f, 1.f);
        public static final float[] vectorRightArray = {1.f, 0.f, 0.f, 0.f};
        public static final float[] vectorUpArray = {0.f, 1.f, 0.f, 0.f};
        public static final float[] vectorFrontArray = {0.f, 0.f, 1.f, 0.f};
        public static final Vector3f zero = new Vector3f();

        public float x, y, z;
        public Vector3f(){
            x = y = z = 0.f;
        }
        public Vector3f(float x, float y, float z){
            this.x = x; this.y = y; this.z = z;
        }
        public Vector3f(Vector3f v){
            x = v.x; y = v.y; z = v.z;
        }
        public Vector3f(float v[]){
            x = v[0];
            y = v[1];
            z = v[2];
        }

        public float dot(Vector3f v){
            return x *v.x + y *v.y + z *v.z;
        }

        public Vector3f cross(Vector3f v){
            return new Vector3f(
                    y *v.z - z *v.y,
                    z *v.x - x *v.z,
                    x *v.y - y *v.x
            );
        }

        public void crossNoCopy(Vector3f v){

            float _x = y *v.z - z *v.y;
            float _y = z *v.x - x *v.z;
            z = x *v.y - y *v.x;
            x = _x;
            y = _y;

        }

        /*** Return a float [3]
         *
         * @return an array of float with the components
         */
        public float [] getAsArray(){
            float array[] = new float[3];
            array[0] = x;
            array[1] = y;
            array[2] = z;

            return array;
        }

        public void toArray(float [] array){
            array[0] = x;
            array[1] = y;
            array[2] = z;
        }

        /*** Lenght of this vector */
        public float length(){
            return x * x + y * y + z * z;
        }

        /*** sqrt(Lenght) of this vector */
        public float lengthSqrt(){
            return (float) Math.sqrt( length());
        }
        /*** Perform a normalization
         * If sqrt(len) of this vector is greater than an EPSILON value (0,0000001)
         * this methods perform a normalization of this vector.
         * Original vector is untouched, a new one is returned.
         * @return Returns a new normalized vector.
         */
        public Vector3f normalize(){
            float sqr_length =  FloatMath.sqrt(length());
            if(sqr_length >= 0.0000001f){
                float inv = 1/sqr_length;
                return new Vector3f(x *inv, y *inv, z *inv);
            }
            return new Vector3f(0.f, 0.f, 0.f);
        }

        /***
         * Normalizes this vector without creating a new one
         */
        public float normalizeNoCopy(){
            float sqr_length =  FloatMath.sqrt(length());
            if(sqr_length >= 0.0000001f){
                float inv = 1/sqr_length;
                x *= inv;
                y *= inv;
                z *= inv;
            } else {
                x = 0.f;
                y = 0.f;
                z = 0.f;
            }

            return sqr_length;
        }

        public float getX(){
            return x;
        }
        public float getY(){
            return y;
        }
        public float getZ(){
            return z;
        }
        public Vector2f getXY(){
            return new Vector2f(x, y);
        }

        public void setX(float x){
            this.x = x;
        }
        public void setY(float y){
            this.y = y;
        }
        public void setZ(float z){
            this.z = z;
        }

        public void set(float x, float y, float z) {
            setX(x);
            setY(y);
            setZ(z);
        }

        public void set(Vector3f v){
            setX(v.x);
            setY(v.y);
            setZ(v.z);
        }

        public void set(float [] vec){
            x = vec[0];
            y = vec[1];
            z = vec[2];
        }

        public Vector3f sub(Vector3f b){
            return new Vector3f(
                    x - b.x,
                    y - b.y,
                    z - b.z
            );
        }
        public void subNoCopy(Vector3f b){
            x -= b.x;
            y -= b.y;
            z -= b.z;
        }

        public Vector3f add(Vector3f b){
            return new Vector3f(
                    x + b.x,
                    y + b.y,
                    z + b.z
            );
        }

        public void addNoCopy(Vector3f b){
            x += b.x;
            y += b.y;
            z += b.z;
        }

        public Vector3f mul(float f){
            return new Vector3f(
                    x *f,
                    y *f,
                    z *f
            );
        }

        public void mulInplace(float f){
            x *= f;
            y *= f;
            z *= f;
        }

        public Vector3f negate(){
            return new Vector3f(
                    -x,
                    -y,
                    -z
            );
        }

        public void negateNoCopy(){
            x = -x;
            y = -y;
            z = -z;
        }

    }
