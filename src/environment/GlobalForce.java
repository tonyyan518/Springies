package environment;
import simulator.Vector;
import java.util.List;
import physicalobject.Mass;
import physicalobject.PhysicalObject;

/**
 * @author tyy
 */
public abstract class GlobalForce {
    private boolean myIsActive;
    /**
     * global forces are set to active by default.
     */
    public GlobalForce() {
        myIsActive = true;
    }
    /**
     * @param m the mass on which the force is applied
     */
    public abstract Vector getForce(Mass m);
    /**
     * @param allObjects all the objects that exist in the simulation
     */
    public void applyToObject(List<PhysicalObject> allObjects) {
        for (PhysicalObject obj : allObjects) {
            if (myIsActive && obj instanceof Mass) {
                ((Mass) obj).applyForce(getForce((Mass)obj));
            }
        }
    }
    /**
     * returns the activity status of the force.
     */
    public boolean getActivity() {
        return myIsActive;
    }
    /**
     * toggles the activity of the force.
     */
    public void toggleActivity() {
        myIsActive = !myIsActive;
    }
}
