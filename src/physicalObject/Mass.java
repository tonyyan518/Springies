package physicalObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import simulator.Simulation;
import simulator.Vector;

/**
 * @author Robert C. Duvall
 * modified by tyy
 */
public class Mass extends PhysicalObject {
    // state
    private static final int LEFT = 180;
    private static final int RIGHT = 0;
    private static final int UP = 90;
    private static final int DOWN = 270;
    private static final int BOUNCE_SCALE = 2;
    private static final double GRAVITY = 0.5;
    private static final int MASS_DIM = 16;
    private Point2D myCenter;
    private Vector myVelocity;
    private Dimension mySize;
    private int myID;
    private double myMass;
    private Vector myAcceleration;
    private Point2D myPrevPos;

    /**
     * @param id Mass's id
     * @param x initial x coordinate
     * @param y initial y coordinate
     * @param mass Mass's mass
     */
    public Mass (int id, double x, double y, double mass) {
        myAcceleration = new Vector();
        myMass = mass;
        myID = id;
        setCenter(x, y);
        setVelocity(0, 0);
        mySize = new Dimension(MASS_DIM, MASS_DIM);
    }

    /**
     * @param pen the pen
     */
    public void paint (Graphics2D pen) {
        pen.setColor(Color.BLACK);
        pen.fillOval(getLeft(), getTop(), getSize().width, getSize().height);
    }

    /**
     * @param canvas the canvas
     * @param dt change in time
     */
    public void update (Simulation canvas, double dt) {
        myPrevPos = (Point2D)(myCenter.clone());
        applyForce(getGravity());
        applyForce(getBounce(canvas.getSize()));
        // convert force back into Mover's velocity
        getVelocity().sum(myAcceleration);
        myAcceleration.reset();
        // move mass by velocity
        myCenter.setLocation(myCenter.getX() + myVelocity.getXChange() * dt,
                myCenter.getY() + myVelocity.getYChange() * dt);
        //when mass rises, there is a reduction in Y-axis
    }

    /**
     * @param f
     *        force to be applied to this mass
     */
    public void applyForce (Vector f) {
        myAcceleration.sum(f);
    }

    /**
     * @param id check if two Masses have the same id
     */
    public boolean match (int id) {
        return myID == id;
    }

    // add gravity towards bottom
    private Vector getGravity () {
        Vector result = new Vector(UP, GRAVITY);
        return result;
    }

    // check for move out of bounds
    private Vector getBounce (Dimension bounds) {
        Vector impulse = new Vector();
        if (getLeft() < 0) {
            impulse = new Vector(RIGHT, BOUNCE_SCALE);
            setCenter(getSize().width / 2, getCenter().getY());
        }
        else if (getRight() > bounds.width) {
            impulse = new Vector(LEFT, BOUNCE_SCALE);
            setCenter(bounds.width - getSize().width / 2, getCenter().getY());
        }
        if (getTop() < 0) {
            impulse = new Vector(UP, BOUNCE_SCALE);
            setCenter(getCenter().getX(), getSize().height / 2);
        }
        else if (getBottom() > bounds.height) {
            impulse = new Vector(DOWN, BOUNCE_SCALE);
            setCenter(getCenter().getX(), bounds.height - getSize().height / 2);
        }
        impulse.scale(getVelocity().getRelativeMagnitude(impulse));
        return impulse;
    }

    /**
     * Returns shape's velocity.
     */
    public Vector getVelocity () {
        return myVelocity;
    }

    /**
     * Resets shape's velocity.
     * @param direction a double
     * @param magnitude another double
     */
    public void setVelocity (double direction, double magnitude) {
        myVelocity = new Vector(direction, magnitude);
    }

    /**
     * Returns shape's center.
     */
    public Point2D getCenter () {
        return myCenter;
    }

    /**
     * Resets shape's center.
     * @param x center x coordinate
     * @param y center y coordinate
     */
    public void setCenter (double x, double y) {
        myCenter = new Point2D.Double(x, y);
    }

    /**
     * Returns shape's left-most coordinate.
     */
    public int getLeft () {
        return (int) (getCenter().getX() - getSize().width / 2);
    }

    /**
     * Returns shape's top-most coordinate.
     */
    public int getTop () {
        return (int) (getCenter().getY() - getSize().height / 2);
    }

    /**
     * Returns shape's right-most coordinate.
     */
    public int getRight () {
        return (int) (getCenter().getX() + getSize().width / 2);
    }

    /**
     * Reports shape's bottom-most coordinate.
     * @return bottom-most coordinate
     */
    public int getBottom () {
        return (int) (getCenter().getY() + getSize().height / 2);
    }

    /**
     * Returns shape's size.
     */
    public Dimension getSize () {
        return mySize;
    }

    /**
     * Returns shape's acceleration.
     */
    public Vector getAcceleration () {
        return myAcceleration;
    }

    /**
     * Sets shape's acceleration.
     * @param a acceleration
     */
    public void setAcceleration (Vector a) {
        myAcceleration = a;
    }

    /**
     * Returns shape's mass.
     */
    public double getMass () {
        return myMass;
    }
    /**
     * Returns shape's previous position.
     */
    public Point2D getPrevPos() {
        return myPrevPos;
    }
    /**
     * @param f how much to move by
     */
    public void move (Vector f) {
        myCenter.setLocation(myCenter.getX() + f.getXChange(),
                myCenter.getY() - f.getYChange());
    }
    /**
     * detects if the mass is moving.
     */
    public boolean isMoving() {
        return !myCenter.equals(myPrevPos);
    }
}