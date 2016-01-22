/* Created by Victor Ignatenkov on 1/18/16 */

package a4.app.commands;
import a4.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class RotateMuzzleToRight extends AbstractAction {

    private GameWorld gw;

    private RotateMuzzleToRight(){
        super("Accelerate");
    }

    private static RotateMuzzleToRight rotateMuzzleToRight = new RotateMuzzleToRight();

    public static RotateMuzzleToRight getInstance(){
        return rotateMuzzleToRight;
    }

    /**
     * Call the corresponding method which is from the GameWorld
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            gw.rotateMuzzleToRight();
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