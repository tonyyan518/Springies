package physicalObject;
import simulator.Simulation;

/**
 * @author tyy
 */
public class Muscle extends Spring {
    private double myAmplitude;
    private double myOriginalLength;
    private double myTime;
    /**
     * @param start one end of the spring
     * @param end the other end of the spring
     * @param length the rest length
     * @param kVal the spring constant
     * @param amp the amplitude
     */
    public Muscle (Mass start, Mass end, double length, double kVal, double amp) {
        super(start, end, length, kVal);
        myAmplitude = amp;
        myOriginalLength = length;
        myTime = 0;
    }
    @Override
    public void update (Simulation canvas, double dt) {
        super.update(canvas, dt);
        myTime += dt;
        setLength(myOriginalLength + myAmplitude * Math.sin(myTime));
    }
}
