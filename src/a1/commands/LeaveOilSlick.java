package a1.commands;

import a1.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
public class LeaveOilSlick extends AbstractAction {


    private GameWorld gw;

    private LeaveOilSlick(){
    }

    private static LeaveOilSlick leaveOilSlick = new LeaveOilSlick();

    public static LeaveOilSlick getInstance(){
        return leaveOilSlick;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.leaveOilSlick();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}