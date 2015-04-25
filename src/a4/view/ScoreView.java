/**
 * Created by Victor Ignatenkov on 3/3/15.
 */

package a4.view;
import a4.model.GameWorldProxy;
import a4.model.IObserver;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ScoreView is used to show some of the data
 * of the current state of the game on the screen.
 * It's going to be stick to the top part of the window.
 */


class scoreLabel extends JLabel{

    public scoreLabel(String st){
        super(st);
        setForeground(new Color(200,200,200));
    }
}


public class ScoreView extends JPanel implements IObserver {

    //**********************************************
    //              top panel                     **
    //**********************************************

    /**
     * The state which are going to be updated.
     */
    private JLabel currentTime;
    private JLabel livesLeft;
    private JLabel highestPylon;
    private JLabel remainingFuelLevel;
    private JLabel playerDamageLevel;
    private JLabel soundStatus;

    int time = 0;
    long lastTime = 0;

    public ScoreView(){
        setBorder(new LineBorder(new Color(0, 0, 0), 2));
        this.setForeground(new Color(200, 200, 200));
        this.setOpaque(true);

        setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));
        this.setBackground(new Color(20, 20, 20));




        currentTime = new scoreLabel("Time: ");
        add(currentTime);

        livesLeft = new scoreLabel("Lives Left: ");
        add(livesLeft);

        highestPylon = new scoreLabel("Highest Player Pylon: ");
        add(highestPylon);

        remainingFuelLevel = new scoreLabel("Player Fuel Remaining: ");
        add(remainingFuelLevel);

        playerDamageLevel = new scoreLabel("Player Damage Level: ");
        add(playerDamageLevel);

        soundStatus = new scoreLabel("Sound: ");
        add(soundStatus);

    }



    public void update (GameWorldProxy gw, Object arg){
        /**
         * Code here to update JLabels from data in the Observable.
         * Every time there is a change in the GameWorld date, the
         * information is going to be updated with update() method.
         */

        livesLeft.setText("Lives Left: " + gw.getLivesRemaining());
        highestPylon.setText("Highest Player Pylon: " + gw.getLastPylonReached());
        //TODO find out if getCurrentFuelLevel have dependencies relying on float
        remainingFuelLevel.setText("Player Fuel Remaining: " + (int)gw.getCurrentFuelLevel());
        playerDamageLevel.setText("Player Damage Level: " + gw.getDamageLevel());
        soundStatus.setText("Sound: " + (gw.isSound() ? "ON" : "OFF"));
        currentTime.setText("Time: " + gw.getTimer());







    }
}
