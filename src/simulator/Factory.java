package simulator;
import environment.CenterMass;
import environment.Gravity;
import environment.Viscosity;
import environment.WallRepulsion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import physicalobject.Mass;
import physicalobject.Spring;

/**
 * @author Robert C. Duvall
 * modified by tyy, Rex
 */
public class Factory {
    private static final String MASS = "mass";
    private static final String SPRING = "spring";
    private static final String MUSCLE = "muscle";
    private static final String GRAVITY = "gravity";
    private static final String VISCOSITY = "viscosity";
    private static final String CENTER_OF_MASS  = "centermass";
    private static final String WALL = "wall";

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
                        sim.add(Mass.createMass(line));
                    }
                    else if (type.equals(SPRING) || type.equals(MUSCLE)) {
                        sim.add(Spring.createSpring(line, sim, type));
                    }
                    else if (type.equals(GRAVITY)) {
                        sim.add(Gravity.createGravity(line));
                    }
                    else if (type.equals(VISCOSITY)) {
                        sim.add(Viscosity.createViscosity(line));
                    }
                    else if (type.equals(CENTER_OF_MASS)) {
                        sim.add(CenterMass.createCenterMass(line));
                    }
                    else if (type.equals(WALL)) {
                        sim.add(WallRepulsion.createWallRepulsion(line));
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
}
