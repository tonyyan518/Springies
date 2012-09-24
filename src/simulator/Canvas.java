package simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import physicalobject.Mass;

/**
 * Creates an component that is a viewer onto an animation.
 * @author Robert C. Duvall
 *         modified by tyy, Rex
 */
@SuppressWarnings("serial")
public class Canvas extends JComponent {
    /**
     * no key pressed.
     */
    public static final int NO_KEY_PRESSED = -1;
    /**
     * 1000 milliseconds in 1 second.
     */
    public static final int ONE_SECOND = 1000;
    /**
     * animate 25 times per second if possible (in milliseconds).
     */
    public static final int FRAMES_PER_SECOND = 25;

    private static final int UNIT_CHANGE_IN_PIXELS = 10;
    private static final int ORIGINAL_WIDTH = 600;
    private static final int ORIGINAL_HEIGHT = 600;
    private static final int MINIMUM_WIDTH = 300;
    private static final JFileChooser CHOOSER =
            new JFileChooser(System.getProperties().getProperty("user.dir"));
    // the size of canvas that is to be changed by UP and DOWN keys
    private static Dimension ourSize;
    // the x, y value of the top-left origin point
    private static Point ourOriginPoint;
    // whether the global forces file is added
    // user's game to be animated
    // a series of simulations
    private ArrayList<Simulation> myTargets = new ArrayList<Simulation>();
    // drives simulation
    private Timer myTimer;
    // input state
    private int myLastKeyPressed;
    // only one so that it maintains user's preferences
    private Point myLastMousePosition;
    private boolean myGlobalForcesApplied;
    // the mass that's controlled by the mouse
    private Mass myControlledMass;

