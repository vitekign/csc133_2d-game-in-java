/* Created by Victor Ignatenkov on 3/3/15 */

package a4.view;
import a4.app.utilities.Utilities;
import a4.model.*;
import a4.objects.GameObject;
import a4.objects.IDrawable;
import a4.objects.ISelectable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


/* MapView is used to show the map of the current
 * state of the game.*/

public class MapView extends JPanel implements IObserver, MouseListener,
        MouseWheelListener, MouseMotionListener{

    private GameWorldProxy gw;

    private Image backgroundPatternImage;
    private AffineTransform theVTM ;
    private MouseEvent lastMouseEvent = null;

    /* Define boundaries */
    private double winRight = 845;
    private double winTop = 709;
    private double winLeft = 0;
    private double winBottom = 0;

    public MapView(){
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Retrieve an image for background
        // backgroundPatternImage = Services.loadImage("asphalt_light.jpg");
        // backgroundPatternImage = Services.loadImage("red_square_weird.png");
        // backgroundPatternImage = Services.loadImage("gray_square_with_rocks.png");
        backgroundPatternImage = Utilities.loadImage("grass_1.png");

        theVTM = new AffineTransform();
    }

    public double getWinHeight(){
        return winTop - winBottom;
    }

    public double getWinWidth(){
        return winRight - winLeft;
    }

    public void update(GameWorldProxy gw, Object arg){
        this.gw = gw;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        fillUpScreenWithPatternImage(g);

        AffineTransform worldToND, ndToScreen;

        Graphics2D g2d = (Graphics2D)g;
        worldToND = buildWorldToNDXform(winRight - winLeft, winTop - winBottom, winLeft, winBottom);
        ndToScreen = buildNDToScreenXForm(this.getSize().getWidth(), this.getSize().getHeight());
        theVTM = (AffineTransform) ndToScreen.clone();
        theVTM.concatenate(worldToND);

        Utilities.supplyUtilitiesWithVTM(theVTM);
        g2d.transform(theVTM);

        /**
         * Draw in the right order according the zIndex
         */
        int i = 0;
        int maxZIndex = 0;
         while(true) {
          Iterator iterator = gw.getIterator();
          while (iterator.hasNext()) {
              GameObject mObj = (GameObject) iterator.getNext();
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

    private void fillUpScreenWithPatternImage(Graphics g) {
        int ratio = 65;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                g.drawImage(backgroundPatternImage, ratio * i, ratio * j, ratio, ratio, null);
            }
        }
    }

    private AffineTransform buildWorldToNDXform(double winWidth, double winHeight, double winLeft, double winBottom) {
        AffineTransform  myTranslationMatrix = new AffineTransform();
        AffineTransform  myScaleMatrix = new AffineTransform();

        myTranslationMatrix.translate(-winLeft, -winBottom);
        myScaleMatrix.scale((double)1/winWidth, (double)1/winHeight);

        //public void concatenate(AffineTransform Tx)
        //Cx'(p) = Cx(Tx(p))
        //first transforming p by Tx and then transforming the result by the original transform Cx

        AffineTransform temp = new AffineTransform();
        temp.setTransform(myScaleMatrix);
        temp.concatenate(myTranslationMatrix);

        return temp;
    }


    private AffineTransform buildNDToScreenXForm(double width, double height) {
        AffineTransform  myTranslationMatrix = new AffineTransform();
        AffineTransform  myScaleMatrix = new AffineTransform();

        myScaleMatrix.scale(width, -height);
        myTranslationMatrix.translate(0, height);

        AffineTransform temp = new AffineTransform();

        temp.setTransform(myTranslationMatrix);
        temp.concatenate(myScaleMatrix);

        return temp;
    }


    /* Functionality for detecting if an object has been selected */
    @Override
    public void mouseClicked(MouseEvent e) {
        gw.setLastMouseEvent(e);
        GameObject temp;
        Point2D mouseWorldLoc =  Utilities.applyInverseAndGetPoint(e);

        if(gw.isItInPause()) {
            Iterator iterator = gw.getIterator();
            while (iterator.hasNext()) {
                temp = (GameObject) iterator.getNext();
                if (temp instanceof ISelectable) {
                    if (((ISelectable) temp).contains(mouseWorldLoc)) {
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
        lastMouseEvent = null;
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void zoomIn(MouseEvent event){
        double tempHeight = getWinHeight();
        double tempWidth = getWinWidth();

        double xRatio = (double)event.getX() / winRight;
        double yRatio = (double)event.getY() / winTop;

        winLeft += tempWidth*(0.05 * ( xRatio));
        winRight -= tempWidth*(0.05 * (1 - xRatio));
        winTop -= tempHeight*(0.05 * (yRatio));
        winBottom += tempHeight*(0.05 * (1 - yRatio));

        this.repaint();
    }

    public void zoomOut(MouseEvent event){
        double tempHeight = getWinHeight();
        double tempWidth = getWinWidth();

        double xRatio = (double)event.getX() / winRight;
        double yRatio = (double)event.getY() / winTop;

        winLeft -= tempWidth*(0.05 * ( xRatio));
        winRight += tempWidth*(0.05 * (1 - xRatio));
        winTop += tempHeight*(0.05 * (yRatio));
        winBottom -= tempHeight*(0.05 * (1 - yRatio));

        this.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if(!gw.isItInPause()) {
            if (!event.isShiftDown()) {
                if (event.getWheelRotation() > 0)
                    zoomIn(event);
                else
                    zoomOut(event);
            }
        }
    }


    @Override
    public void mouseDragged(MouseEvent currentMouseEvent) {
        if(!gw.isItInPause()) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (lastMouseEvent == null) {
                lastMouseEvent = currentMouseEvent;
            } else {
                double difX = currentMouseEvent.getX() - lastMouseEvent.getX();
                double difY = currentMouseEvent.getY() - lastMouseEvent.getY();

                double winRatioX = (winRight - winLeft) / this.getWidth();
                double winRationY = (winTop - winBottom) / this.getHeight();

                winLeft -= difX * winRatioX;
                winRight -= difX * winRatioX;

                winBottom += difY * winRationY;
                winTop += difY * winRationY;

                lastMouseEvent = currentMouseEvent;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
