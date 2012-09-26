package physicalobject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Scanner;
import simulator.Simulation;
import simulator.Vector;

/**
 * @author Robert C. Duvall
 * modified by tyy, Rex
 */
public class Spring extends PhysicalObject {
    protected static final double AT_REST = 0.001;
    private static final String MUSCLE = "muscle";
    private Mass myStart;
    private Mass myEnd;
    private double myLength;
    private double myK;
    private double myDx;
    private double myDy;
    /**
     * @param start one end of the spring
     * @param end the other end of the spring
     * @param length the rest length
     * @param kVal the spring constant
     */
    public Spring (Mass start, Mass end, double length, double kVal) {
        myStart = start;
        myEnd = end;
        myLength = length;
        myK = kVal;
    }

    /**
     * create a Viscosity instance 
     * @param line input to be handled
     * @param sim the Simulation object it belongs to
     * @param type the type of String it is constructing
     * @return A Viscosity object that is a global force
     */
    public static Spring createSpring (Scanner line, Simulation sim, String type) {
        int m1 = line.nextInt();
        int m2 = line.nextInt();
        double restLength = line.nextDouble();
        double ks = line.nextDouble();
        //construct a muscle
        if (type.equals(MUSCLE)) {
            double amp = line.nextDouble();
            return new Muscle(sim.getMass(m1), sim.getMass(m2),
                    restLength, ks, amp);
        }
        //construct a bar
        else if (ks < 0) {
            return new Bar(sim.getMass(m1), sim.getMass(m2),
                    Vector.distanceBetween(sim.getMass(m1).getCenter(),
                    sim.getMass(m2).getCenter()), ks);
        }
        //construct a spring
        return new Spring(sim.getMass(m1), sim.getMass(m2), restLength, ks);
    }

    /**
     * @param pen the other pen
     */
    public void paint (Graphics2D pen) {
        int xStart = (int) myStart.getCenter().getX();
        int yStart = (int) myStart.getCenter().getY();
        int xEnd = (int) myEnd.getCenter().getX();
        int yEnd = (int) myEnd.getCenter().getY();
        myDx = xStart - xEnd;
        myDy = yStart - yEnd;
        double len = Math.sqrt(myDx * myDx + myDy * myDy) - myLength;

        if (Math.abs(len) < AT_REST) {
            pen.setColor(Color.WHITE);
        }
        else if (len < 0.0) {
            pen.setColor(Color.BLUE);
        }
        else {
            pen.setColor(Color.RED);
        }
        pen.drawLine(xStart, yStart, xEnd, yEnd);
    }

    /**
     * calculate the current length of the spring.
     */
    public double calcCurrentLen() {
        Point2D start = myStart.getCenter();
        Point2D end = myEnd.getCenter();
        myDx = start.getX() - end.getX();
        myDy = start.getY() - end.getY();
        return Vector.distanceBetween(myDx, myDy);
    }
    /**
     * @param canvas the canvas
     * @param dt change in time
     */
    public void update (Simulation canvas, double dt) {
        // apply hooke's law to each attached mass
        Vector f = new Vector(Vector.angleBetween(myDx, myDy), myK * (myLength - calcCurrentLen()));

        myStart.applyForce(f);
        f.negate();
        myEnd.applyForce(f);
    }
    /**
     * get one end of the spring.
     */
    public Mass getStart() {
        return myStart;
    }
    /**
     * get the other end.
     */
    public Mass getEnd() {
        return myEnd;
    }
    /**
     * get the length.
     */
    public double getLength() {
        return myLength;
    }
    /**
     * @param length new length
     */
    public void setLength(double length) {
        myLength = length;
    }
}
