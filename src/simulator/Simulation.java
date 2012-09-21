package simulator;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import environment.GlobalForce;
import physicalObject.Mass;
import physicalObject.PhysicalObject;


/**
 * Simulates objects moving around in a bounded environment.
 * @author Robert C. Duvall 
 * modified by tyy
 */
public class Simulation {
    private List<PhysicalObject> myObjects;
    private List<GlobalForce> myGlobalForces;
    private Canvas myContainer;

    /**
     * Create a Canvas with the given size.
     * @param container the canvas
     */
    public Simulation (Canvas container) {
        myObjects = new ArrayList<PhysicalObject>();
        myGlobalForces = new ArrayList<GlobalForce>();
        myContainer = container;
    }
    /**
     * @param obj a PhysicalObject to be added
     */
    public void add (PhysicalObject obj) {
        myObjects.add(obj);
    }
    
    public void add (GlobalForce f) {
        myGlobalForces.add(f);
    }

    /**
     * Paint all shapes on the canvas.
     * @param pen used to paint shape on the screen
     */
    public void paint (Graphics2D pen) {
        for (int i = myObjects.size() - 1; i >= 0; i--) {
            myObjects.get(i).paint(pen);
        }
    }

    /**
     * Called by each step of timer, multiple times per second.
     * This should update the state of the animated shapes by just
     * a little so they appear to move over time.
     * @param dt change in time
     */
    public void update (double dt) {
        for (GlobalForce gf : myGlobalForces) {
            gf.applyToObject(myObjects);
        }
        
        for (PhysicalObject obj : myObjects) {
            obj.update(this, dt);
        }
    }

    /**
     * Returns size (in pixels) of the game area.
     */
    public Dimension getSize () {
        return myContainer.getSize();
    }

    /**
     * @param id the id of the Mass
     */
    public Mass getMass (int id) {
        for (PhysicalObject obj : myObjects) {
            if (obj instanceof Mass) {
                if (((Mass)obj).match(id)) {
                    return (Mass)obj;
                }
            }
        }
        return null;
    }
}
