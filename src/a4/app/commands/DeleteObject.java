package a4.app.commands;

/**
 * Created by Victor Ignatenkov on 3/26/15.
 */
/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

import a4.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class DeleteObject extends AbstractAction {

    private GameWorld gw;

    private DeleteObject(){
        super("Delete");
    }

    private static DeleteObject deleteObject = new DeleteObject();

    public static DeleteObject getInstance(){
        return deleteObject;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
         gw.deleteSelectedElements();
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