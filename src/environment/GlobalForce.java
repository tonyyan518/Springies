package environment;
import simulator.Vector;
import java.util.List;
import physicalObject.PhysicalObject;
import physicalObject.Mass;

public abstract class GlobalForce {
    private boolean myIsActive;
    
    public GlobalForce() {
        myIsActive = true;
    }
    
    public abstract Vector getForce(Mass m);
    
    public void applyToObject(List<PhysicalObject> allObjects) {
        for (PhysicalObject obj : allObjects) {
            if (myIsActive && obj instanceof Mass) {
                ((Mass) obj).applyForce(getForce((Mass)obj));
            }
        }
    }
    
    public boolean getActivity() {
        return myIsActive;
    }
    
    public void toggleActivity() {
        myIsActive = !myIsActive;
    }
}
