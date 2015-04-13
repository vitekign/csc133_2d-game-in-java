/**
 * Created by Victor Ignatenkov on 3/3/15.
 */

package a3.view;
import a3.app.utilities.Services;
import a3.model.*;
import a3.objects.GameObject;
import a3.objects.IDrawable;
import a3.objects.ISelectable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


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
    public boolean drawBackFlag = false;

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


       // setBackground(new Color(200, 200, 200));
        //TODO Refactor background image drawing, now it redraws the background each time it's being updated


         //   System.out.println("I'm inside paintCopmonent");

            Image imageRes;
            imageRes = null;

            String pathToResources = Services.getPathToImgResources();
            String imgName = "asphalt.png";
            try {
                imageRes = ImageIO.read(new File(pathToResources + imgName));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            int ratio = 200;
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    g.drawImage(imageRes, ratio * i, ratio * j, ratio, ratio, null);
                }
            }








       // g.drawString(String.valueOf(gw.getCurrentClockTime()), 200, 200);


        Graphics2D g2d = (Graphics2D)g;
//        g2d.translate(0, this.getHeight());
//        g2d.rotate(Math.toRadians(0));
//        g2d.scale(1,-1);


        Iterator iter = gw.getIterator();
        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            ((IDrawable)mObj).draw(g2d);
        }



    }


    @Override
    public void mouseClicked(MouseEvent e) {

        gw.setLastMouseEvent(e);

        Point p = e.getPoint();
        GameObject temp = null;

        if(gw.isItInPause()) {
            Iterator iter = gw.getIterator();
            while (iter.hasNext()) {
                temp = (GameObject) iter.getNext();
                if (temp instanceof ISelectable) {

                    if (((ISelectable) temp).contains(p)) {
                        ((ISelectable) temp).setSelected(true);
                        gw.addToTheDeleteObjectsCollection(temp);
                    } else {
                        if (!e.isControlDown()) {
                            ((ISelectable) temp).setSelected(false);
                            gw.eraseFromTheDeleteObjectsCollections(temp);
                        }
                    }
                }
            }
            repaint();
        }
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
