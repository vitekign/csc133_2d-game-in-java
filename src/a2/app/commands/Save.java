/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a2.app.commands;
import a2.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class Save extends AbstractAction {


    private GameWorld gw;

    private Save(){
    }

    private static Save commandSave = new Save();

    public static Save getInstance(){
        return commandSave;
    }

    /**
     * Show the command was triggered.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("\nCommand Save Was Clicked");
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