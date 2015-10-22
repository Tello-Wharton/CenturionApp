import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Aaron on 22/10/2015.
 */
public class AutoEntity {

    private int xPos;
    private int yPos;
    private int movement;
    private Board board;
    private Image body;

    private static ArrayList<Image> images = getImages();

    public AutoEntity(Board board){
        this.board = board;

        if(Main.IS_IN_FULLSCREEN){
            yPos = (int) (Math.random()*board.getToolkit().getScreenSize().height);
            xPos = board.getToolkit().getScreenSize().width;
        }else {
            yPos = (int) (Math.random()*Main.HEIGHT);
            xPos = Main.WIDTH;
        }


        if(Math.random() < 0.5){
            xPos = -1000;
            movement = 3;
        }else {
            movement = -3;
        }

        int index = (int) (Math.random()*images.size());
        System.out.println();
        body = images.get(index);


    }

    public void calculate(){
        xPos+=movement;

        if(xPos > 3000 || xPos < -3000){
            board.entities.remove(this);
        }
    }

    public void draw(Graphics g){
        g.drawImage(body,xPos,yPos,board);
    }

    private static ArrayList<Image> getImages(){
        ArrayList<Image> imageList = new ArrayList<Image>();
        try {
            Files.walk(Paths.get("")).forEach(fileName -> {
                if (Files.isRegularFile(fileName)) {
                    if(fileName.toString().endsWith(".png")){
                        ImageIcon ii = new ImageIcon(fileName.toString());
                        imageList.add(ii.getImage());
                    }
                }

            });
            return imageList;

        } catch (IOException e) {
            e.printStackTrace();
            return imageList;
        }
    }
}
