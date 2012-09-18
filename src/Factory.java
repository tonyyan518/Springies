import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Robert C. Duvall
 * modified by tyy
 */
public class Factory {
    private static final String MASS = "mass";
    private static final String SPRING = "spring";
    private static final String MUSCLE = "muscle";
    /**
     * @param sim the simulation
     * @param modelFile the file
     */
    public void loadModel (Simulation sim, File modelFile) {
        try {
            Scanner input = new Scanner(modelFile);
            while (input.hasNext()) {
                Scanner line = new Scanner(input.nextLine());
                if (line.hasNext()) {
                    String type = line.next();
                    if (type.equals(MASS)) {
                        sim.add(massCommand(line));
                    }
                    else if (type.equals(SPRING) || type.equals(MUSCLE)) {
                        sim.add(springCommand(line, sim, type));
                    }
                }
            }
            input.close();
        }
        catch (FileNotFoundException e) {
            // should not happen because File came from user selection
            e.printStackTrace();
        }
    }

    private Mass massCommand (Scanner line) {
        int id = line.nextInt();
        double x = line.nextDouble();
        double y = line.nextDouble();
        double mass = line.nextDouble();
        if (mass < 0) {
            return new FixedMass(id, x, y, mass);
        }
        return new Mass(id, x, y, mass);
    }

    private Spring springCommand (Scanner line, Simulation sim, String type) {
        int m1 = line.nextInt();
        int m2 = line.nextInt();
        double restLength = line.nextDouble();
        double ks = line.nextDouble();
        if (type.equals(MUSCLE)) {
            double amp = line.nextDouble();
            return new Muscle(sim.getMass(m1), sim.getMass(m2), restLength, ks, amp);
        }
        else if (ks < 0) {
            return new Bar(sim.getMass(m1), sim.getMass(m2), restLength, ks);
        }
        return new Spring(sim.getMass(m1), sim.getMass(m2), restLength, ks);
    }
}
