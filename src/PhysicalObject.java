import java.awt.Graphics2D;

/**
 * @author tyy
 */
public abstract class PhysicalObject {
    /**
     * @param pen the pen
     */
    public abstract void paint (Graphics2D pen);
    /**
     * @param canvas the canvas
     * @param dt change in time
     */
    public abstract void update (Simulation canvas, double dt);
}
