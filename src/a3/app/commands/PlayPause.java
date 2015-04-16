package a3.app.commands;
import a3.controller.Game;
import a3.model.GameWorld;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Accelerate command.
 * Accelerate the Car.
 */
public class PlayPause extends AbstractAction {

    private GameWorld gw;
    private Game g;
    private boolean playPauseSwitch;
    // state 0 is for Play

    SwitchStrategies switchStrategiesAction = SwitchStrategies.getInstance();

    DeleteObject deleteObjectAction = DeleteObject.getInstance();
    AddPylon addPylonAction = AddPylon.getInstance();
    AddFuelCan addFuelCanAction = AddFuelCan.getInstance();


    private PlayPause(){
        super("Pause");

    }

    private static PlayPause playPause = new PlayPause();

    public static PlayPause getInstance(){
        return playPause;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){



         if(playPauseSwitch == false){
             this.putValue(Action.NAME, "Play");
             playPauseSwitch = true;

             deleteObjectAction.setEnabled(true);
             addPylonAction.setEnabled(true);
             addFuelCanAction.setEnabled(true);

         } else {
             this.putValue(Action.NAME, "Pause");
             playPauseSwitch = false;


             deleteObjectAction.setEnabled(false);
             addPylonAction.setEnabled(false);
             addFuelCanAction.setEnabled(false);
         }

            gw.playPause();
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
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