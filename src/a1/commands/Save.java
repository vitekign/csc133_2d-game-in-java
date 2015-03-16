/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a1.commands;
import a1.model.GameWorld;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("\nCommand Save Was Clicked");
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}