/* Created by Victor Ignatenkov on 1/18/16 */
package a4.app.commands;
import a4.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class RotateMuzzleToLeft extends AbstractAction {

    private GameWorld gw;

    private RotateMuzzleToLeft(){
        super("Accelerate");
    }

    private static RotateMuzzleToLeft rotateMuzzleToLeft = new RotateMuzzleToLeft();

    public static RotateMuzzleToLeft getInstance(){
        return rotateMuzzleToLeft;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.rotateMuzzleToLeft();
        } else {
            System.out.println("The target for " + this.getClass().getName() + " is not set up");
        }
    }

    /**
     * Supply a target, so the command can
     * call the actual implementation of the
     * corresponding command.
     */
    public void setTarget(GameWorld gw){
        this.gw = gw;
    }
}