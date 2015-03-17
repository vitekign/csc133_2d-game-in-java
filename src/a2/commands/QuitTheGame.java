package a2.commands;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/8/15.
 */
public class QuitTheGame extends AbstractAction {
    private QuitTheGame(){
        super("Quit");
    }

    private static QuitTheGame quitTheGame = new QuitTheGame();

    public static QuitTheGame getInstance(){
        return quitTheGame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exist?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION){
            System.out.println("Exiting the game...");
            System.exit(0);
        }
        return;



    }
}
