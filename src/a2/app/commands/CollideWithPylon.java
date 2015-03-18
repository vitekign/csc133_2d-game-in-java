/**
 * Created by Victor Ignatenkov on 3/8/15.
 */

package a2.app.commands;
import a2.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;



public class CollideWithPylon extends AbstractAction {


    private GameWorld gw;

    private CollideWithPylon(){
    }

    private static CollideWithPylon collideWithPylon = new CollideWithPylon();

    public static CollideWithPylon getInstance(){
        return collideWithPylon;
    }


    /**
     * Call the corresponding method which is from the GameWorld
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gw != null){
            try {
                int input = proccessInput();
                gw.carCollideWithPylon(input);
            } catch (Exception ex) {
                if(ex.getMessage() != "null") {
                    JOptionPane.showMessageDialog(null, "Please enter a number");
                }
            }
        } else {
            System.out.println("\nThe target for " + this.getClass().getName() + " is not set up");
        }
    }

    private int proccessInput() throws Exception{
        String inputString = JOptionPane.showInputDialog("Please input a pylon number");
        return Integer.parseInt(inputString);
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
