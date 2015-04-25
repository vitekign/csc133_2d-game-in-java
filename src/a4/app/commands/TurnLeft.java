/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

package a4.app.commands;
import a4.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class TurnLeft extends AbstractAction {


    private GameWorld gw;

    private TurnLeft(){
        super("Turn Left");
    }

    private static TurnLeft turnLeft = new TurnLeft();

    public static TurnLeft getInstance(){
        return turnLeft;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.changeSteeringToLeft();
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