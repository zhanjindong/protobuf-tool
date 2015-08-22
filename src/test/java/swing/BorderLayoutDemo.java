package swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BorderLayoutDemo {

	public static void addComponentsToPane(Container pane) {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, -1, 100);
		pane.add(panel, BorderLayout.PAGE_START);
		panel = new JPanel();
		panel.setBounds(0, 0, -1, 600);
		JButton button1 = new JButton("[TOP]");
		button1.setBounds(0, 0, 100, 200);
		panel.add(button1);
		panel.setPreferredSize(new Dimension(200, 100));
		pane.add(panel, BorderLayout.CENTER);
		panel = new JPanel();
		pane.add(panel, BorderLayout.LINE_START);
		panel = new JPanel();
		pane.add(panel, BorderLayout.PAGE_END);
		panel = new JPanel();
		JButton button = new JButton("[TOP]");
		button.setBounds(0, 0, 100, 200);
		panel.add(button);
		pane.add(panel, BorderLayout.LINE_END);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("BorderLayoutManage");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}