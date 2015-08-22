package swing;

import java.awt.BorderLayout;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;

public class TreeEditJTextField {
  public static void main(String args[]) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Properties props = System.getProperties();
    JTree tree = new JTree(props);
        
    
    JTextField textField = new JTextField();
    TreeCellEditor editor = new DefaultCellEditor(textField);

    tree.setEditable(true);
    tree.setCellEditor(editor);
    
    JScrollPane scrollPane = new JScrollPane(tree);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.setSize(300, 150);
    frame.setVisible(true);
  }
}
