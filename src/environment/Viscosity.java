package environment;

import physicalobject.Mass;
import simulator.Vector;

/**
 * @author tyy
 */
public class Viscosity extends GlobalForce {
    private double myResistance;
    /**
     * @param res resistance constant of the viscosity
     */
    public Viscosity (double res) {
        myResistance = res;
    }
    /**
     * Viscosity applies a force in the opposite to the direction
     * of motion proportional to a mass's velocity.
     * @param m the mass
     */
    @Override
    public Vector getForce (Mass m) {
        double dir = m.getVelocity().getDirection();
        double mag = myResistance * m.getVelocity().getMagnitude();
        Vector f = new Vector(dir, mag);
        f.negate();
        return f;
    }
}
