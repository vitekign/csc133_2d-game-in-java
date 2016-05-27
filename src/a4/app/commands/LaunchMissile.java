package a4.app.commands;

import a4.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 1/23/16.
 */
public class LaunchMissile extends AbstractAction {


    private GameWorld gw;

    private LaunchMissile(){
    }

    private static LaunchMissile launchMissile = new LaunchMissile();

    public static LaunchMissile getInstance(){
        return launchMissile;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.launchMissile();
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