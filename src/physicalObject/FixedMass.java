package physicalObject;
import simulator.Simulation;

/**
 * @author tyy
 */
public class FixedMass extends Mass {
    /**
     * @param id its id
     * @param x its initial x-coordinate
     * @param y its initial y-coordinate
     * @param mass the mass
     */
    public FixedMass (int id, double x, double y, double mass) {
        super(id, x, y, mass);
    }
    @Override
    public void update (Simulation canvas, double dt) { }
}
