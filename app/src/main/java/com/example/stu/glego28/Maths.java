package com.example.stu.glego28;

import android.opengl.Matrix;

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
        return Math.sqrt(quat.qx * quat.qx + quat.qy * quat.qy +
                quat.qz * quat.qz + quat.qw * quat.qw);
    }

    Quaternion normalize(Quaternion quat)
    {
        double L = length(quat);

        quat.qx /= L;
        quat.qy /= L;
        quat.qz /= L;
        quat.qw /= L;

        return quat;
    }

    Quaternion conjugate(Quaternion quat)
    {
        quat.qx = -quat.qx;
        quat.qy = -quat.qy;
        quat.qz = -quat.qz;
        return quat;
    }

    Quaternion mult(Quaternion A, Quaternion B)
    {
        Quaternion C = new Quaternion(0,0,0,0);

        C.qx = A.qw*B.qx + A.qx*B.qw + A.qy*B.qz - A.qz*B.qy;
        C.qy = A.qw*B.qy - A.qx*B.qz + A.qy*B.qw + A.qz*B.qx;
        C.qz = A.qw*B.qz + A.qx*B.qy - A.qy*B.qx + A.qz*B.qw;
        C.qw = A.qw*B.qw - A.qx*B.qx - A.qy*B.qy - A.qz*B.qz;
        return C;
    }


}

