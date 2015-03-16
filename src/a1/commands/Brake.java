/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

package a1.commands;
import a1.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class Brake extends AbstractAction {


    private GameWorld gw;

    private Brake(){
        super("Brake");
    }

    private static Brake brake = new Brake();

    public static Brake getInstance(){
        return brake;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.brake();
            //TODO get rid of sout
            System.out.println("Brake");
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}