import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Aaron on 21/10/2015.
 */
public class Main extends JFrame{
    private JButton fullscreen;
    private JButton close;
    private JButton settings;
    public static JPanel menuBox = new JPanel();

    public static ArrayList<JButton> menu;
    JTextField timeBox;
    JTextField startBox;

    private JFrame options;
    private ArrayList<JPanel> rows;

    public static int drinkingTime;
    public static int startTime;

    private boolean Am_I_In_FullScreen;
    private int PrevX,PrevY,PrevWidth,PrevHeight;
    Board board;

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main ex = new Main();
                ex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ex.setSize(1366, 768);
                ex.setVisible(true);
            }
        });
    }

    public Main() {
        menu = new ArrayList<JButton>();
        board = new Board();
        Am_I_In_FullScreen = false;
        setContentPane(board);
        pack();

        settings = new JButton("Settings");
        menu.add(settings);
        settings.addActionListener(new SettingsListener());

        fullscreen = new JButton("Fullscreen");
        menu.add(fullscreen);
        FullScreenEffect effect = new FullScreenEffect();
        fullscreen.addActionListener(effect);

        close = new JButton("Close");
        menu.add(close);
        close.addActionListener(new CloseListener());


        JPanel menuBox = makeRow(settings, fullscreen, close);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridy = 0;
        board.add(menuBox,c);

        settings.setVisible(true);
        fullscreen.setVisible(true);
        close.setVisible(true);



        rows = new ArrayList<JPanel>();

        drinkingTime = 100;
        startTime = 0;

    }



    private class FullScreenEffect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {


            if(Am_I_In_FullScreen == false){

                PrevX = getX();
                PrevY = getY();
                PrevWidth = getWidth();
                PrevHeight = getHeight();

                dispose();

                setUndecorated(true);

                setBounds(0,0,getToolkit().getScreenSize().width,getToolkit().getScreenSize().height);
                setVisible(true);
                Am_I_In_FullScreen = true;
            }
            else{
                setVisible(true);

                setBounds(PrevX, PrevY, PrevWidth, PrevHeight);
                dispose();
                setUndecorated(false);
                setVisible(true);
                Am_I_In_FullScreen = false;
            }
        }
    }
    private class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class SettingsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            rows = new ArrayList<JPanel>();
            options = new JFrame();

            JLabel time = new JLabel("Centurion Length:");
            time.setForeground(Color.white);
            timeBox = new JTextField("" + drinkingTime,3);
            rows.add(makeRow(time,timeBox));

            JLabel start = new JLabel("Start Time:");
            start.setForeground(Color.white);
            startBox = new JTextField("" + startTime,3);
            rows.add(makeRow(start,startBox));




            options.setLayout(new GridLayout(rows.size() + 1, 0));

            for(JPanel row : rows){
                options.add(row);
            }

            JButton okay = new JButton("Okay");
            okay.addActionListener(new OkayListener());
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new CancelListener());
            options.add(makeRow(okay,cancel));

            options.setSize(300, 200);
            options.pack();
            options.setResizable(false);
            options.setVisible(true);


        }
    }

    private static JPanel makeRow(Component... components){
        JPanel row = new JPanel();
        row.setLayout(new FlowLayout());
        row.setBackground(Color.black);
        for(Component component : components){
            row.add(component);
        }
        return row;
    }

    private class OkayListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                startTime = Integer.parseInt(startBox.getText());
                drinkingTime = Integer.parseInt(timeBox.getText());
                options.setVisible(false);
            }catch (NumberFormatException exception){
                JOptionPane.showMessageDialog(null,"Numbers Plz");
            }
        }
    }

    private class CancelListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            options.setVisible(false);
        }
    }

    private class ResetListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
