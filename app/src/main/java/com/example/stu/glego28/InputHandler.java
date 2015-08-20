package com.example.stu.glego28;

import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Stu on 8/18/2015.
 */
public class InputHandler {

    ArrayList<ControllerBall> ballList;
    ArrayList<ScreenControllerSocket> socketlist;
    ScreenControllerSocket screenControllerSocket1;
    ScreenControllerSocket screenControllerSocket2;
    int screenwidth,screenheight;
    int numSockets;

    public InputHandler() {
        screenControllerSocket1 = new ScreenControllerSocket(new Point2D(0,800), new Point2D(200,1000));
        screenControllerSocket1.setId(0);
        screenControllerSocket2 = new ScreenControllerSocket(new Point2D(1600,800), new Point2D(2000,1000));
        screenControllerSocket2.setId(1);
        socketlist = new ArrayList<ScreenControllerSocket>();
        ballList = new ArrayList<ControllerBall>();
        socketlist.add(screenControllerSocket1);
        socketlist.add(screenControllerSocket2);
    }

    boolean addcontrollerball(ControllerBall cb){
        if(socketlist.size()>ballList.size()) {
            ballList.add(cb);
            return true;
        }else{
            return false;
        }
    }

    // check list of sockets for hits and activate controllers
    void checkScreenSockets(int action, int actionIndex, int id, int x, int y){
        int z=0;
        for(Iterator<ScreenControllerSocket> iter= socketlist.iterator();iter.hasNext();){
            ScreenControllerSocket thisSCS = iter.next();
            if(isPointInBounnds(new Point2D(x,y),thisSCS.lowPoint,thisSCS.highpoint)){
                // we have a hit so pass the action to the controller
                // calculate the x and y within our frame
                float xextent=thisSCS.lowPoint.x-thisSCS.highpoint.x;
                float yextent=thisSCS.lowPoint.y-thisSCS.highpoint.y;
                Point2D p = new Point2D((x-thisSCS.lowPoint.x)/xextent,(y-thisSCS.lowPoint.y)/yextent);
                ballList.get(z).notify(action,actionIndex,id,p);
            }
            z++; // keep track of list id
        }
    }

    /*
    Check to see if point is within bounds
     */
    boolean isPointInBounnds(Point2D target, Point2D bound1, Point2D bound2){

        if(target.x>bound1.x && target.y>bound1.y && target.x <bound2.x && target.y<bound2.y){
                return true;

        } else {
            return false;
        }
    }


}
