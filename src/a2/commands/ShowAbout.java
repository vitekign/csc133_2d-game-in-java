/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
package a2.commands;
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

    @Override
    public void actionPerformed(ActionEvent e) {
       JOptionPane.showMessageDialog(null, "Victor Ignatenkov\nCSC 133\nVersion: 2");
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}