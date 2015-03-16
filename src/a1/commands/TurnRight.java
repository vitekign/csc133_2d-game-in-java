/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a1.commands;
import a1.model.GameWorld;
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
            //TODO get rid of sout
            System.out.println("Turn right");
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}