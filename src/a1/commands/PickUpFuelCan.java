package a1.commands;

/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

import a1.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;



/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
public class PickUpFuelCan extends AbstractAction {


    private GameWorld gw;

    private PickUpFuelCan(){
    }

    private static PickUpFuelCan pickUpFuelCan = new PickUpFuelCan();

    public static PickUpFuelCan getInstance(){
        return pickUpFuelCan;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.pickUpFuelCan();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}