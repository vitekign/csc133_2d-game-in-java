/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a2.commands;

import a2.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class Accelerate extends AbstractAction {


    private GameWorld gw;

    private Accelerate(){
        super("Accelerate");
    }

    private static Accelerate accelerate = new Accelerate();

    public static Accelerate getInstance(){
        return accelerate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.accelerate();
            //TODO get rid of sout
            System.out.println("Accelerate");
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}