/**
 * Created by Victor Ignatenkov on 3/3/15.
 */

package a4.view;
import a4.app.utilities.Services;
import a4.model.*;
import a4.objects.GameObject;
import a4.objects.IDrawable;
import a4.objects.ISelectable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
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

    private Image imageRes;
    public MapView(){
        this.addMouseListener(this);

        imageRes = null;

        //Retrieve an image for background
        String pathToResources = Services.getPathToImgResources();
        String imgName = "asphalt_light.jpg";
        try {
            imageRes = ImageIO.read(new File(pathToResources + imgName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void update(GameWorldProxy gw, Object arg){
        /**
         * Code here to output current map information (based on
         * the data in the Observable ) to the console and to the
         * screen through JTextAre
         */
        //gw = game world proxy
        this.gw = gw;

        Iterator iter = gw.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();

        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //Draw background with an image
        super.paintComponent(g);
            int ratio = 200;
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    g.drawImage(imageRes, ratio * i, ratio * j, ratio, ratio, null);
                }
            }


        AffineTransform worldToND, ndToScreen, theVTM;

        int winWidth, winHeight, winLeft, winBottom;
        winWidth = 1000;
        winHeight = 800;
        winLeft = 0;
        winBottom = 0;


        Graphics2D g2d = (Graphics2D)g;
        worldToND = buildWorldToNDXform(winWidth, winHeight, winLeft, winBottom);
        ndToScreen = buildNDToScreenXForm(getWidth(), getHeight());
        theVTM = (AffineTransform) ndToScreen.clone();
        theVTM.concatenate(worldToND);

        g2d.transform(theVTM);
       // g2d.setClip(0,0,500,500);

//        g2d.translate(0, this.getHeight());
//        g2d.scale(1,-1);


        /**
         * Draw in the right order according the the zIndex
         */
        int i = 0;
        int maxZIndex = 0;
         while(true) {
          Iterator iter = gw.getIterator();
          while (iter.hasNext()) {
              GameObject mObj = (GameObject) iter.getNext();
              if (mObj.getZIndex() > maxZIndex)
                  maxZIndex = mObj.getZIndex();
              if (mObj.getZIndex() == i) {
                  ((IDrawable) mObj).draw(g2d);
              }
          }
          if (i == maxZIndex) {
              break;
          } else {
              i++;
          }
      }
    }

    private AffineTransform buildWorldToNDXform(int winWidth, int winHeight, int winLeft, int winBottom) {

        AffineTransform  myTranslationMatrix = new AffineTransform();
        AffineTransform  myScaleMatrix = new AffineTransform();

        myTranslationMatrix.translate(0, 0);//-winLeft, -winBottom
        myScaleMatrix.scale((double)1/winWidth, (double)1/winHeight); //(1 / winWidth, 1 / winHeight)


        //public void concatenate(AffineTransform Tx)
        //Cx'(p) = Cx(Tx(p))
        //first transforming p by Tx and then transforming the result by the original transform Cx

        AffineTransform temp = new AffineTransform();
        temp.setTransform(myScaleMatrix);
        temp.concatenate(myTranslationMatrix);


        return temp;
    }



    private AffineTransform buildNDToScreenXForm(int width, int height) {

        AffineTransform  myTranslationMatrix = new AffineTransform();
        AffineTransform  myScaleMatrix = new AffineTransform();

        myScaleMatrix.scale((width), -(double)height);  //width, -height
        myTranslationMatrix.translate(0, this.getHeight());

        AffineTransform temp = new AffineTransform();

        temp.setTransform(myTranslationMatrix);
        temp.concatenate(myScaleMatrix);

        return temp;

    }



    /**
     * Logic for detecting if an object has been selected.
     * @param e
     */
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
