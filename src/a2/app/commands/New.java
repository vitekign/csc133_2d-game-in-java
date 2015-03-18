/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

package a2.app.commands;
import a2.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class New extends AbstractAction {


    private GameWorld gw;

    private New(){
    }

    private static New commandNew = new New();

    public static New getInstance(){
        return commandNew;
    }

    /**
     * Show that the New command was triggered.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("\nCommand New Was Clicked");
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