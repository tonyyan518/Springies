package environment;

import physicalObject.Mass;
import simulator.Vector;

public class WallRepulsion extends GlobalForce {
    int myID;
    double myMagnitude;
    double myExponent;
    
    public WallRepulsion (int id, double mag, double exp) {
        myID = id;
        myMagnitude = mag;
        myExponent = exp;
    }
    
    @Override
    public Vector getForce (Mass m) {
        if (myID == 1) {
            return topWall();
        }
        else if (myID == 2) {
            return rightWall();
        }
        else if (myID == 3) {
            return bottomWall();
        }
        else if (myID == 4) {
            return leftWall();
        }
        else
            return new Vector();
    }
    
    private Vector leftWall() {
        return new Vector();
    }
    
    private Vector rightWall() {
        return new Vector();
    }
    
    private Vector topWall() {
        return new Vector();
    }
    
    private Vector bottomWall() {
        return new Vector();
    }
}
