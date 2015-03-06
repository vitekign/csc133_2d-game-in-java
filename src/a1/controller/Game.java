package a1.controller;

import a1.model.GameWorld;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Scanner;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */
public class Game extends JFrame{


    private GameWorld gw;

    public Game() {
        super("My Racing Game");

        gw = new GameWorld();
        gw.initLayout();





        setLayout(new BorderLayout());




        //**********************************************
        //              top panel                     **
        //**********************************************

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,16,0));
        topPanel.setBorder(new LineBorder(Color.blue,2));
        this.add(topPanel,BorderLayout.NORTH);

        JLabel currentTime = new JLabel("Time: ");
        topPanel.add(currentTime);


        JLabel livesLeft = new JLabel("Lives Left: ");
        topPanel.add(livesLeft);

        JLabel highestPylon = new JLabel("Highest Player Pylon: ");
        topPanel.add(highestPylon);

        JLabel remainingFuelLevel = new JLabel("Player Fuel Remaining: ");
        topPanel.add(remainingFuelLevel);

        JLabel playerDamageLevel = new JLabel("Player Damage Level: ");
        topPanel.add(playerDamageLevel);

        JLabel soundStatus = new JLabel("Sound: ");
        topPanel.add(soundStatus);


        //**********************************************
        //              left panel                    **
        //**********************************************
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new TitledBorder(" Options: "));
        leftPanel.setLayout(new GridLayout(10,1));

        leftPanel.setBorder(new LineBorder(Color.green,2));
        this.add(leftPanel, BorderLayout.WEST);

        JButton collideWithNPC = new JButton("Collide With NPC");
        leftPanel.add(collideWithNPC);

        JButton collideWithPylon = new JButton("Collide With Pylon");
        leftPanel.add(collideWithPylon);

        JButton collideWithBird = new JButton("Collide With Bird");
        leftPanel.add(collideWithBird);

        JButton pickUpFuelCan = new JButton("Picked Up FuelCan");
        leftPanel.add(pickUpFuelCan);

        JButton enterOilSlick = new JButton("Entered Oil Slick");
        leftPanel.add(enterOilSlick);

        JButton exitOilSlick = new JButton("Exited Oil Slick");
        leftPanel.add(exitOilSlick);

        JButton switchStrategy = new JButton("Switch Strategy");
        leftPanel.add(switchStrategy);

        JButton triggerTick = new JButton("Trigger Tick");
        leftPanel.add(triggerTick);

        JButton quitTheGame = new JButton("Quit");
        leftPanel.add(quitTheGame);



        //**********************************************
        //              central panel                **
        //**********************************************

        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(new EtchedBorder());
        centerPanel.setBackground(new Color(100,100,123));
        this.add(centerPanel,BorderLayout.CENTER);


        //**********************************************
        //                    menu                    **
        //**********************************************

        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem mItem = new JMenuItem("1");
        fileMenu.add(mItem);

        JMenu commandMenu = new JMenu("Commands");

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
     *
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