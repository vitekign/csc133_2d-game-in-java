/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a3.app.commands;
import a3.model.GameWorld;

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

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.changeSteeringToRight();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }


    /**
     * Supply the target, so the command has the
     * knowledge of the all parts it needs to operates on.
     * @param gw
     */
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}