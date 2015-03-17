package a2.controller;

import a2.commands.*;
import a2.model.GameWorld;
import a2.view.MapView;
import a2.view.ScoreView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
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

public class Game extends JFrame {


    private GameWorld gw;
    private ScoreView scoreView;
    private MapView mapView;

    public Game() {
        super("My Racing Game");

        gw = new GameWorld();
        gw.initLayout();


        scoreView = new ScoreView();
        mapView = new MapView();

        setLayout(new BorderLayout());


        gw.addObserver(scoreView);
        gw.addObserver(mapView);




        gw.notifyObserver();


        //**********************************************
        //              commands                      **
        //**********************************************
        QuitTheGame quitAction = QuitTheGame.getInstance();
        TriggerTick tickAction = TriggerTick.getInstance();
        CollideWithBird colWithBirdAction = CollideWithBird.getInstance();
        CollideWithPylon colWithPylonAction = CollideWithPylon.getInstance();
        CollideWithNPC colWithNPCAction = CollideWithNPC.getInstance();
        PickUpFuelCan pickUpFuelCanAction = PickUpFuelCan.getInstance();
        EnterOilSlick enterOilSlickAction = EnterOilSlick.getInstance();
        LeaveOilSlick leaveOilSlickAction = LeaveOilSlick.getInstance();

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

        add(scoreView, BorderLayout.NORTH);


        //**********************************************
        //              left panel                    **
        //**********************************************


        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new TitledBorder(" Options: "));
        leftPanel.setLayout(new GridLayout(10, 1));

        leftPanel.setBorder(new LineBorder(Color.green, 2));
        this.add(leftPanel, BorderLayout.WEST);

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

        JButton triggerTick = new ButtonSpaceKeyFocusAgnostic("");
        leftPanel.add(triggerTick);
        triggerTick.setAction(tickAction);
        tickAction.setTarget(gw);
        tickAction.putValue(Action.NAME, "Trigger Tick");


        ButtonSpaceKeyFocusAgnostic quitTheGame = new ButtonSpaceKeyFocusAgnostic("");
        quitTheGame.setAction(quitAction);
        leftPanel.add(quitTheGame);
        quitAction.putValue(Action.NAME, "Quit");


        // ‘o’ (add oil slick), and ‘n’ (new colors).

        //TODO Find out how to switch the focus to another jcomponent
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("T"), "doTick");
        this.getRootPane().getActionMap().put("doTick",tickAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F"), "pickUpFuelCan");
        this.getRootPane().getActionMap().put("pickUpFuelCan", pickUpFuelCanAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("O"), "addOilSlick");
        this.getRootPane().getActionMap().put("addOilSlick", addOilSlickAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("N"), "changeColors");
        this.getRootPane().getActionMap().put("changeColors", changeColorsAction);

        //TODO arrows don't work unless a button pressed
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

        JPanel centerPanel = new JPanel();



        this.getRootPane().getActionMap().put("accelerate", accelerateAction);
        centerPanel.setBorder(new EtchedBorder());
        centerPanel.setBackground(new Color(123, 0, 11));
        this.add(mapView, BorderLayout.CENTER);
        centerPanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");

        //**********************************************
        //                    menu                    **
        //**********************************************

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


        setResizable(false);
        setSize(900, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        play();

    }



    /**
     * Main loop of the game
     * In the current version, the game loop is not used.
     **/
    private void play() {

    }

}