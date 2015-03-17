/**
 * Created by Victor Ignatenkov on 3/15/15.
 */

package a2.commands;
import a2.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EnterOilSlick extends AbstractAction {


    private GameWorld gw;

    private EnterOilSlick(){
    }

    private static EnterOilSlick enterOilSlick = new EnterOilSlick();

    public static EnterOilSlick getInstance(){
        return enterOilSlick;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.enterOilSlick();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}