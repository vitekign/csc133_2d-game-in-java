/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a2.commands;
import a2.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class TurnRight extends AbstractAction {


    private GameWorld gw;

    private TurnRight(){
        super("Turn Right");
    }

    private static TurnRight turnRight = new TurnRight();

    public static TurnRight getInstance(){
        return turnRight;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.changeSteeringToRight();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}