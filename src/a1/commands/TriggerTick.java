package a1.commands;

import a1.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/8/15.
 */
public class TriggerTick extends AbstractAction {


    private GameWorld gw;

    private TriggerTick(){
        super("TriggerTick");
    }



    private static TriggerTick TriggerTick = new TriggerTick();

    public static TriggerTick getInstance(){
        return TriggerTick;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.makeTick();

        } else {
            System.out.println("The target is not set up");
        }

    }

    public void setTarget(GameWorld gw){

        this.gw = gw;
    }
}
