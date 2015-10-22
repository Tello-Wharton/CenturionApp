

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Aaron on 21/10/2015.
 */
public class Board extends JPanel implements ActionListener{
    private static final int DELAY = 25;
    private static int mouseMotion = 5000;
    private Timer timer;
    private boolean started = false;
    private long startTime;
    private long timeRemaining;
    private long spentTime;


    private ArrayList<JButton> menu;

    private JLabel countDown;
    private JButton pause;
    private JButton start;

    private boolean startVisible = true;


    public Board() {
        addMouseMotionListener(new MAdapter());
        setLayout(new GridBagLayout());
        setBackground(Color.black);
        setFocusable(true);
        setDoubleBuffered(true);

        spentTime = 0;

        menu = Main.menu;

        initComponents();
        initApp();

    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;

        c.weighty = 10;
        c.gridy = 2;

        countDown = new JLabel();
        countDown.setFont(new Font(null, Font.PLAIN, 200));
        countDown.setForeground(Color.white);
        add(countDown, c);

        c = new GridBagConstraints();

        pause = new JButton("Pause");
        pause.addActionListener(new PauseListener());
        pause.setFont(new Font(null, Font.PLAIN, 48));
        c.anchor = GridBagConstraints.CENTER;

        c.gridy = 1;
        add(pause, c);

        c = new GridBagConstraints();

        start = new JButton("Start");
        start.addActionListener(new StartListener());
        start.setFont(new Font(null, Font.PLAIN, 48));
        c.anchor = GridBagConstraints.CENTER;

        c.gridy = 1;



        add(start, c);


    }

    public void initApp(){

        startTime = System.currentTimeMillis();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        mouseMotion -= DELAY;
        if(mouseMotion < 0)menuVisible(false);

        if(started) {
            timeRemaining = startTime + (Main.drinkingTime - Main.startTime) * 60000 - System.currentTimeMillis() - spentTime;
            countDown.setText(processClock(timeRemaining));
        }else {
            countDown.setText(processClock((Main.drinkingTime - Main.startTime)* 60000 - spentTime));
        }
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
            menuVisible(true);

        }

    }

    private void menuVisible(boolean b){
        for(JButton button : menu){
            button.setVisible(b);
        }
        if(b){
            start.setVisible(startVisible);
            pause.setVisible(!startVisible);
        }else{
            start.setVisible(false);
            pause.setVisible(false);
        }

    }

    private String processClock(long time){
            int minutes = 0;
            int seconds = 0;
            int milliseconds;

            String mins;
            String secs;
            String mills;

            while (time >= 60000){
                minutes+=1;
                time -= 60000;
            }
            if(minutes < 10){
                mins = "00" + minutes;
            }else if(minutes < 100){
                mins = "0" + minutes;
            }
            else{
                mins = "" + minutes;
            }

            while (time >= 1000) {
                seconds += 1;
                time -= 1000;
            }
            if(seconds < 10){
                secs = "0"+seconds;
            }else {
                secs = "" + seconds;
            }
            milliseconds = (int) time;
            if(milliseconds < 10){
                mills = "00" + milliseconds;
            }else if(milliseconds < 100){
                mills = "0" + milliseconds;
            }
            else{
                mills = "" + milliseconds;
            }
        return mins+":"+secs+":"+mills;
    }

    private class StartListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            startTime = System.currentTimeMillis();
            startVisible = false;
            started = true;
        }
    }
    private class PauseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            spentTime = (Main.drinkingTime - Main.startTime)* 60000 - timeRemaining;
            startVisible = true;
            started = false;
        }
    }
}
