package a3.app.commands; /**
 * Created by Victor Ignatenkov on 3/15/15.
 */

import a3.model.GameWorld;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddPylon extends AbstractAction {


    private GameWorld gw;

    private AddPylon(){
        super("Add Pylon");
    }

    private static AddPylon addPylon = new AddPylon();

    public static AddPylon getInstance(){
        return addPylon;
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
                gw.createNewPylon(input);
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
        String inputString = JOptionPane.showInputDialog("Please input the number of pylon");
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