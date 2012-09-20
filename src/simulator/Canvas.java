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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.Timer;


/**
 * Creates an component that is a viewer onto an animation.
 * @author Robert C. Duvall
 * modified by tyy
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
    
    private final int UNIT_CHANGE_IN_PIXELS = 10;
    private final int ORIGINAL_WIDTH = 800;
    private final int ORIGINAL_HEIGHT = 800;
    private final int MINIMUM_WIDTH = 400;
    private static final JFileChooser CHOOSER = new JFileChooser(System
            .getProperties().getProperty("user.dir"));
    // user's game to be animated
    private Simulation myTarget;
    // drives simulation
    private Timer myTimer;
    // input state
    private int myLastKeyPressed;
    // only one so that it maintains user's preferences
    private Point myLastMousePosition;
    //the size of canvas that is to be changed by UP and DOWN keys
    private Dimension mySize;
    //the x, y value of the top-left origin point
    private Point originPoint;
    

    /**
     * Initializes the canvas.
     * @param size of the canvas
     */
    public Canvas (Dimension size) {
        originPoint = new Point(0, 0);
        mySize = size;
        // request component size
        setPreferredSize(mySize);
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
        // initialize simulation
        myTarget = new Simulation(this);
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
        myTarget.update(elapsedTime);
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
        pen.setColor(Color.PINK);
        int paintX = Math.max(originPoint.x, 0);
        int paintY = Math.max(originPoint.y, 0);
        int paintWidth = Math.min(mySize.width, ORIGINAL_WIDTH);
        int paintHeight = Math.min(mySize.height, ORIGINAL_HEIGHT);
        pen.fillRect(paintX, paintY, paintWidth, paintHeight);
        myTarget.paint((Graphics2D) pen);
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
        });
    }

    private void loadModel () {
        final Factory factory = new Factory();
        int response = CHOOSER.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            factory.loadModel(myTarget, CHOOSER.getSelectedFile());
        }
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
                System.out.println(myTarget);
                break;
            case KeyEvent.VK_N:
                //
            case KeyEvent.VK_C:
            case KeyEvent.VK_G:
            case KeyEvent.VK_1:
            case KeyEvent.VK_UP: 
                //increase in size 
                changeSize(UNIT_CHANGE_IN_PIXELS);
                break;
            case KeyEvent.VK_DOWN:
                //decrease in size
                changeSize(-UNIT_CHANGE_IN_PIXELS);
                break;
            default:
                // good style
                break;
        }
    }
    
    /**
     * To make the size of the walled area increase by numberOfPixels on each side.
     * @param numberOfPixels
     */
    private void changeSize(int numberOfPixels) {
        if (mySize.width + 2*numberOfPixels <= MINIMUM_WIDTH) {
            System.out.println("The canvas size reaches minimum.");
            return;
        }
        mySize.setSize(mySize.width + 2*numberOfPixels, mySize.height + 2*numberOfPixels);
        if (mySize.width >= ORIGINAL_WIDTH)
            mySize.setSize(ORIGINAL_WIDTH, mySize.height);
        if (mySize.height >= ORIGINAL_HEIGHT)
            mySize.setSize(mySize.width, ORIGINAL_HEIGHT);
        changeOrigin(numberOfPixels);
    }
    
    private void changeOrigin(int numberOfPixels) {
        originPoint.x -= numberOfPixels;
        originPoint.y -= numberOfPixels;
    }
    
    public Point getOrigin() {
        return originPoint;
    }
    
    public Dimension getCanvasSize() {
        return mySize;
    }
}
