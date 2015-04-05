package a3.controller;

import a3.app.commands.*;
import a3.model.GameWorld;
import a3.view.MapView;
import a3.view.ScoreView;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */


/**
 * Override the JButton to avoid issues with
 * hitting "SPACE" key.
 */
class ButtonSpaceKeyFocusAgnostic extends JButton{

    public ButtonSpaceKeyFocusAgnostic(String str){
        super(str);
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");


    }

}

/**
 * Game - functions as a controller of the
 * game.
 */
public class Game extends JFrame{


    private GameWorld gw;
    private ScoreView scoreView;
    private MapView mapView;

    public Game() {
        super("My Racing Game");

        gw = new GameWorld();
        gw.initLayout();
        gw.startTimer();

        scoreView = new ScoreView();
        mapView = new MapView();

        setLayout(new BorderLayout());


        gw.addObserver(scoreView);
        gw.addObserver(mapView);





        gw.notifyObserver();


        //**********************************************
        //              commands                      **
        //**********************************************

        /**
         * Create a set of commands which are responsible
         * for specific business logic. Each command is created
         * only once because it is a singleton in its nature.
         */

        QuitTheGame quitAction = QuitTheGame.getInstance();
        TriggerTick tickAction = TriggerTick.getInstance();
        CollideWithBird colWithBirdAction = CollideWithBird.getInstance();
        CollideWithPylon colWithPylonAction = CollideWithPylon.getInstance();
        CollideWithNPC colWithNPCAction = CollideWithNPC.getInstance();
        PickUpFuelCan pickUpFuelCanAction = PickUpFuelCan.getInstance();
        EnterOilSlick enterOilSlickAction = EnterOilSlick.getInstance();
        LeaveOilSlick leaveOilSlickAction = LeaveOilSlick.getInstance();

        PlayPause playPause = PlayPause.getInstance();
        playPause.setTarget(gw);

        DeleteObject deleteObjectAction = DeleteObject.getInstance();
        deleteObjectAction.setTarget(gw);
        deleteObjectAction.setEnabled(false);

        AddPylon addPylonAction = AddPylon.getInstance();
        addPylonAction.setTarget(gw);
        addPylonAction.setEnabled(false);

        AddFuelCan addFuelCanAction = AddFuelCan.getInstance();
        addFuelCanAction.setTarget(gw);
        addFuelCanAction.setEnabled(false);

        AddOilSlick addOilSlickAction = AddOilSlick.getInstance();
        addOilSlickAction.setTarget(gw);

        ChangeColors changeColorsAction = ChangeColors.getInstance();
        changeColorsAction.setTarget(gw);

        TurnOnSound turnOnSoundAction = TurnOnSound.getInstance();
        turnOnSoundAction.setTarget(gw);

        New newAction = New.getInstance();
        newAction.putValue(Action.NAME, "New");

        Save saveAction = Save.getInstance();
        saveAction.putValue(Action.NAME, "Save");

        ShowAbout showAboutAction = ShowAbout.getInstance();

        Accelerate accelerateAction = Accelerate.getInstance();
        accelerateAction.setTarget(gw);

        Brake brakeAction = Brake.getInstance();
        brakeAction.setTarget(gw);

        TurnLeft turnLeftAction = TurnLeft.getInstance();
        turnLeftAction.setTarget(gw);

         TurnRight turnRightAction = TurnRight.getInstance();
        turnRightAction.setTarget(gw);

        SwitchStrategies switchStrategiesAction = SwitchStrategies.getInstance();
        switchStrategiesAction.setTarget(gw);


        //**********************************************
        //              top panel                     **
        //**********************************************

        /**
         * The ScoreView is implemented in the
         * different class, and then just add it the
         * main JPanel.
         */
        add(scoreView, BorderLayout.NORTH);


        //**********************************************
        //              left panel                    **
        //**********************************************

        /**
         * Left Panel - represents a set of buttons
         * which are located on the left side.
         * 1. Create a button.
         * 2. Supply the button's setAction with appropriate Command
         */



        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(22, 22, 22));

        leftPanel.setBorder(new TitledBorder(" Options: "));
        leftPanel.setLayout(new GridLayout(20, 1));

       // leftPanel.setBorder(new LineBorder(green, 2));
        this.add(leftPanel, BorderLayout.WEST);

        JButton playBtn = new ButtonSpaceKeyFocusAgnostic("Play");
        leftPanel.add(playBtn);
        colWithNPCAction.setTarget(gw);
        playBtn.setAction(playPause);
        colWithNPCAction.putValue(Action.NAME, "Play");


        JButton collideWithNPC = new ButtonSpaceKeyFocusAgnostic("Collide With NPC");
        leftPanel.add(collideWithNPC);
        colWithNPCAction.setTarget(gw);
        collideWithNPC.setAction(colWithNPCAction);
        colWithNPCAction.putValue(Action.NAME, "Collide With NPC");

        JButton collideWithPylon = new ButtonSpaceKeyFocusAgnostic("");
        leftPanel.add(collideWithPylon);
        colWithPylonAction.setTarget(gw);
        collideWithPylon.setAction(colWithPylonAction);
        colWithPylonAction.putValue(Action.NAME, "Collide With Pylon");

        JButton collideWithBird = new ButtonSpaceKeyFocusAgnostic("");
        leftPanel.add(collideWithBird);
        colWithBirdAction.setTarget(gw);
        collideWithBird.setAction(colWithBirdAction);
        colWithBirdAction.putValue(Action.NAME, "Collide With Bird");



        JButton pickUpFuelCan = new ButtonSpaceKeyFocusAgnostic("Picked Up FuelCan");
        leftPanel.add(pickUpFuelCan);
        pickUpFuelCanAction.setTarget(gw);
        pickUpFuelCan.setAction(pickUpFuelCanAction);
        pickUpFuelCanAction.putValue(Action.NAME, "Picked Up FuelCan");

