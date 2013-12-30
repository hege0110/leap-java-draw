package leap;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.leapmotion.leap.Controller;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setSize(500, 500);
                final Canvas canvas = new Canvas(500, 500);
                frame.add(canvas);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                Thread t = new Thread(new Runnable() {
                    public void run() {

                        // Create a sample listener and controller
                        DrawListener listener = new DrawListener(canvas);
                        Controller controller = new Controller();

                        // Have the sample listener receive events from the
                        // controller
                        controller.addListener(listener);

                        // Keep this process running until Enter is pressed
                        System.out.println("Press Enter to quit...");
                        try {
                            System.in.read();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Remove the sample listener when done
                        controller.removeListener(listener);
                    }
                });
                t.start();
            }
        });
    }
}
