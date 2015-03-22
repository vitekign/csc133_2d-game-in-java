/**
 * Created by Victor Ignatenkov on 3/3/15.
 */

package a3.view;
import a3.model.GameWorld;
import a3.model.IObservable;
import a3.model.IObserver;
import a3.model.Iterator;
import a3.objects.GameObject;
import javax.swing.*;
import java.awt.*;


/**
 * MapView is used to show the map of the current
 * state of the game. In this version, the date change
 * is shown in the Console and also in the textArea on the screen.
 */
public class MapView extends JPanel implements IObserver {


    /**
     * Create a textArea to show the current
     * state of the game on the screen.
     */
    JTextArea textArea;


    public MapView(){

        textArea = new JTextArea(90, 70);

        textArea.setEditable(false);
        textArea.setBackground(new Color(8, 27, 52));

        /**
         * Disable textArea's consuming of the arrows buttons
         * when they're pressed.
         */
        textArea.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");
        textArea.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "none");
        textArea.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "none");
        textArea.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "none");

        textArea.setForeground(new Color(230,230,230));
        this.add(textArea);
    }

    public void update(IObservable o, Object arg){
        /**
         * Code here to output current map information (based on
         * the data in the Observable ) to the console and to the
         * screen through JTextAre
         */

        GameWorld gw = (GameWorld)arg;
        Iterator iter = gw.getIterator();

        textArea.setText("");

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            System.out.println(mObj.toString());
            textArea.append("  " + mObj.toString() + "\n");
        }
    }
}