        JButton enterOilSlick = new ButtonSpaceKeyFocusAgnostic("Entered Oil Slick");
        leftPanel.add(enterOilSlick);
        enterOilSlickAction.setTarget(gw);
        enterOilSlick.setAction(enterOilSlickAction);
        enterOilSlickAction.putValue(Action.NAME, "Entered Oil Slick");

        JButton exitOilSlick = new ButtonSpaceKeyFocusAgnostic("Exited Oil Slick");
        leftPanel.add(exitOilSlick);
        leaveOilSlickAction.setTarget(gw);
        exitOilSlick.setAction(leaveOilSlickAction);
        leaveOilSlickAction.putValue(Action.NAME, "Exited Oil Slick");

        JButton switchStrategy = new ButtonSpaceKeyFocusAgnostic("Switch Strategy");
        leftPanel.add(switchStrategy);
        switchStrategy.setAction(switchStrategiesAction);
        switchStrategiesAction.putValue(Action.NAME, "Switch Strategy");


        JButton deleteGameObjectBtn = new ButtonSpaceKeyFocusAgnostic("Delete");
        leftPanel.add(deleteGameObjectBtn);
        deleteGameObjectBtn.setAction(deleteObjectAction);

        JButton addPylonBtn = new ButtonSpaceKeyFocusAgnostic("Add Pylon");
        leftPanel.add(addPylonBtn);
        addPylonBtn.setAction(addPylonAction);

        JButton addFuelCanBtn = new ButtonSpaceKeyFocusAgnostic("Add FuelCan");
        leftPanel.add(addFuelCanBtn);
        addFuelCanBtn.setAction(addFuelCanAction);





        ButtonSpaceKeyFocusAgnostic quitTheGame = new ButtonSpaceKeyFocusAgnostic("");
        quitTheGame.setAction(quitAction);
        leftPanel.add(quitTheGame);
        quitAction.putValue(Action.NAME, "Quit");





        //**********************************************
        //      Set up logic for key strokes.         **
        //**********************************************

        //TODO Find out how to switch the focus to another JComponent

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F"), "pickUpFuelCan");
        this.getRootPane().getActionMap().put("pickUpFuelCan", pickUpFuelCanAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("O"), "addOilSlick");
        this.getRootPane().getActionMap().put("addOilSlick", addOilSlickAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("N"), "changeColors");
        this.getRootPane().getActionMap().put("changeColors", changeColorsAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0),"accelerate");
        this.getRootPane().getActionMap().put("accelerate", accelerateAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"),"brake");
        this.getRootPane().getActionMap().put("brake", brakeAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"),"turnLeft");
        this.getRootPane().getActionMap().put("turnLeft", turnLeftAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"),"turnRight");
        this.getRootPane().getActionMap().put("turnRight", turnRightAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"),"switchStr");
        this.getRootPane().getActionMap().put("switchStr", switchStrategiesAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Q"),"quit");
        this.getRootPane().getActionMap().put("quit", quitAction);





        //**********************************************
        //              central panel                **
        //**********************************************


        /**
         * Central panel is implemented in another class
         * and then just add the main JPanel.
         */
        this.add(mapView, BorderLayout.CENTER);


        //**********************************************
        //                    menu                    **
        //**********************************************

        /**
         * Create a menu in the main JPanel.
         * All buttons in the menu do specific functionality
         * by supplying them with an appropriate Command.
         */

        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        fileMenu.add(newMenuItem);
        newMenuItem.setAction(newAction);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        saveMenuItem.setAction(saveAction);

        JCheckBoxMenuItem soundMenuItem = new JCheckBoxMenuItem("Sound");
        soundMenuItem.setState(gw.isSound());
        fileMenu.add(soundMenuItem);
        soundMenuItem.setAction(turnOnSoundAction);
        turnOnSoundAction.putValue(Action.NAME, "Sound");


        JMenuItem aboutMenuItem = new JMenuItem("About");
        fileMenu.add(aboutMenuItem);
        aboutMenuItem.setAction(showAboutAction);


        JMenuItem quitTheGameMenuItem = new JMenuItem("Quit");
        fileMenu.add(quitTheGameMenuItem);
        quitTheGameMenuItem.setAction(quitAction);

        JMenu commandMenu = new JMenu("Commands");
        JMenuItem pickUpFuelItem = new JMenuItem("fuel pickup");
        pickUpFuelItem.setAction(pickUpFuelCanAction);
        commandMenu.add(pickUpFuelItem);

        JMenuItem addOilSlickItem = new JMenuItem("add oil slick");
        commandMenu.add(addOilSlickItem);
        addOilSlickItem.setAction(addOilSlickAction);
        addOilSlickAction.putValue(Action.NAME, "Add Oil Slick");

        JMenuItem generateNewColorsItem = new JMenuItem("new colors");
        commandMenu.add(generateNewColorsItem);
        generateNewColorsItem.setAction(changeColorsAction);
        changeColorsAction.putValue(Action.NAME, "Change Colors");

        bar.add(fileMenu);
        bar.add(commandMenu);
        this.setJMenuBar(bar);


        //**********************************************
        //              main JPanel                   **
        //**********************************************
        /**
         * Set up the main JPanel.
         */

        setResizable(false);
        setSize(gw.GLOBAL_WIDTH, gw.GLOBAL_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       // setBackground(Color.GRAY);
        setVisible(true);


        /**
         * Right now play() is not used,
         * however, it might be need in the next
         * versions of the game by implementing
         * a game loop.
         */
        play();

    }








    /**
     * Main loop of the game
     * In the current version, the game loop is not used.
     **/
    private void play() {

    }

}