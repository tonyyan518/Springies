import java.awt.geom.Point2D;

/**
 * @author tyy
 */
public class Bar extends Spring {

    public Bar (Mass start, Mass end, double length, double kVal) {
        super(start, end, length, kVal);
    }

    private void perpComponent(Mass m, Mass fixed) {
        double angle, angleV;
        Force bar = new Force(m.getCenter(), fixed.getCenter());
        
        angleV = m.getAcceleration().getAngle();
            
        Force a = new Force(bar.getAngle(), -m.getPrevAcc().getMagnitude()*Math.cos(Math.toRadians(bar.getAngle()-angleV)));
        //System.out.println(bar.getAngle()-angleV);
        //System.out.println(Math.cos(Math.toRadians(bar.getAngle()-angleV)));
        
        Force f = new Force(bar.getAngle(), m.getPrevAcc().getRelativeMagnitude(bar));
        //System.out.println(f.getAngle());
        
        //System.out.println(f.getMagnitude());
        m.applyForce(f);
    }
    
    @Override
    public void update (Simulation canvas, double dt) {
        
        
        
        if (getStart() instanceof FixedMass && getEnd() instanceof FixedMass) {
            
        }
        else if (getStart() instanceof FixedMass) {
            perpComponent(getEnd(), getStart());
        }
        else if (getEnd() instanceof FixedMass) {
            perpComponent(getStart(), getEnd());
        }
        else {
            Point2D start = getStart().getCenter();
            Point2D end = getEnd().getCenter();
            double dx = start.getX() - end.getX();
            double dy = start.getY() - end.getY();

            Force f = getStart().calcForce();
            f.sum(getEnd().calcForce());
            f.scale(1 / (getStart().getMass() + getEnd().getMass()));
            getStart().setVelocity(f.getDirection(), f.getMagnitude());
            getEnd().setVelocity(f.getDirection(), f.getMagnitude());
        }
    }
}
