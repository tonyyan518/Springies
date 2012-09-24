package simulator;

import environment.GlobalForce;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import physicalobject.Mass;
import physicalobject.PhysicalObject;


/**
 * Simulates objects moving around in a bounded environment.
 * @author Robert C. Duvall
 * modified by tyy, Rex
 */
public class Simulation {
    private static List<GlobalForce> ourGlobalForces;
    private List<PhysicalObject> myObjects;

    /**
     * Create a Canvas with the given size.
     * @param container the canvas
     */
    public Simulation (Canvas container) {
        myObjects = new ArrayList<PhysicalObject>();
        if (ourGlobalForces == null) {
            ourGlobalForces = new ArrayList<GlobalForce>();
        }
    }
    /**
     * @param obj a PhysicalObject to be added
     */
    public void add (PhysicalObject obj) {
        myObjects.add(obj);
    }
    /**
     * @param f a new force to be added
     */
    public void add (GlobalForce f) {
        ourGlobalForces.add(f);
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
        for (GlobalForce gf : ourGlobalForces) {
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
        return Canvas.getCanvasSize();
    }
    /**
     * Returns origin of the game area.
     */
    public Point getOrigin() {
        return Canvas.getOrigin();
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
    /**
     * @param type the type of force to toggle
     */
    public void toggleForce(String type) {
        for (GlobalForce f : ourGlobalForces) {
            f.toggleActivity(type);
        }
    }
    /**
     * @param mousePosition the position of the mouse
     */
    public Mass highlight(Point mousePosition) {
        for (PhysicalObject o: myObjects) {
            if (o instanceof Mass) {
                Mass m = (Mass)o;
                Rectangle myRect = new Rectangle(m.getLeft(), m.getTop(),
                        m.getSize().width, m.getSize().height);
                if (myRect.contains(mousePosition)) {
                    return m;
                }
            }
        }
        return null;
    }
}
