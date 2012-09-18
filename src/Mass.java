import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;


/**
 * @author Robert C. Duvall
 */
public class Mass {
    // state
    private static final int LEFT = 180;
    private static final int RIGHT = 0;
    private static final int UP = 270;
    private static final int DOWN = 90;
    private static final int BOUNCE_SCALE = 2;
    private static final double GRAVITY = 0.5;
    private static final int MASS_DIM = 16;
    private Point2D myCenter;
    private Force myVelocity;
    private Dimension mySize;
    private int myID;
    private double myMass;
    private Force myAcceleration;
    private Force myPrevAcc = new Force();
    
    /**
     * @param id Mass's id
     * @param x initial x coordinate
     * @param y initial y coordinate
     * @param mass Mass's mass
     */
    public Mass (int id, double x, double y, double mass) {
        myAcceleration = new Force();
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
        applyForce(getGravity());
        applyForce(getBounce(canvas.getSize()));
        // convert force back into Mover's velocity
        getVelocity().sum(myAcceleration);
        myPrevAcc = new Force (myAcceleration);
        myAcceleration.reset();
        // move mass by velocity
        myCenter.setLocation(myCenter.getX() + myVelocity.getXChange() * dt,
                myCenter.getY() + myVelocity.getYChange() * dt);
    }

    /**
     * @param f
     *        force to be applied to this mass
     */
    public void applyForce (Force f) {
        myAcceleration.sum(f);
    }

    /**
     * @param id check if two Masses have the same id
     */
    public boolean match (int id) {
        return myID == id;
    }

    // add gravity towards bottom
    private Force getGravity () {
        Force result = new Force(DOWN, GRAVITY);
        return result;
    }

    // check for move out of bounds
    private Force getBounce (Dimension bounds) {
        Force impulse = new Force();
        if (getLeft() < 0) {
            impulse = new Force(RIGHT, BOUNCE_SCALE);
            setCenter(getSize().width / 2, getCenter().getY());
        }
        else if (getRight() > bounds.width) {
            impulse = new Force(LEFT, BOUNCE_SCALE);
            setCenter(bounds.width - getSize().width / 2, getCenter().getY());
        }
        if (getTop() < 0) {
            impulse = new Force(UP, BOUNCE_SCALE);
            setCenter(getCenter().getX(), getSize().height / 2);
        }
        else if (getBottom() > bounds.height) {
            impulse = new Force(DOWN, BOUNCE_SCALE);
            setCenter(getCenter().getX(), bounds.height - getSize().height / 2);
        }
        impulse.scale(getVelocity().getRelativeMagnitude(impulse));
        return impulse;
    }

    /**
     * Returns shape's velocity.
     */
    public Force getVelocity () {
        return myVelocity;
    }

    /**
     * Resets shape's velocity.
     * @param direction a double
     * @param magnitude another double
     */
    public void setVelocity (double direction, double magnitude) {
        myVelocity = new Force(direction, magnitude);
    }

    public void setVelocity (Force v) {
        myVelocity = v;
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
    public Force getAcceleration () {
        return myAcceleration;
    }

    /**
     * Sets shape's acceleration.
     * @param a acceleration
     */
    public void setAcceleration (Force a) {
        myAcceleration = a;
    }

    /**
     * Returns shape's mass.
     */
    public double getMass () {
        return myMass;
    }

    /**
     * Calculate the force acting on a mass.
     */
    public Force calcForce () {
        Force f = new Force(myVelocity);
        f.scale(myMass);
        return f;
    }
    
    public Force getPrevAcc() {
        return myPrevAcc;
    }
}
