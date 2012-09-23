package simulator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import environment.CenterMass;
import environment.GlobalForce;
import environment.Gravity;
import environment.Viscosity;
import environment.WallRepulsion;
import physicalObject.FixedMass;
import physicalObject.Mass;
import physicalObject.PhysicalObject;
import physicalObject.Spring;


/**
 * Simulates objects moving around in a bounded environment.
 * 
 * @author Robert C. Duvall
 *         modified by tyy, Rex
 */
public class Simulation {
    private List<PhysicalObject> myObjects;
    // the global force list contains forces global to all objects in all
    // simulations
    private static List<GlobalForce> myGlobalForces;
    
    //Mass and Spring controlled by users
    private Mass userMass;
    private Mass nearestMass;
    private Spring userSpring;
    //Minimum distance from any Masses in this simulation to the mouse point
    private double minimumDistance;

    /**
     * Create a Canvas with the given size.
     * 
     * @param container the canvas
     */
    public Simulation (Canvas container) {
        myObjects = new ArrayList<PhysicalObject>();
        // myGlobalForces list is only initialized once even there are multiple
        // simulations
        if (myGlobalForces == null)
            myGlobalForces = new ArrayList<GlobalForce>();
        userMass = null;
        userSpring = null;
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
        return Canvas.getCanvasSize();
    }

    public Point getOrigin () {
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

    public void toggleGravity () {
        for (GlobalForce f : myGlobalForces) {
            if (f instanceof Gravity) f.toggleActivity();
        }
    }

    public void toggleViscosity () {
        for (GlobalForce f : myGlobalForces) {
            if (f instanceof Viscosity) f.toggleActivity();
        }
    }

    public void toggleCenterMass () {
        for (GlobalForce f : myGlobalForces) {
            if (f instanceof CenterMass) f.toggleActivity();
        }
    }

    public void toggleWall (int id) {
        for (GlobalForce f : myGlobalForces) {
            if (f instanceof WallRepulsion) {
                if (((WallRepulsion) f).getID() == id) f.toggleActivity();
            }
        }
    }

    public Mass highlight (Point mousePosition) {
        for (PhysicalObject o : myObjects) {
            if (o instanceof Mass) {
                Mass m = (Mass) o;
                Rectangle myRect = new Rectangle(m.getLeft(), m.getTop(),
                        m.getSize().width, m.getSize().height);
                if (myRect.contains(mousePosition)) { return m; }
            }
        }
        return null;
    }

    public double calculateMinimumDistance (Point mousePosition) {
        minimumDistance = Double.MAX_VALUE;
        nearestMass = null;
        for (PhysicalObject o : myObjects) {
            if (o instanceof Mass) {
                if (Vector.distanceBetween(mousePosition,
                        ((Mass) o).getCenter()) < minimumDistance) {
                    minimumDistance = Vector.distanceBetween(mousePosition,
                            ((Mass) o).getCenter());
                    nearestMass = (Mass)o;
                }
            }
        }
        return minimumDistance;
    }
    
    public void createUserObjects (Point mousePosition) {
        userMass = new FixedMass(0, mousePosition.x, mousePosition.y, 0);
        userMass.hide();
        myObjects.add(userMass);
        userSpring = new Spring(nearestMass, userMass, minimumDistance, 1);
        myObjects.add(userSpring);
    }
    
    public void deleteUserObjects (Point mousePosition) {
        myObjects.remove(userMass);
        myObjects.remove(userSpring);
        userMass = null;
        userSpring = null;
    }
    
    public void moveUserPoint (Point targetPosition) {
        userMass.setCenter(targetPosition);
    }
}
