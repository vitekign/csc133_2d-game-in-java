package a2.app.commands; /**
 * Created by Victor Ignatenkov on 3/15/15.
 */

import a2.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddOilSlick extends AbstractAction {


    private GameWorld gw;

    private AddOilSlick(){
    }

    private static AddOilSlick addOilSlick = new AddOilSlick();

    public static AddOilSlick getInstance(){
        return addOilSlick;
    }


    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.addOilSlick();
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