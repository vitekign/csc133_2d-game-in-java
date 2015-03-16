/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

package a1.commands;
import a1.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class TurnOnSound extends AbstractAction {


    private GameWorld gw;

    private TurnOnSound(){
    }

    private static TurnOnSound turnOnSound = new TurnOnSound();

    public static TurnOnSound getInstance(){
        return turnOnSound;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.switchSound();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}