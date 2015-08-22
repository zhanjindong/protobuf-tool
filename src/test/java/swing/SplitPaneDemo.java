package swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

//SplitPaneDemo itself is not a visible component.
public class SplitPaneDemo extends JPanel
                          implements ListSelectionListener {
    private JLabel picture;
    private JList list;
    private JSplitPane splitPane;
    private String[] imageNames = { "Bird", "Cat", "Doddddddddddddddddddddg", "Rabbit", "Pig", "dukeWaveRed",
        "kathyCosmo", "lainesTongue", "left", "middle", "right", "stickerface"};
    public SplitPaneDemo() {

        //Create the list of images and put it in a scroll pane.
        
        list = new JList(imageNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        
       
        JScrollPane listScrollPane = new JScrollPane(list);
        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.CENTER);
        
        JScrollPane pictureScrollPane = new JScrollPane(picture);

        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   listScrollPane, pictureScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        pictureScrollPane.setMinimumSize(minimumSize);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(400, 200));
        updateLabel(imageNames[list.getSelectedIndex()]);
    }
    
    //Listens to the list
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        updateLabel(imageNames[list.getSelectedIndex()]);
    }
    
    //Renders the selected image
    protected void updateLabel (String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".gif");
        picture.setIcon(icon);
        if  (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
    }

    //Used by SplitPaneDemo2
    public JList getImageList() {
        return list;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

   
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
       java.net.URL imgURL = SplitPaneDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("SplitPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SplitPaneDemo splitPaneDemo = new SplitPaneDemo();
        frame.getContentPane().add(splitPaneDemo.getSplitPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

      
}