package com.example.stu.glego28;

import android.opengl.Matrix;

import java.util.Vector;

/**
 * Created by Stu on 23/07/2015.
 */
class Maths {

    /** 180 in radians. */
    public static final double ONE_EIGHTY_DEGREES = Math.PI;

    /** 360 in radians. */
    public static final double THREE_SIXTY_DEGREES = ONE_EIGHTY_DEGREES * 2;

    /** 120 in radians. */
    public static final double ONE_TWENTY_DEGREES = THREE_SIXTY_DEGREES / 3;

    /** 90 degrees, North pole. */
    public static final double NINETY_DEGREES = Math.PI / 2;

    /** Used by power. */
    private static final long POWER_CLAMP = 0x00000000ffffffffL;

    /**
     * Constructor, although not used at the moment.
     */
    private Maths() {
    }

    /** PI constant as float */
    public static float PI = 3.141592653589f;
    /** PI/2 constant as float */
    public static float HALF_PI = 1.5707963267946f;
    /** Convert radians to degrees */
    public static float toDegrees(float radians){
        return radians*180/PI;
    }
    /** Convert degrees to radians */
    public static float toRadians(float degrees){
        return degrees*PI/180;
    }

    /**
     * Quick integer power function.
     *
     * @param base
     *            number to raise.
     * @param raise
     *            to this power.
     * @return base ^ raise.
     */
    public static int power(final int base, final int raise) {
        int p = 1;
        long b = raise & POWER_CLAMP;

        // bits in b correspond to values of powerN
        // so start with p=1, and for each set bit in b, multiply corresponding
        // table entry
        long powerN = base;

        while (b != 0) {
            if ((b & 1) != 0) {
                p *= powerN;
            }
            b >>>= 1;
            powerN = powerN * powerN;
        }

        return p;
    }

    double length(Quaternion quat)
    {
        return Math.sqrt(quat.x * quat.x + quat.y * quat.y +
                quat.z * quat.z + quat.w * quat.w);
    }

    Quaternion normalize(Quaternion quat)
    {
        double L = length(quat);

        quat.x /= L;
        quat.y /= L;
        quat.z /= L;
        quat.w /= L;

        return quat;
    }

    Quaternion conjugate(Quaternion quat)
    {
        quat.x = -quat.x;
        quat.y = -quat.y;
        quat.z = -quat.z;
        return quat;
    }

    Quaternion mult(Quaternion A, Quaternion B)
    {
        Quaternion C = new Quaternion(0,0,0,0);

        C.x = A.w*B.x + A.x*B.w + A.y*B.z - A.z*B.y;
        C.y = A.w*B.y - A.x*B.z + A.y*B.w + A.z*B.x;
        C.z = A.w*B.z + A.x*B.y - A.y*B.x + A.z*B.w;
        C.w = A.w*B.w - A.x*B.x - A.y*B.y - A.z*B.z;
        return C;
    }


}

