package environment;

import physicalObject.Mass;
import simulator.Vector;

public class Gravity extends GlobalForce {
    private double myDirection;
    private double myMagnitude;
       
    public Gravity (double dir, double mag) {
        super();
        myDirection = dir;
        myMagnitude = mag;
    }
    
    @Override
    public Vector getForce (Mass m) {
        Vector f = new Vector();
        if (getActivity()) {
            f.setDirection(myDirection);
            f.setMagnitude(myMagnitude);
        }
        return f;
    }
}
