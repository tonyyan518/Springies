package environment;

import physicalObject.Mass;
import simulator.Vector;

public class Viscosity extends GlobalForce {
    private double myResistance;
    
    public Viscosity (double res) {
        myResistance = res;
    }
    
    @Override
    public Vector getForce (Mass m) {
        double dir = m.getVelocity().getDirection();
        double mag = myResistance * m.getVelocity().getMagnitude();
        Vector f = new Vector (dir, mag);
        f.negate();
        return f;
    }
    
}
