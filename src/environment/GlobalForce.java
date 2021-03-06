package environment;

import java.util.List;
import physicalobject.Mass;
import physicalobject.PhysicalObject;
import simulator.Vector;


/**
 * @author tyy
 */
public abstract class GlobalForce {
    private boolean myIsActive;
    private String myType;

    /**
     * global forces are set to active by default.
     * 
     * @param type the type of force
     */
    public GlobalForce (final String type) {
        myIsActive = true;
        myType = type;
    }

    /**
     * @param m the mass on which the force is applied
     * @return a vector that represents the force
     */
    public abstract Vector getForce (Mass m);

    /**
     * @param allObjects all the objects that exist in the simulation
     */
    public void applyToObject (List<PhysicalObject> allObjects) {
        for (PhysicalObject obj : allObjects) {
            if (myIsActive && obj instanceof Mass) {
                ((Mass) obj).applyForce(getForce((Mass) obj));
            }
        }
    }

    /**
     * @return the activity status of the force.
     */
    public boolean getActivity () {
        return myIsActive;
    }

    /**
     * For non-wall forces.
     *
     * @param type the type of force to be toggled
     */
    public void toggleActivity(final String type) {
        if (type.equals(myType)) {
            String printString = "GlobalForce " + type;
            if (myIsActive) {
                printString += " off";
            }
            else {
                printString += " on";
            }
            System.out.println(printString);
            myIsActive = !myIsActive;
        }
    }

    /**
     * @param type the new type
     */
    public void setType(final String type) {
        myType = type;
    }
}