    /**
     * Initializes the canvas.
     * @param size of the canvas
     */
    public Canvas (Dimension size) {
        myControlledMass = null;
        myGlobalForcesApplied = false;
        ourOriginPoint = new Point(0, 0);
        ourSize = size;
        // request component size
        setPreferredSize(ourSize);
        // set component to receive user input
        setInputListeners();
        setFocusable(true);
        requestFocus();
        // create timer to drive the animation
        myTimer = new Timer(ONE_SECOND / FRAMES_PER_SECOND,
                new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent e) {
                        step((double) FRAMES_PER_SECOND / ONE_SECOND);
                    }
                });
        loadModel();
    }

    /**
     * Starts the applet's action, i.e., starts the animation.
     */
    public void start () {
        myTimer.start();
    }

    /**
     * Take one step in the animation.
     * @param elapsedTime how much time has elapsed
     */
    public void step (double elapsedTime) {
        for (Simulation s : myTargets) {
            s.update(elapsedTime);
        }
        // indirectly causes paint to be called
        repaint();
    }

    /**
     * Stops the applet's action, i.e., the animation.
     */
    public void stop () {
        myTimer.stop();
    }

    /**
     * Returns the last key pressed by the player (or -1 if none pressed).
     * @see java.awt.event.KeyEvent
     */
    public int getLastKeyPressed () {
        return myLastKeyPressed;
    }

    /**
     * Returns the last position of the mouse in the canvas.
     */
    public Point getLastMousePosition () {
        return myLastMousePosition;
    }

    /**
     * Paint the contents of the canvas.
     * Never called by you directly, instead called by Java runtime
     * when area of screen covered by this container needs to be
     * displayed (i.e., creation, uncovering, change in status)
     * @param pen used to paint shape on the screen
     */
    @Override
    public void paintComponent (Graphics pen) {
        pen.setColor(Color.LIGHT_GRAY);
        int paintX = Math.max(ourOriginPoint.x, 0);
        int paintY = Math.max(ourOriginPoint.y, 0);
        int paintWidth = Math.min(ourSize.width, ORIGINAL_WIDTH);
        int paintHeight = Math.min(ourSize.height, ORIGINAL_HEIGHT);
        pen.fillRect(paintX, paintY, paintWidth, paintHeight);
        for (Simulation s : myTargets) {
            s.paint((Graphics2D) pen);
        }
    }

    /**
     * Create listeners that will update state based on user input.
     */
    private void setInputListeners () {
        myLastKeyPressed = NO_KEY_PRESSED;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {
                myLastKeyPressed = e.getKeyCode();
                manageSimulation(myLastKeyPressed);
            }

            @Override
            public void keyReleased (KeyEvent e) {
                myLastKeyPressed = NO_KEY_PRESSED;
            }
        });
        myLastMousePosition = new Point();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved (MouseEvent e) {
                myLastMousePosition = e.getPoint();
            }
            @Override
            public void mouseDragged (MouseEvent e) {
                if (myControlledMass != null) {
                    Point targetPosition = new Point(e.getPoint());
                    //regulatePointWithinFrame(targetPosition);
                    myControlledMass.setCenter(targetPosition);
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed (MouseEvent e) {
                highlight(myLastMousePosition);
            }
            @Override
            public void mouseReleased (MouseEvent e) {
                if (myControlledMass != null) {
                    myControlledMass.changeToDefaultColor();
                    myControlledMass = null;
                }
            }
        });
    }

    private void highlight(Point mousePosition) {
        for (Simulation s: myTargets) {
            myControlledMass = s.highlight(mousePosition);
        }
        if (myControlledMass != null) {
            myControlledMass.highlight();
        }
    }

    private void loadModel () {
        final Factory factory = new Factory();
        int response = CHOOSER.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            // initialize a new simulation
            myTargets.add(new Simulation(this));
            factory.loadModel(myTargets.get(myTargets.size() - 1),
                    CHOOSER.getSelectedFile());
        }

        if (!myGlobalForcesApplied) {
            int optionalResponse = CHOOSER.showOpenDialog(null);
            if (optionalResponse == JFileChooser.APPROVE_OPTION) {
                myGlobalForcesApplied = true;
                factory.loadModel(myTargets.get(myTargets.size() - 1),
                        CHOOSER.getSelectedFile());
            }
        }
    }

    private void clearModel () {
        myTargets.clear();
    }

    private void manageSimulation (int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_SPACE:
                if (myTimer.isRunning()) {
                    stop();
                }
                else {
                    start();
                }
                break;
            case KeyEvent.VK_S:
                step((double) FRAMES_PER_SECOND / ONE_SECOND);
                break;
            case KeyEvent.VK_P:
                for (Simulation s : myTargets) {
                    System.out.println(s);
                }
                break;
            case KeyEvent.VK_N:
                // load another model
                loadModel();
                break;
            case KeyEvent.VK_C:
                // clear all models
                int myOption = JOptionPane.showConfirmDialog(this,
                        "You seriously want to destroy "
                        + "all the lovely springies?");
                if (myOption == 0) {
                    clearModel();
                }
                break;
            case KeyEvent.VK_G:
                for (Simulation s : myTargets) {
                    s.toggleForce("gravity");
                }
                break;
            case KeyEvent.VK_V:
                for (Simulation s : myTargets) {
                    s.toggleForce("viscosity");
                }
                break;
            case KeyEvent.VK_M:
                for (Simulation s : myTargets) {
                    s.toggleForce("centermass");
                }
                break;
            case KeyEvent.VK_1:
                for (Simulation s : myTargets) {
                    s.toggleForce("topwall");
                }
                break;
            case KeyEvent.VK_2:
                for (Simulation s : myTargets) {
                    s.toggleForce("rightwall");
                }
                break;
            case KeyEvent.VK_3:
                for (Simulation s : myTargets) {
                    s.toggleForce("bottomwall");
                }
                break;
            case KeyEvent.VK_4:
                for (Simulation s : myTargets) {
                    s.toggleForce("leftwall");
                }
                break;
            case KeyEvent.VK_UP:
                // increase the walled area in size
                changeSize(UNIT_CHANGE_IN_PIXELS);
                break;
            case KeyEvent.VK_DOWN:
                // decrease the walled area in size
                changeSize(-UNIT_CHANGE_IN_PIXELS);
                break;
            default:
                // good style
                break;
        }
    }

    /**
     * To make the size of the walled area increase by numberOfPixels on each
     * side.
     * @param numberOfPixels
     */
    private void changeSize (int numberOfPixels) {
        if (ourSize.width + 2 * numberOfPixels <= MINIMUM_WIDTH) {
            System.out.println("The canvas size reaches minimum.");
            return;
        }
        ourSize.setSize(ourSize.width + 2 * numberOfPixels, ourSize.height + 2
                * numberOfPixels);
        changeOrigin(numberOfPixels);
        System.out.println(ourOriginPoint);
        System.out.println(ourSize);
    }

    private void changeOrigin (int numberOfPixels) {
        ourOriginPoint.x -= numberOfPixels;
        ourOriginPoint.y -= numberOfPixels;
    }
    /**
     * gets the origin of our canvas.
     */
    public static Point getOrigin () {
        return ourOriginPoint;
    }
    /**
     * gets the size of our canvas.
     */
    public static Dimension getCanvasSize () {
        return ourSize;
    }
}
