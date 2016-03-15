


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

    private long waitStart;
    private double waitTime;

    private ArrayList<JButton> menu;

    private JLabel countDown;


    private JButton pause;
    private JButton start;

    private boolean startVisible = true;

    public static ArrayList<AutoEntity> entities;

    private JPanel drinkContainer;
    private JLabel drinkText;



    private boolean cake;



    public Board() {
        addMouseMotionListener(new MAdapter());
        setLayout(new GridBagLayout());
        setBackground(Color.black);
        setFocusable(true);
        setDoubleBuffered(true);
        setMinimumSize(new Dimension(1366, 768));
        spentTime = 0;
        waitTime = 4;
        cake = true;

        menu = Main.menu;

        initComponents();
        initApp();

        entities = new ArrayList<AutoEntity>();

    }

    private void initComponents() {



        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;

        c.weighty = 10;
        c.gridy = 3;

        countDown = new JLabel();
        countDown.setFont(new Font(null, Font.PLAIN, 200));
        countDown.setForeground(Color.white);
        add(countDown, c);

        c = new GridBagConstraints();

        pause = new JButton("Pause");
        pause.setVisible(false);
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

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 2;

        drinkContainer = new JPanel();
        drinkContainer.setOpaque(false);
        drinkContainer.setVisible(false);
        drinkText = new JLabel("Drink!");
        drinkText.setFont(new Font(null,Font.PLAIN,300));
        drinkText.setForeground(Color.white);
        drinkText.setVisible(true);
        drinkContainer.add(drinkText);
        add(drinkContainer,c);


    }

    public void initApp(){

        startTime = System.currentTimeMillis();
        waitStart = System.currentTimeMillis();

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

        if((System.currentTimeMillis() - waitStart) > waitTime * 1000){
            waitStart = System.currentTimeMillis();
            entities.add(new AutoEntity(this));
        }

        ArrayList<AutoEntity> tempEntities = new ArrayList<AutoEntity>(entities);
        tempEntities.forEach(AutoEntity::calculate);

        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        for(AutoEntity entity :  entities){
            entity.draw(g);
        }

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
        if (time > 0) {
            int minutes = 0;
            int seconds = 0;
            int milliseconds;

            String mins;
            String secs;
            String mills;

            while (time >= 60000) {
                minutes += 1;
                time -= 60000;
            }
            if (minutes < 10) {
                mins = "00" + minutes;
            } else if (minutes < 100) {
                mins = "0" + minutes;
            } else {
                mins = "" + minutes;
            }

            while (time >= 1000) {
                seconds += 1;
                time -= 1000;
            }
            if (minutes + 1 < Main.drinkingTime) {
                if (seconds == 59) drink();
                if (seconds == 54){
                    drinkContainer.setVisible(false);
                    cake = true;
                }
            }
            if (seconds < 10) {
                secs = "0" + seconds;
            } else {
                secs = "" + seconds;
            }
            milliseconds = (int) time;
            if (milliseconds < 10) {
                mills = "00" + milliseconds;
            } else if (milliseconds < 100) {
                mills = "0" + milliseconds;
            } else {
                mills = "" + milliseconds;
            }
            return mins + ":" + secs + ":" + mills;
        }else {
            drinkText.setText("<html><center>LAST</center><center>DRINK</center><center>:)</center></html>");
            drinkText.setFont(new Font(null, Font.PLAIN, 96));
            drinkContainer.setVisible(true);
            horn();
            return "000:00:000";
        }
    }

    private void drink() {
        if(started){
            drinkContainer.setVisible(true);
            horn();
        }
        else{
            drinkContainer.setVisible(false);
        }
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

    private void horn(){
        if(cake) {
            Thread sound = new Thread(new Sound());
            sound.start();
            cake = false;
        }
    }
}
