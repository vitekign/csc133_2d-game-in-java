package a1.commands;

/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

import a1.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/8/15.
 */
public class CollideWithPylon extends AbstractAction {


    private GameWorld gw;

    private CollideWithPylon(){
    }

    private static CollideWithPylon collideWithPylon = new CollideWithPylon();

    public static CollideWithPylon getInstance(){
        return collideWithPylon;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            try {
                int input = proccessInput();
                gw.carCollideWithPylon(input);
            } catch (Exception ex) {
                if(ex.getMessage() != "null") {
                    JOptionPane.showMessageDialog(null, "Please enter a number");
                }
            }
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }

    private int proccessInput() throws Exception{
        String inputString = JOptionPane.showInputDialog("Please input a pylon number");
        return Integer.parseInt(inputString);
    }

    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}
