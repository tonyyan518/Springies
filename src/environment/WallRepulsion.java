package environment;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import physicalObject.Mass;
import simulator.Canvas;
import simulator.Vector;

public class WallRepulsion extends GlobalForce {
    private static final int LEFT = 180;
    private static final int RIGHT = 0;
    private static final int UP = 90;
    private static final int DOWN = 270;
    private int myID;
    private double myMagnitude;
    private double myExponent;
    
    public WallRepulsion (int id, double mag, double exp) {
        myID = id;
        myMagnitude = mag;
        myExponent = exp;
    }
    
    @Override
    public Vector getForce (Mass m) {
        Dimension canvasDimension = Canvas.getCanvasSize();
        Point2D canvasOrigin = Canvas.getOrigin();
        double dir = 0;
        double dist = 0;
        if (myID == 1) {
            dir = UP;
            dist = m.getCenter().getY() - canvasOrigin.getY();
        }
        else if (myID == 2) {
            dir = LEFT;
            dist = canvasOrigin.getX() + canvasDimension.width - m.getCenter().getX();
        }
        else if (myID == 3) {
            dir = DOWN;
            dist = canvasOrigin.getY() + canvasDimension.height - m.getCenter().getY();
        }
        else if (myID == 4) {
            dir = RIGHT;
            dist = m.getCenter().getX() - canvasOrigin.getX();
        }
        double mag = myMagnitude / Math.pow(dist, myExponent);
        System.out.println(mag);
        return new Vector(dir, mag);
    }
    
    public int getID() {
        return myID;
    }
}
