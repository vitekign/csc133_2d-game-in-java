/* Created by Victor Ignatenkov on 3/3/15 */

package a4.view;
import a4.app.utilities.Utilities;
import a4.model.GameWorldProxy;
import a4.model.IObserver;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/* ScoreView is used to show some of the data
 * of the current state of the game on the screen.
 * It's going to be stick to the top part of the window */

public class ScoreView extends JPanel implements IObserver {

    //**********************************************
    // top panel - to show the state of the game  **
    //**********************************************

    private JLabel currentTime;
    private JLabel livesLeft;
    private JLabel highestPylon;
    private JLabel remainingFuelLevel;
    private JLabel playerDamageLevel;
    private JLabel soundStatus;

    class scoreLabelWithPreferredFont extends JLabel{
        public scoreLabelWithPreferredFont(String value){
            super(value);
        }
        @Override
        public void setFont(Font font) {
            super.setFont(new Font(Utilities.BASE_FONT, Font.PLAIN, 15));
        }
    }

    public ScoreView(){
        setBorder(new LineBorder(new Color(0, 0, 0), 2));
        this.setForeground(new Color(200, 200, 200));
        this.setOpaque(true);

        setLayout(new FlowLayout(FlowLayout.LEFT, 28, 0));
        this.setBackground(new Color(219, 219, 219));

        currentTime = new scoreLabelWithPreferredFont("Time: ");
        add(currentTime);

        livesLeft = new scoreLabelWithPreferredFont("Lives Left: ");
        add(livesLeft);

        highestPylon = new scoreLabelWithPreferredFont("Highest Player Pylon: ");

        add(highestPylon);

        remainingFuelLevel = new scoreLabelWithPreferredFont("Player Fuel Remaining: ");
        add(remainingFuelLevel);

        playerDamageLevel = new scoreLabelWithPreferredFont("Player Damage Level: ");
        add(playerDamageLevel);

        soundStatus = new scoreLabelWithPreferredFont("Sound: ");
        add(soundStatus);
    }

    public void update (GameWorldProxy gw, Object arg){
        /* Code here to update JLabels from data in the Observable.
         * Every time there is a change in the GameWorld data state,
         * the data in this object is going be to be updated by the
         * observable's triggering update method in the object */

        livesLeft.setText("Lives Left: " + gw.getLivesRemaining());
        highestPylon.setText("Highest Player Pylon: " + gw.getLastPylonReached());
        //TODO find out if getCurrentFuelLevel have dependencies relying on float
        remainingFuelLevel.setText("Player Fuel Remaining: " + (int)gw.getCurrentFuelLevel());
        playerDamageLevel.setText("Player Damage Level: " + gw.getDamageLevel());
        soundStatus.setText("Sound: " + (gw.isSound() ? "ON" : "OFF"));
        currentTime.setText("Time: " + gw.getTimer());
    }
}
