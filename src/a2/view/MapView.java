package a2.view;

import a2.model.GameWorld;
import a2.model.IObservable;
import a2.model.IObserver;
import a2.model.Iterator;
import a2.objects.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by Victor Ignatenkov on 3/3/15.
 */
public class MapView extends JPanel implements IObserver {


    JTextArea textArea;


    public MapView(){

        textArea = new JTextArea(20, 62);
       // JScrollPane scrollPane = new JScrollPane(textArea);

        //scrollPane.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");

        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 18, 51));

        //TODO If you have time return back JScrollPane and find out how to make it stop eating up arr
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
         * the data in the Observable ) to the console
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
