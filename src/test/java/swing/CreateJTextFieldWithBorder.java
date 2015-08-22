package swing;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class CreateJTextFieldWithBorder extends JFrame {

	private static final long serialVersionUID = 1L;

	public CreateJTextFieldWithBorder() {

		// set flow layout for the frame
		this.getContentPane().setLayout(new FlowLayout());

		// create JTextField
		JTextField field = new JTextField(20);
		field.setText("Java Code Geeks");

		// create a line border with the specified color and width
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);

		// set the border of this component
		field.setBorder(border);

		// add textfield to frame
		add(field);

	}

	private static void createAndShowGUI() {

		// Create and set up the window.

		JFrame frame = new CreateJTextFieldWithBorder();

		// Display the window.

		frame.pack();

		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {

		// Schedule a job for the event-dispatching thread:

		// creating and showing this application's GUI.

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				createAndShowGUI();

			}

		});
	}

}