package protobuf;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class ActionExample {

    public ActionExample() {

        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.addMenuListener(new SampleMenuListener());
        JMenu menu1 = new JMenu("Tool");
        menu1.setMnemonic(KeyEvent.VK_T);
        menu1.addMenuListener(new SampleMenuListener());
        JFrame f = new JFrame("ActionExample");
        JMenuBar mb = new JMenuBar();
        mb.add(menu);
        mb.add(menu1);
        f.setJMenuBar(mb);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ActionExample actionExample = new ActionExample();
            }
        });
    }
}

class SampleMenuListener implements MenuListener {

    public void menuSelected(MenuEvent e) {
        System.out.println("menuSelected");
    }

    public void menuDeselected(MenuEvent e) {
        System.out.println("menuDeselected");
    }

    public void menuCanceled(MenuEvent e) {
        System.out.println("menuCanceled");
    }
}