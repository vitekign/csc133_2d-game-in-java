/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a2.app.commands;
import a2.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class ShowAbout extends AbstractAction {


    private GameWorld gw;

    private ShowAbout(){
        super("About");
    }

    private static ShowAbout showAbout = new ShowAbout();

    public static ShowAbout getInstance(){
        return showAbout;
    }

    /**
     * Provide the functionality for the showAbout command.
     * Basically, it shows the windows with some information
     * about a student.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
       JOptionPane.showMessageDialog(null, "Victor Ignatenkov\nCSC 133\nVersion: 2");
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