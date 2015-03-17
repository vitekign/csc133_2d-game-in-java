package a2.commands;

import a2.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/16/15.
 */
public class SwitchStrategies extends AbstractAction {


    private GameWorld gw;

    private SwitchStrategies(){
    }

    private static SwitchStrategies switchStrategies = new SwitchStrategies();

    public static SwitchStrategies getInstance(){
        return switchStrategies;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.switchStrategies();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}