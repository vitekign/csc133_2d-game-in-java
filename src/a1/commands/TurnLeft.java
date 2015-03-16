/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

package a1.commands;
import a1.model.GameWorld;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.changeSteeringToLeft();
            //TODO get rid of sout
            System.out.println("Turn left");
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}