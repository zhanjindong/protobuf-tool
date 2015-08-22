package swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class TestDialog {

	public static void main(String[] args) {

		JFrame f = new JFrame();
		final MyDialog dialog = new MyDialog(f, "Title", true);
		Timer timer = new Timer(10000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();

		dialog.setVisible(true);
		System.out.println("Dialog closed");

	}

	private static class MyDialog extends JDialog {

		public MyDialog(JFrame frame, String title, boolean modal) {
			super(frame, title, modal);

			setLayout(new BorderLayout());
			add(new JButton("CENTER"), BorderLayout.CENTER);

			pack();
			setLocationRelativeTo(null);

		}
	}
}