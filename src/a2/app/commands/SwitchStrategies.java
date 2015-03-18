/**
 * Created by Victor Ignatenkov on 3/16/15.
 */

package a2.app.commands;
import a2.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class SwitchStrategies extends AbstractAction {


    private GameWorld gw;

    private SwitchStrategies(){
    }

    private static SwitchStrategies switchStrategies = new SwitchStrategies();

    public static SwitchStrategies getInstance(){
        return switchStrategies;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.switchStrategies();
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