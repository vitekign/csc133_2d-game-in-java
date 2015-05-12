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
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;


/**
 * MapView is used to show the map of the current
 * state of the game. In this version, the date change
 * is shown in the Console and also in the textArea on the screen.
 */

//TODO Character Car isn't drawn on the top of other game objects.



public class MapView extends JPanel implements IObserver, MouseListener,
        MouseWheelListener, MouseMotionListener{
    /**
     * Create a textArea to show the current
     * state of the game on the screen.
     */

    GameWorldProxy gw;

    private Image imageRes;
    private AffineTransform theVTM ;


   // protected double winWidth, winHeight, winLeft, winBottom;

    MouseEvent lastMouseEvent = null;


    private double winRight = 845;
    private double winTop = 709;
    private double winLeft = 0;
    private double winBottom = 0;



    public MapView(){
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);



        imageRes = null;

        //Retrieve an image for background
        String pathToResources = Services.getPathToImgResources();
        String imgName = "asphalt_light.jpg";
        try {
            imageRes = ImageIO.read(new File(pathToResources + imgName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        theVTM = new AffineTransform();
    }

    public double getWinHeight(){
        return winTop - winBottom;
    }

    public double getWinWidth(){
        return winRight - winLeft;
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


        AffineTransform worldToND, ndToScreen;


        Graphics2D g2d = (Graphics2D)g;
        worldToND = buildWorldToNDXform(winRight - winLeft, winTop - winBottom, winLeft, winBottom);
        ndToScreen = buildNDToScreenXForm(this.getSize().getWidth(), this.getSize().getHeight());
        theVTM = (AffineTransform) ndToScreen.clone();
        theVTM.concatenate(worldToND);

        Services.supplyServicesWithVTM(theVTM);
        g2d.transform(theVTM);
       // g2d.setClip(0,0,500,500);



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






    private AffineTransform buildWorldToNDXform(double winWidth, double winHeight, double winLeft, double winBottom) {

        AffineTransform  myTranslationMatrix = new AffineTransform();
        AffineTransform  myScaleMatrix = new AffineTransform();

        myTranslationMatrix.translate(-winLeft, -winBottom);//-winLeft, -winBottom
        myScaleMatrix.scale((double)1/winWidth, (double)1/winHeight); //(1 / winWidth, 1 / winHeight)

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

        myScaleMatrix.scale(width, -height);  //width, -height
        myTranslationMatrix.translate(0, height);

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

        Point2D mouseWorldLoc =  Services.applyInverseAndGetPoint(e);

        if(gw.isItInPause()) {
            Iterator iter = gw.getIterator();
            while (iter.hasNext()) {
                temp = (GameObject) iter.getNext();
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

    public void zoomIn(){

        double tempHeight = getWinHeight();
        double tempWidth = getWinWidth();

        winLeft += tempWidth*0.05;
        winRight -= tempWidth*0.05;
        winTop -= tempHeight*0.05;
        winBottom += tempHeight*0.05;
        this.repaint();
    }
    public void zoomOut(){

        double tempHeight = getWinHeight();
        double tempWidth = getWinWidth();

        winLeft -= tempWidth*0.05;
        winRight += tempWidth*0.05;
        winTop += tempHeight*0.05;
        winBottom -= tempHeight*0.05;
        this.repaint();
    }



    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if (event.isShiftDown()) {
        } else {

            if(event.getWheelRotation() > 0)
                zoomIn();
            else
                zoomOut();
        }
    }


    /*********************************************/
    /*      Confirm To MouseMotionListener       */
    /*********************************************/
    @Override
    public void mouseDragged(MouseEvent currrentMouseEvent) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if(lastMouseEvent == null) {
            lastMouseEvent = currrentMouseEvent;
        } else {
            double difX = currrentMouseEvent.getX() - lastMouseEvent.getX();
            double difY = currrentMouseEvent.getY() - lastMouseEvent.getY();

            double winRatioX =  (winRight - winLeft)/ this.getWidth();
            double winRationY = (winTop - winBottom)/this.getHeight();



            winLeft -= difX * winRatioX;
            winRight -=difX * winRatioX;

            winBottom +=difY * winRationY;
            winTop += difY * winRationY;


            lastMouseEvent = currrentMouseEvent;
        }




    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    /*---------------------------------------------*/
}
