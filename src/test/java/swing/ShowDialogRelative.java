package swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ShowDialogRelative extends JFrame {

    private JFrame frame = new JFrame();
    private static final long serialVersionUID = 1L;
    private JMenuBar MenuBar;
    private JMenu File;
    private JMenuItem Exit;
    private JMenuItem ShowDialog;
    private JDialog dialog;
    private Point location;

    public ShowDialogRelative() {
        Exit = new JMenuItem(" Exit ");
        Exit.addActionListener(new ExitListener());
        ShowDialog = new JMenuItem(" Show Dialog ");
        ShowDialog.addActionListener(showingDialog());
        File = new JMenu(" File ");
        File.add(Exit);
        File.add(ShowDialog);
        MenuBar = new JMenuBar();
        MenuBar.add(File);
        frame.addWindowListener(exitListener);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(MenuBar);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setLocation(100, 100);
        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createDialog();
            }
        });
    }

    private void createDialog() {
        JButton btn = new JButton(" Save changes ");
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //some stuff for saving whatewer programatically
                dialog.setVisible(false);
            }
        });
        JButton btn1 = new JButton(" Ignore changes ");
        btn1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(false);
            }
        });
        dialog = new JDialog();
        dialog.add(btn);
        dialog.add(btn1);
        dialog.setVisible(false);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setLayout(new GridLayout(2, 0, 10, 10));
        JPanel pane = (JPanel) dialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        dialog.addWindowListener(closeListener);
        dialog.pack();
    }
//
    private WindowListener exitListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(frame,
                    "Are You Sure to Close this Application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                System.exit(1);
            }
        }

        @Override
        public void windowIconified(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(frame,
                    "Are You Sure to Close this Application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                //System.exit(1);
            }
        }
    };
//
    private WindowListener closeListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(dialog,
                    "Are you want to save changes",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                //some stuff for saving whatewer programatically
                dialog.setVisible(true);
            } else if (confirm == 1) {// nothing only hide JDialog
                dialog.setVisible(true);
            }
        }
    };

    private Action showingDialog() {
        return new AbstractAction("Show Dialog") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable doRun = new Runnable() {

                    @Override
                    public void run() {
                        dialog.setVisible(false);
                        location = frame.getLocationOnScreen();
                        int x = location.x;
                        int y = location.y;
                        //dialog.setLocation(x, y);
                        dialog.setLocationRelativeTo(frame);
                        dialog.setVisible(true);
                    }
                };
                SwingUtilities.invokeLater(doRun);
            }
        };
    }

    private class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showOptionDialog(frame,
                    "Are You Sure to Close this Application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
            	ShowDialogRelative cf = new ShowDialogRelative();
            }
        });
    }
}