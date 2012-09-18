import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;


/**
 * @author Robert C. Duvall
 */
public class Spring {
    private static final double AT_REST = 0.001;
    private Mass myStart;
    private Mass myEnd;
    private double myLength;
    private double myK;
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
     * @param pen the other pen
     */
    public void paint (Graphics2D pen) {
        int xStart = (int) myStart.getCenter().getX();
        int yStart = (int) myStart.getCenter().getY();
        int xEnd = (int) myEnd.getCenter().getX();
        int yEnd = (int) myEnd.getCenter().getY();
        double dx = xStart - xEnd;
        double dy = yStart - yEnd;
        double len = Math.sqrt(dx * dx + dy * dy) - myLength;

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
     * @param canvas the canvas
     * @param dt change in time
     */
    public void update (Simulation canvas, double dt) {
        Point2D start = myStart.getCenter();
        Point2D end = myEnd.getCenter();
        double dx = start.getX() - end.getX();
        double dy = start.getY() - end.getY();

        // apply hooke's law to each attached mass
        Force f = new Force(Force.angleBetween(dx, dy), myK
                * (myLength - Force.distanceBetween(dx, dy)));

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
