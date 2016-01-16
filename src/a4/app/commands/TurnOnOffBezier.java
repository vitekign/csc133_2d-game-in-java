/* Created by Victor Ignatenkov on 1/16/16 */

package a4.app.commands;
import a4.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class TurnOnOffBezier extends AbstractAction {


    private GameWorld gw;

    private TurnOnOffBezier(){
    }

    private static TurnOnOffBezier turnOnOffBezier = new TurnOnOffBezier();

    public static TurnOnOffBezier  getInstance(){
        return turnOnOffBezier;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
           // gw.enterOilSlick();
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