package com.example.stu.glego28;

/**
 * Created by Stu on 8/15/2015.
 */
public class Vertex3f {
    public float Vx, Vy, Vz;

    Vertex3f(float x, float y, float z) {
        Vx = x;
        Vy = y;
        Vz = z;
    }

    Vertex3f subtract(Vertex3f sub){
        return new Vertex3f(this.Vx-sub.Vx,this.Vy-sub.Vy,this.Vz-sub.Vz);
    }

    float length(){
        return (float) Math.sqrt((this.Vx*this.Vx)+(this.Vy*this.Vy)+(this.Vz*this.Vz));
    }

    float distanceTo(Vertex3f to){
        return (float) Math.sqrt(((to.Vx-this.Vx)*(to.Vx-this.Vx))+((to.Vy-this.Vy)*(to.Vy-this.Vy))+((to.Vz-this.Vz)*(to.Vz-this.Vz)));
    }

    Vertex3f normalise(){
        float mult=1/this.length();
        return new Vertex3f(this.Vx*mult,this.Vy*mult,this.Vz*mult);
    }
}