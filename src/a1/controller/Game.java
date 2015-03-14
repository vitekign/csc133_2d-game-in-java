package a1.controller;

import a1.commands.CollideWithBird;
import a1.commands.QuitTheGame;
import a1.commands.TriggerTick;
import a1.model.GameWorld;
import a1.model.ScoreView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
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
        QuitTheGame quit = QuitTheGame.getInstance();
        TriggerTick tick = TriggerTick.getInstance();
        CollideWithBird colWithBird = CollideWithBird.getInstance();


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

        JButton collideWithPylon = new ButtonSpaceKeyFocusAgnostic("Collide With Pylon");
        leftPanel.add(collideWithPylon);

        JButton collideWithBird = new ButtonSpaceKeyFocusAgnostic("Collide With Bird");
        colWithBird.setTarget(gw);
        collideWithBird.setAction(colWithBird);
        leftPanel.add(collideWithBird);



        JButton pickUpFuelCan = new ButtonSpaceKeyFocusAgnostic("Picked Up FuelCan");
        leftPanel.add(pickUpFuelCan);

        JButton enterOilSlick = new ButtonSpaceKeyFocusAgnostic("Entered Oil Slick");
        leftPanel.add(enterOilSlick);

        JButton exitOilSlick = new ButtonSpaceKeyFocusAgnostic("Exited Oil Slick");
        leftPanel.add(exitOilSlick);

        JButton switchStrategy = new ButtonSpaceKeyFocusAgnostic("Switch Strategy");
        leftPanel.add(switchStrategy);

        JButton triggerTick = new ButtonSpaceKeyFocusAgnostic("Trigger Tick");
        triggerTick.setAction(tick);
        tick.setTarget(gw);
        leftPanel.add(triggerTick);

        JButton quitTheGame = new ButtonSpaceKeyFocusAgnostic("Quit");
        quitTheGame.setAction(quit);
        leftPanel.add(quitTheGame);


        //TODO Find out how to switch the focus to another jcomponent
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("T"), "doTick");
        this.getRootPane().getActionMap().put("doTick",tick);





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

        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);

        JCheckBoxMenuItem soundMenuItem = new JCheckBoxMenuItem("Sound");
        soundMenuItem.setState(true);
        fileMenu.add(soundMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        fileMenu.add(aboutMenuItem);

        JMenuItem quitTheGameMenuItem = new JMenuItem("Quit");
        fileMenu.add(quitTheGameMenuItem);


        JMenu commandMenu = new JMenu("Commands");
        JMenuItem pickUpFuelItem = new JMenuItem("fuel pickup");
        commandMenu.add(pickUpFuelItem);

        JMenuItem addOilSlickItem = new JMenuItem("add oil slick");
        commandMenu.add(addOilSlickItem);

        JMenuItem generateNewColorsItem = new JMenuItem("new colors");
        commandMenu.add(generateNewColorsItem);


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