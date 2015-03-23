/**
 * Created by Victor Ignatenkov on 3/3/15.
 */

package a3.view;
import a3.model.GameWorld;
import a3.model.GameWorldProxy;
import a3.model.IObservable;
import a3.model.IObserver;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ScoreView is used to show some of the data
 * of the current state of the game on the screen.
 * It's going to be stick to the top part of the window.
 */
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

    public ScoreView(){
        setBorder(new LineBorder(Color.blue, 2));
        setLayout(new FlowLayout(FlowLayout.LEFT,16,0));

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



    public void update (GameWorldProxy gw, Object arg){
        /**
         * Code here to update JLabels from data in the Observable.
         * Every time there is a change in the GameWorld date, the
         * information is going to be updated with update() method.
         */
        currentTime.setText("Time: " + gw.getCurrentClockTime());
        livesLeft.setText("Lives Left: " + gw.getLivesRemaining());
        highestPylon.setText("Highest Player Pylon: " + gw.getLastPylonReached());
        //TODO find out if getCurrentFuelLevel have dependencies relying on float
        remainingFuelLevel.setText("Player Fuel Remaining: " + (int)gw.getCurrentFuelLevel());
        playerDamageLevel.setText("Player Damage Level: " + gw.getDamageLevel());
        soundStatus.setText("Sound: " + (gw.isSound() ? "ON" : "OFF"));







    }
}
