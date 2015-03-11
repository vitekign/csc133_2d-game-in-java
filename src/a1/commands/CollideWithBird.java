package a1.commands;

import a1.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/8/15.
 */
public class CollideWithBird extends AbstractAction {


    private GameWorld gw;

    private CollideWithBird(){
        super("CollideWithBird");
    }



    private static CollideWithBird collideWithBird = new CollideWithBird();

    public static CollideWithBird getInstance(){
        return collideWithBird;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.birdFlyOver();
        } else {
            System.out.println("The target is not set up");
        }

    }

    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}
