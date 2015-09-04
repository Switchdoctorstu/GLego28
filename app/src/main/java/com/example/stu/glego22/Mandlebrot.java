package com.example.stu.glego22;

/**
 * Generate a surface to explore
 * Created by Stu on 8/28/2015.
 */
public class Mandlebrot {

    public Mandlebrot(){};
    // return number of iterations to check if c = a + ib is in Mandelbrot set

   public static int mandel(float px, float py, float max) {
        float zx = 0.0f;
        float zy = 0.0f;
        float zx2 = 0.0f, zy2 = 0.0f;
        int value = 0;
        while (value < max && zx2 + zy2 < 4.0f) {
            zy = 2.0f * zx * zy + py;
            zx = zx2 - zy2 + px;
            zx2 = zx * zx;
            zy2 = zy * zy;
            value++;
        }
        return value;
    }

    private int mandel(float zRe, float zIm, float cRe, float cIm, float maxCount) {
        float zRe2 = zRe * zRe;
        float zIm2 = zIm * zIm;
        float zM2 = 0.0f;
        int count = 0;
        while (zRe2 + zIm2 < 4.0 && count < maxCount) {
            zM2 = zRe2 + zIm2;
            zIm = 2.0f * zRe * zIm + cIm;
            zRe = zRe2 - zIm2 + cRe;
            zRe2 = zRe * zRe;
            zIm2 = zIm * zIm;
            count++;
        }
        if (count == 0 || count == maxCount) return 0;
        zM2 += 0.000000001;
        return 256 * count + (int)(255.0 * Math.log(4.0 / zM2) / Math.log((zRe2 + zIm2) / zM2));
    }


}
