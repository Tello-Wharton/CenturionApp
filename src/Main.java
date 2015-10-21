import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Aaron on 21/10/2015.
 */
public class Main extends JFrame{
    private JButton fullscreenButton;
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
        board = new Board();
        Am_I_In_FullScreen = false;
        setContentPane(board);
        pack();

        fullscreenButton = new JButton("Fullscreen");



        FullScreenEffect effect = new FullScreenEffect();
        fullscreenButton.addActionListener(effect);
        //fullscreenButton.setLocation(100, 100);



        board.add(fullscreenButton);
        fullscreenButton.setVisible(true);


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
}
