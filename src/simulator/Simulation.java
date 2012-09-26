package simulator;

import environment.GlobalForce;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import physicalobject.FixedMass;
import physicalobject.Mass;
import physicalobject.PhysicalObject;
import physicalobject.Spring;


/**
 * Simulates objects moving around in a bounded environment.
 * @author Robert C. Duvall
 * modified by tyy, Rex
 */
public class Simulation {
    // the global force list contains forces global to all objects in all
    // simulations
    private static List<GlobalForce> ourGlobalForces;
    private List<PhysicalObject> myObjects;

    //Mass and Spring controlled by users
    private Mass myUserMass;
    private Mass myNearestMass;
    private Spring myUserSpring;
    //Minimum distance from any Masses in this simulation to the mouse point
    private double myMinimumDistance;

    /**
     * Create a Canvas with the given size.
     *
     * @param container the canvas
     */
    public Simulation (Canvas container) {
        myObjects = new ArrayList<PhysicalObject>();
        // myGlobalForces list is only initialized once even there are multiple
        // simulations
        if (ourGlobalForces == null) {
            ourGlobalForces = new ArrayList<GlobalForce>();
            myUserMass = null;
            myUserSpring = null;
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
     * 
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
     * 
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
                if (((Mass) obj).match(id)) { return (Mass) obj; }
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

    /**
     * calculate the minimum distance from the position of mouse to any
     * mass in any Simulation objects contained in myTargets
     *
     * @param mousePosition the position of the mouse
     * @return the minimum distance
     */
    public double calculateMinimumDistance (Point mousePosition) {
        myMinimumDistance = Double.MAX_VALUE;
        myNearestMass = null;
        for (PhysicalObject o : myObjects) {
            if (o instanceof Mass) {
                if (Vector.distanceBetween(mousePosition,
                        ((Mass) o).getCenter()) < myMinimumDistance) {
                    myMinimumDistance = Vector.distanceBetween(mousePosition,
                            ((Mass) o).getCenter());
                    myNearestMass = (Mass)o;
                }
            }
        }
        return myMinimumDistance;
    }

    /**
     * create a Mass object that is at the mousePosition
     * @param mousePosition the position of mouse at which the object is created
     */
    public void createUserObjects (Point mousePosition) {
        myUserMass = new FixedMass(0, mousePosition.x, mousePosition.y, 0);
        myUserMass.hide();
        myObjects.add(myUserMass);
        myUserSpring = new Spring(myNearestMass, myUserMass, myMinimumDistance, 1);
        myObjects.add(myUserSpring);
    }

    /**
     * delete the Mass object created by clicking on the walled area
     * @param mousePosition the position of mouse at which the object is created
     */
    public void deleteUserObjects (Point mousePosition) {
        myObjects.remove(myUserMass);
        myObjects.remove(myUserSpring);
        myUserMass = null;
        myUserSpring = null;
    }

    /**
     * move the Mass object created by clicking on the walled area
     * @param targetPosition the position to which the Mass is moved
     */
    public void moveUserPoint (Point targetPosition) {
        myUserMass.setCenter(targetPosition);
    }
}
