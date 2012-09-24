package environment;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import physicalobject.Mass;
import simulator.Canvas;
import simulator.Vector;

/**
 * @author tyy
 */
public class WallRepulsion extends GlobalForce {
    private static final int LEFT = 180;
    private static final int RIGHT = 0;
    
    private int myID;
    private double myMagnitude;
    private double myExponent;
    /**
     * @param id the id of the wall
     * @param mag the magnitude
     * @param exp the exponent
     */
    public WallRepulsion (int id, double mag, double exp) {
        myID = id;
        myMagnitude = mag;
        myExponent = exp;
    }
    /**
     * @param m the mass on which the repulsion is applied
     */
    @Override
    public Vector getForce (Mass m) {
        Dimension canvasDimension = Canvas.getCanvasSize();
        Point2D canvasOrigin = Canvas.getOrigin();
        double dir = 0;
        double dist = 0;
        if (myID == TOP_WALL) {
            dir = UP;
            dist = m.getCenter().getY() - canvasOrigin.getY();
        }
        else if (myID == RIGHT_WALL) {
            dir = LEFT;
            dist = canvasOrigin.getX() + canvasDimension.width - m.getCenter().getX();
        }
        else if (myID == BOTTOM_WALL) {
            dir = DOWN;
            dist = canvasOrigin.getY() + canvasDimension.height - m.getCenter().getY();
        }
        else if (myID == LEFT_WALL) {
            dir = RIGHT;
            dist = m.getCenter().getX() - canvasOrigin.getX();
        }
        double mag = myMagnitude / Math.pow(dist, myExponent);
        System.out.println(mag);
        return new Vector(dir, mag);
    }
    /**
     * get the wall id.
     */
    public int getID() {
        return myID;
    }
}
