package a1.controller;

import a1.model.GameWorld;

import java.util.Scanner;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */
public class Game {


    private GameWorld gw;

    public Game() {
        gw = new GameWorld();
        gw.initLayout();
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