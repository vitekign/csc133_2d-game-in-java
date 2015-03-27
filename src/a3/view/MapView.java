/**
 * Created by Victor Ignatenkov on 3/3/15.
 */

package a3.view;
import a3.model.*;
import a3.objects.GameObject;
import a3.objects.IDrawable;
import a3.objects.ISelectable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * MapView is used to show the map of the current
 * state of the game. In this version, the date change
 * is shown in the Console and also in the textArea on the screen.
 */
public class MapView extends JPanel implements IObserver, MouseListener {


    /**
     * Create a textArea to show the current
     * state of the game on the screen.
     */
    JTextArea textArea;

    GameWorldProxy gw;
    public MapView(

    ){
        this.addMouseListener(this);
    }

    public void update(GameWorldProxy gw, Object arg){
        /**
         * Code here to output current map information (based on
         * the data in the Observable ) to the console and to the
         * screen through JTextAre
         */
        this.gw = gw;


        Iterator iter = gw.getIterator();



        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            //System.out.println(mObj.toString());

        }


        repaint();


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        setBackground(new Color(200, 200, 200));
       // g.drawString(String.valueOf(gw.getCurrentClockTime()), 200, 200);


        Iterator iter = gw.getIterator();
        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            ((IDrawable)mObj).draw(g);
        }



    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        GameObject temp = null;

        Iterator iter = gw.getIterator();
        while(iter.hasNext()){
            temp = (GameObject)iter.getNext();
            if(temp instanceof ISelectable) {

            if (((ISelectable) temp).contains(p)) {
                ((ISelectable) temp).setSelected(true);
            } else {
                ((ISelectable) temp).setSelected(false);
            }
        }
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
