/**
 * Created by Victor Ignatenkov on 3/26/15.
 */
package a3.app.commands;
import a3.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class AddFuelCan extends AbstractAction {


    private GameWorld gw;

    private AddFuelCan(){
        super("Add FuelCan");
    }

    private static AddFuelCan addFuelCan = new AddFuelCan();

    public static AddFuelCan getInstance(){
        return addFuelCan;
    }


    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            //add fuelCan
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