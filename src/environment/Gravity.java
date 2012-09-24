package environment;

import java.util.Scanner;
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
        super("gravity");
        myDirection = dir;
        myMagnitude = mag;
    }

    /**
     * create a Gravity instance 
     * @param line input to be handled
     * @return A gravity object that is a global force
     */
    public static Gravity createGravity (Scanner line) {
        double direction = line.nextDouble();
        double magnitude = line.nextDouble();
        return new Gravity(direction, magnitude);
    }

    /**
     * Gravity is a constant force in a certain direction,
     * independent of the mass.
     * 
     * @param m the mass on which gravity is applied
     */
    @Override
    public Vector getForce (Mass m) {
        return new Vector(myDirection, myMagnitude);
    }
}
