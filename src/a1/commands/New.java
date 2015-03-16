package a1.commands;

import a1.model.GameWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Victor Ignatenkov on 3/15/15.
 */
public class New extends AbstractAction {


    private GameWorld gw;

    private New(){
    }

    private static New commandNew = new New();

    public static New getInstance(){
        return commandNew;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("\nCommand New Was Clicked");
    }
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}