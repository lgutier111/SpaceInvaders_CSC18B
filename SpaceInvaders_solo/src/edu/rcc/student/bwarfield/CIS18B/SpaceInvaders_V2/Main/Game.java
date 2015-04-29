
package edu.rcc.student.bwarfield.CIS18B.SpaceInvaders_V2.Main;

import javax.swing.JFrame;


public class Game {
    public static void main (String[] args){
        //Create Game Frame
        JFrame window = new JFrame("Invaders from Space!");
        //set application to close on exit
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set game area
        window.setContentPane(new GamePanel());
        //activate frame
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
