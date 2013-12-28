package draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * Canvas represents a drawing surface that allows the user to draw on it
 * freehand, with the mouse.
 */
public class Canvas extends JPanel {
    // image where the user's drawing is stored
    private Image drawingBuffer;
    Color color;
    BasicStroke stroke;
    /**
     * Make a canvas.
     * 
     * @param width
     *            width in pixels
     * @param height
     *            height in pixels
     */
    public Canvas(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        addDrawingController();
        color = Color.BLACK;
        stroke = new BasicStroke(1);
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window. Have to
        // wait until paintComponent() is first called.
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        try {// Copy the drawing buffer to the screen.
            g.drawImage(drawingBuffer, 0, 0, null);
        } finally {
        }
    }

    public Image getImage() {
        return this.drawingBuffer;
    }

    public void setImage(Image newDrawingBuffer) {
        this.drawingBuffer = newDrawingBuffer;
    }

    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = createImage(getWidth(), getHeight());
        fillWithWhite();
    }

    /*
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        try {
            final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        } finally {
        }

        // IMPORTANT! every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }

    /*
     * Draw a line between two points (x1, y1) and (x2, y2), specified in
     * pixels relative to the upper-left corner of the drawing buffer.
     */
    private void drawLineSegment(int x1, int y1, int x2, int y2) {
        try {
            final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
            g.setColor(Color.BLACK);
            g.drawLine(x1, y1, x2, y2);
        } finally {
        }

        this.repaint();
    }

    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }

    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next
        // mouse event.
        private int lastX, lastY;

        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /*
         * When mouse moves while a button is pressed down, draw a line
         * segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            drawLineSegment(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    public void changeColorAndSize(Color newColor, BasicStroke basicStroke) {
        color = newColor;
        stroke = basicStroke;
    }


public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            JFrame frame = new JFrame();
            frame.setSize(500,500);
            Canvas canvas = new Canvas(500,500);
            frame.add(canvas);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    });
}
}