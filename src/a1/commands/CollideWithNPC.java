package a1.commands;
import a1.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
public class CollideWithNPC extends AbstractAction {


    private GameWorld gw;

    private CollideWithNPC(){
    }

    private static CollideWithNPC collideWithNPC = new CollideWithNPC();

    public static CollideWithNPC getInstance(){
        return collideWithNPC;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.carCollideWithCar();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}