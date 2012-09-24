package environment;

import java.awt.geom.Point2D;
import physicalobject.Mass;
import simulator.Vector;

/**
 * @author tyy
 */
public class CenterMass extends GlobalForce {
    private Point2D myCenter;
    private double myMagnitude;
    private double myExponent;
    /**
     * @param x the x coordinate of the center of mass
     * @param y the y coordinate of the center of mass
     * @param mag the magnitude of the force
     * @param exp the exponent of the force
     */
    public CenterMass(int x, int y, double mag, double exp) {
        super();
        myCenter = new Point2D.Double(x, y);
        myMagnitude = mag;
        myExponent = exp;
    }
    /**
     * The center of mass applies a force on each mass depending
     * on its magnitude and how far away the mass is.
     * @param m the mass affected
     */
    @Override
    public Vector getForce (Mass m) {
        double dx = myCenter.getX() - m.getCenter().getX();
        double dy =  myCenter.getY() -  m.getCenter().getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        double dir = Math.toDegrees(Math.atan2(dy, dx));
        double mag = Math.abs(myMagnitude) / Math.pow(dist, myExponent);
        Vector f = new Vector(dir, mag);
        if (myMagnitude < 0) {
            f.negate();
        }
        return f;
    }

}
