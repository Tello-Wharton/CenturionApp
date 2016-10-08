import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Aaron
 * @author JRIngram (Docs). 
 * Created 21/10/2015.
 * @version 04/10/2016
 */
public class Main extends JFrame{
    private JButton fullscreen;
    private JButton close;
    private JButton settings;
    private JPanel menuBox;
    private GridBagConstraints boxC;

    public static ArrayList<JButton> menu;
    JTextField timeBox;
    JTextField startBox;

    private JFrame options;
    private ArrayList<JPanel> rows;

    public static int drinkingTime;
    public static int startTime;

    public static boolean IS_IN_FULLSCREEN;
    private int PrevX,PrevY,PrevWidth,PrevHeight;
    Board board;

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    
    private boolean playHorn = true;


    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main ex = new Main();
                ex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ex.setSize(WIDTH, HEIGHT);
                ex.setResizable(true);
                ex.setVisible(true);
            }
        });
    }
    
    /**
     * Creates GUI for system; serves as main loop.
     */
    public Main() {
        menu = new ArrayList<JButton>();
        IS_IN_FULLSCREEN = false;

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


        menuBox = makeRow(settings, fullscreen, close);
        boxC = new GridBagConstraints();
        boxC.anchor = GridBagConstraints.NORTH;
        boxC.gridy = 0;

        settings.setVisible(true);
        fullscreen.setVisible(true);
        close.setVisible(true);


        rows = new ArrayList<JPanel>();

        drinkingTime = 100;
        startTime = 0;

        initBoard();
    }


    /**
     * Changes the Screen size so that it is full screen.
     * @author Aaron
     *
     */
    private class FullScreenEffect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {


            if(IS_IN_FULLSCREEN == false){

                PrevX = getX();
                PrevY = getY();
                PrevWidth = getWidth();
                PrevHeight = getHeight();

                dispose();

                setUndecorated(true);

                setBounds(0,0,getToolkit().getScreenSize().width,getToolkit().getScreenSize().height);
                setVisible(true);
                IS_IN_FULLSCREEN = true;
            }
            else{
                setVisible(true);

                setBounds(PrevX, PrevY, PrevWidth, PrevHeight);
                dispose();
                setUndecorated(false);
                setVisible(true);
                IS_IN_FULLSCREEN = false;
            }
        }
    }
    
    /**
     * Closes the System.
     * @author Aaron
     */
    private class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    
    /**
     * Creates a UI for the user to change the Centurion Settings.
     * @author Aaron
     * @author JRIngram
     *
     */
    private class SettingsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            rows = new ArrayList<JPanel>();
            options = new JFrame("Centurion Settings");

            JPanel optionBoard = new JPanel();
            optionBoard.setBackground(Color.black);
            
            //Creates Centurion Length UI.
            JLabel time = new JLabel("Centurion Length (mins):");
            time.setForeground(Color.white);
            timeBox = new JTextField("" + drinkingTime,3);
            rows.add(makeRow(time,timeBox));
            
            //Creates start time UI.
            JLabel start = new JLabel("Start Time:");
            start.setForeground(Color.white);
            start.setToolTipText("How many minutes into the Centurion you want to start from.");
            startBox = new JTextField("" + startTime,3);
            rows.add(makeRow(start,startBox));
            
            //Choose whether the horn sound plays each minutes.
            JLabel playHornLabel = new JLabel("Horn Sound: ");
            playHornLabel.setForeground(Color.white);
            playHornLabel.setToolTipText("Select whether the horn sound plays each minute.");
            JCheckBox hornBox = new JCheckBox();
            hornBox.setSelected(playHorn);
            hornBox.setBackground(Color.black);
            
            hornBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					playHorn = hornBox.isSelected();
					board.setPlayHorn(playHorn);
				}  	
            });
            
            rows.add(makeRow(playHornLabel, hornBox));
            
            
            optionBoard.setLayout(new GridLayout(rows.size() + 1, 0));

            for(JPanel row : rows){
                optionBoard.add(row);
            }
            
            //Creates Okay, Reset and Cancel Buttons.
            JButton okay = new JButton("Okay");
            okay.setToolTipText("Confirms Settings.");
            
            okay.addActionListener(new OkayListener());
            
            JButton reset = new JButton("Reset");
            reset.setToolTipText("Resets clock to previous start time.");
            reset.addActionListener(new ResetListener());
            JButton cancel = new JButton("Cancel");
            cancel.setToolTipText("Closes Settings Dialog without updating settings.");
            cancel.addActionListener(new CancelListener());
            optionBoard.add(makeRow(okay,reset,cancel));


            options.add(optionBoard);
            options.setSize(300, 200);
            options.pack();
            options.setResizable(false);
            options.setVisible(true);


        }
    }
    
    /**
     * Creates a row of components.
     * @param components
     * @return A row of components in a panel.
     */
    private static JPanel makeRow(Component... components){
        JPanel row = new JPanel();
        row.setLayout(new FlowLayout());
        //row.setBackground(Color.black);
        row.setOpaque(false);
        for(Component component : components){
            row.add(component);
        }
        return row;
    }
    
    /**
     * Updates startTime and drinkingTime.
     * @author Aaron
     *
     */
    private class OkayListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                startTime = Integer.parseInt(startBox.getText());
                drinkingTime = Integer.parseInt(timeBox.getText());
                options.setVisible(false);
                board.setPlayHorn(playHorn);
            }catch (NumberFormatException exception){
                JOptionPane.showMessageDialog(null,"Numbers Plz");
            }
        }
    }
    
    /**
     * Closes Settings GUI.
     * @author Aaron
     *
     */
    private class CancelListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            options.setVisible(false);
        }
    }
    
    /**
     *	Recalls initBoard if alerted. 
     */
    private class ResetListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            initBoard();
        }
    }
    
    /**
     * Creates the board/gui for the program.
     */
    private void initBoard(){
        board = new Board();
        setContentPane(board);
        board.add(menuBox, boxC);
    }

}
