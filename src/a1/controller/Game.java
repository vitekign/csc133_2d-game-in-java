package a1.controller;

import a1.commands.*;
import a1.model.GameWorld;
import a1.model.ScoreView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.KeyStore;
import java.util.Scanner;

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

    public Game() {
        super("My Racing Game");

        gw = new GameWorld();
        gw.initLayout();


        scoreView = new ScoreView();


        setLayout(new BorderLayout());


        gw.addObserver(scoreView);
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

        JButton triggerTick = new JButton("");
        leftPanel.add(triggerTick);
        triggerTick.setAction(tickAction);
        tickAction.setTarget(gw);
        tickAction.putValue(Action.NAME, "Trigger Tick");


        ButtonSpaceKeyFocusAgnostic quitTheGame = new ButtonSpaceKeyFocusAgnostic("");
        quitTheGame.setAction(quitAction);
        leftPanel.add(quitTheGame);
        quitAction.putValue(Action.NAME,"Quit");


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

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"),"accelerate");
        this.getRootPane().getActionMap().put("accelerate", accelerateAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"),"brake");
        this.getRootPane().getActionMap().put("brake", brakeAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"),"turnLeft");
        this.getRootPane().getActionMap().put("turnLeft", turnLeftAction);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"),"turnRight");
        this.getRootPane().getActionMap().put("turnRight", turnRightAction);





        //**********************************************
        //              central panel                **
        //**********************************************

        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(new EtchedBorder());
        centerPanel.setBackground(new Color(100, 100, 123));
        this.add(centerPanel, BorderLayout.CENTER);


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
        setSize(800, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        play();

    }


    private String getCommand() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter one command: ");
        String line = in.nextLine();
        return line;
    }

    /**
     * Main loop of the game
     * <p>
     * play() repeatedly calls a method named getCommand()
     * which prompts the user for one or two-character commands.
     * Each command returned by getCommand() should
     * invoke a corresponding function in the game world, as follows
     */
    private void play() {

        while (true) {
            String command = getCommand();

            if (command.matches("a|b|l|r|o|c|f|g|e|x|n|t|d|m|q|(p\\d\\d?)")) {
                switch (command.charAt(0)) {
                    case 'a':
                        gw.accelerate();
                        break;

                    case 'b':
                        gw.brake();
                        break;

                    case 'l':
                        gw.changeSteeringToLeft();
                        break;

                    case 'r':
                        gw.changeSteeringToRight();
                        break;

                    case 'o':
                        gw.addOilSlick();
                        break;

                    case 'c':
                        gw.carCollideWithCar();
                        break;

                    case 'p':
                        String num;
                        num = command.substring(1, command.length());
                        gw.carCollideWithPylon(Integer.parseInt(num));
                        break;

                    case 'f':
                        gw.pickUpFuelCan();
                        break;

                    case 'g':
                        gw.birdFlyOver();
                        break;

                    case 'e':
                        gw.enterOilSlick();
                        break;

                    case 'x':
                        gw.leaveOilSlick();
                        break;

                    case 'n':
                        gw.generateNewColors();
                        break;

                    case 't':
                        gw.makeTick();
                        break;

                    case 'd':
                        gw.generateDisplay();
                        break;

                    case 'm':
                        gw.generateMap();
                        break;

                    case 'q':
                        gw.quitTheGame();
                        break;
                }
            } else {
                System.out.println("The entered command is incorrect");
            }
        }
    }



}