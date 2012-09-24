package environment;

import physicalobject.Mass;

import simulator.Vector;
/**
 * @author tyy
 */
public class Gravity extends GlobalForce {
    private double myDirection;
    private double myMagnitude;
    /**
     * @param dir the direction of gravity
     * @param mag the magnitude of gravity
     */
    public Gravity (double dir, double mag) {
        myDirection = dir;
        myMagnitude = mag;
    }
    /**
     * Gravity is a constant force in a certain direction,
     * independent of the mass.
     * @param m the mass on which gravity is applied
     */
    @Override
    public Vector getForce (Mass m) {
        return new Vector(myDirection, myMagnitude);
    }
}
