package a2.view;

import a2.model.GameWorld;
import a2.model.IObservable;
import a2.model.IObserver;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by Victor Ignatenkov on 3/3/15.
 */
public class ScoreView extends JPanel implements IObserver {

    //**********************************************
    //              top panel                     **
    //**********************************************


    private JLabel currentTime;
    private JLabel livesLeft;
    private JLabel highestPylon;
    private JLabel remainingFuelLevel;
    private JLabel playerDamageLevel;
    private JLabel soundStatus;

    public ScoreView(){
        setBorder(new LineBorder(Color.blue, 2));
        setLayout(new FlowLayout(FlowLayout.LEFT,16,0));

     //   this.add(topPanel,BorderLayout.NORTH);

        currentTime = new JLabel("Time: ");
        add(currentTime);

        livesLeft = new JLabel("Lives Left: ");
        add(livesLeft);

        highestPylon = new JLabel("Highest Player Pylon: ");
        add(highestPylon);

        remainingFuelLevel = new JLabel("Player Fuel Remaining: ");
        add(remainingFuelLevel);

        playerDamageLevel = new JLabel("Player Damage Level: ");
        add(playerDamageLevel);

        soundStatus = new JLabel("Sound: ");
        add(soundStatus);

    }



    public void update (IObservable o, Object arg){
        /**
         * code here to update JLabels from data in the Observable
         */

        GameWorld gw = (GameWorld)arg;


        currentTime.setText("Time: " + gw.getCurrentClockTime());
        livesLeft.setText("Lives Left: " + gw.getLivesRemaining());
        highestPylon.setText("Highest Player Pylon: " + gw.getLastPylonReached());
        //TODO find out if getCurrentFuelLevel have dependencies relying on float
        remainingFuelLevel.setText("Player Fuel Remaining: " + (int)gw.getCurrentFuelLevel());
        playerDamageLevel.setText("Player Damage Level: " + gw.getDamageLevel());
        soundStatus.setText("Sound: " + (gw.isSound() ? "ON" : "OFF"));







    }
}
