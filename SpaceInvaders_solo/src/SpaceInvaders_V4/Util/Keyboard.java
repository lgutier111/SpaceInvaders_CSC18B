package SpaceInvaders_V4.Util;

import SpaceInvaders_V4.Main.GameWindow;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard {

    //the static of the keys on the keyboard
    private static boolean[] keys = new boolean[1024];
    
    private static GameWindow callback;

    //initialize the central keyboard handler
    public static void init() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new KeyHandler(), AWTEvent.KEY_EVENT_MASK);
    }

    //initialize the central keyboard handler
    //@param c the component listened to
    public static void init(Component c) {
    	c.addKeyListener(new KeyHandler());     
        callback = (GameWindow) c;
    }

    //check to see if specified key is pressed
    //@param key the code of the key to check defined in KeyEvent
    //@return true if the key is pressed
    public static boolean isPressed(int key) {
        return keys[key];
    }

    //set the status of the key
    //@param key the code of the key to set
    //@param pressed the new status of the key
    public static void setPressed(int key, boolean pressed) {
        keys[key] = pressed;
    }
    //add class to respond to key presses on a global scale

    private static class KeyHandler extends KeyAdapter implements AWTEventListener {
        //notification of key press
        //@param e the key event details

        public void keyPressed(KeyEvent e) {
            if (e.isConsumed()) {
                return;
            }
            keys[e.getKeyCode()] = true;
            callback.keyPressed(e.getKeyCode());
        }

        //notification of key release
        //@param e the key event details
        public void keyReleased(KeyEvent e) {
            if (e.isConsumed()) {
                return;
            }
            KeyEvent nextPress = (KeyEvent) Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent(KeyEvent.KEY_PRESSED);
            if ((nextPress == null) || (nextPress.getWhen() != e.getWhen())) {
                keys[e.getKeyCode()] = false;
                callback.keyReleased(e.getKeyCode());
            }
        }

        //notification that an event has occured in the AWT event system
        //@param e the event details
        public void eventDispatched(AWTEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                keyPressed((KeyEvent) e);
            }
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                keyReleased((KeyEvent) e);
            }
        }
    }
}
