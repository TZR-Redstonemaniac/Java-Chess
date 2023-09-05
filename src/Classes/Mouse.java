package Classes;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {

    private static boolean pressed;

    private static boolean grabbed;

    @Override
    public void mouseClicked (MouseEvent e) {
        //Do Nothing
    }

    @Override
    public void mousePressed (MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            pressed = true; //NOSONAR
        }
    }

    @Override
    public void mouseReleased (MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            pressed = false; //NOSONAR
        }
    }

    @Override
    public void mouseEntered (MouseEvent e) {
        //Do Nothing
    }

    @Override
    public void mouseExited (MouseEvent e) {
        //Do Nothing
    }

    public static boolean GetPressed (){
        return pressed;
    }

    public static boolean GetGrabbed (){
        return grabbed;
    }

    public static void SetGrabbed (boolean x) {
        grabbed = x;
    }
}
