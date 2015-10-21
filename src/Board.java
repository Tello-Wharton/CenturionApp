import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Aaron on 21/10/2015.
 */
public class Board extends JPanel implements ActionListener{
    private static final int DELAY = 25;
    private static boolean showMenu = true;
    private static int mouseMotion = 5000;
    private Timer timer;





    public Board() {
        addMouseMotionListener(new MAdapter());

        setBackground(Color.black);
        setFocusable(true);
        setDoubleBuffered(true);

        initApp();

    }

    public void initApp(){
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouseMotion -= DELAY;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {


        Toolkit.getDefaultToolkit().sync();
    }



    private class MAdapter extends MouseAdapter {
        @Override
        public void mouseMoved(MouseEvent e){
            mouseMotion = 5000;

        }

    }
}
