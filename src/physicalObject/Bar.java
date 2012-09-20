package physicalObject;
import simulator.Simulation;
import simulator.Vector;

/**
 * @author tyy
 */
public class Bar extends Spring {

    private double myStartMomentum;
    private double myEndMomentum;
    /**
     * @param start one of the mass
     * @param end the other mass
     * @param length rest length
     * @param kVal the spring constant
     */
    public Bar (Mass start, Mass end, double length, double kVal) {
        super(start, end, length, 0);
    }
    @Override
    public void update (Simulation canvas, double dt) {
        double lenDiff = calcCurrentLen() - getLength();
        Vector bar = new Vector(getStart().getCenter(), getEnd().getCenter());
        if (Math.abs(lenDiff) > AT_REST) {
            myStartMomentum = getSpeed(getStart());
            myEndMomentum = getSpeed(getEnd());
            bar.setMagnitude(lenDiff * myStartMomentum / (myStartMomentum + myEndMomentum));
            getStart().move(bar);
            if (getStart().isMoving()) {
                changeVelocity(getStart());
            }
            bar.negate();
            bar.setMagnitude(lenDiff * myEndMomentum / (myStartMomentum + myEndMomentum));
            getEnd().move(bar);
        }
    }
    private double getSpeed(Mass m) {
        return m.getVelocity().getMagnitude() * m.getMass();
    }
    private void changeVelocity(Mass m) {
        Vector v = new Vector(m.getPrevPos(), m.getCenter());
        m.getVelocity().setDirection(-v.getAngle());
    }
}
