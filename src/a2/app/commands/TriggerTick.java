package a2.app.commands;

import a2.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/8/15.
 */
public class TriggerTick extends AbstractAction {


    private GameWorld gw;

    private TriggerTick(){
        super();
    }

    private static TriggerTick TriggerTick = new TriggerTick();

    public static TriggerTick getInstance(){
        return TriggerTick;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.makeTick();

        } else {
            System.out.println("The target is not set up");
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
