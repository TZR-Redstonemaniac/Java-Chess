package Classes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Key implements KeyListener {

    public static boolean keyPressed;
    public static char keyBeingPressed;

    @Override
    public void keyTyped (KeyEvent e) {
        keyBeingPressed = e.getKeyChar();
    }

    @Override
    public void keyPressed (KeyEvent e) {
        keyPressed = true;
        keyBeingPressed = e.getKeyChar();
    }

    @Override
    public void keyReleased (KeyEvent e) {
        keyPressed = false;
        keyBeingPressed = e.getKeyChar();
    }
}
