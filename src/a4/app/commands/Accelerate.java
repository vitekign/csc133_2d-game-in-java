/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a4.app.commands;
import a4.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Accelerate command.
 * Accelerate the Car.
 */


public class Accelerate extends AbstractAction {

    private GameWorld gw;

    private Accelerate(){
        super("Accelerate");
    }

    private static Accelerate accelerate = new Accelerate();

    public static Accelerate getInstance(){
        return accelerate;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.accelerate();
        } else {
            System.out.println("The target for " + this.getClass().getName() + " is not set up");
        }
    }

    /**
     * Supply a target, so the command can
     * call the actual implementation of the
     * corresponding command.
     */
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}