package environment;

import java.awt.geom.Point2D;
import physicalObject.Mass;
import simulator.Vector;
import simulator.Simulation;

public class CenterMass extends GlobalForce{
    private Point2D myCenter;
    private double myMagnitude;
    private double myExponent;
    
    public CenterMass(int x, int y, double mag, double exp) {
        super();
        myCenter = new Point2D.Double(x, y);
        myMagnitude = mag;
        myExponent = exp;
    }
    
    @Override
    public Vector getForce (Mass m) {
        double dx = myCenter.getX() - m.getCenter().getX();
        double dy =  myCenter.getY() -  m.getCenter().getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        double dir = Math.toDegrees(Math.atan2(dy, dx));
        double mag = Math.abs(myMagnitude) / Math.pow(dist, myExponent);
        Vector f = new Vector (dir, mag);
        if (myMagnitude < 0)
            f.negate();
        return f;
    }

}
