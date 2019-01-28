package mechanics;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class Main
 *
 * Functionalities: Main class which sets everything up and runs the game.
 */
public class Main {
    public static void main(String[] args) {
        final Board board = new Board(10,10);
        final GameFrame frame = new GameFrame(board);
        frame.setSize(800,720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        final Action doOneStep = new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                frame.tick();
            }
        };

        final Timer clockTimer = new Timer (1000, doOneStep);
        clockTimer.setCoalesce(true);
        clockTimer.start();
    }
}
