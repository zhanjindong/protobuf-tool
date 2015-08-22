package swing;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TestTree {
	public static void main(String[] args) {
		 try {
	            JFrame.setDefaultLookAndFeelDecorated(true);
	            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	            MyTree userframe = new MyTree();
	        } catch (ClassNotFoundException ex) {
	            Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (InstantiationException ex) {
	            Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IllegalAccessException ex) {
	            Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (UnsupportedLookAndFeelException ex) {
	            Logger.getLogger(MyTree.class.getName()).log(Level.SEVERE, null, ex);
	        }
	}
}
