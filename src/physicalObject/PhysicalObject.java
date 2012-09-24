package physicalobject;
import java.awt.Graphics2D;
import simulator.Simulation;

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
