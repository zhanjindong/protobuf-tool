package protobuf;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TestSwing {

	private JFrame frame;
	private JPopupMenu popMenu;
	private JTable tblHttpHeader;
	private JButton btnAddHeader, btnDeleteHeader;
	private JScrollPane spHeader;
	private JDialog dialog;
	private HttpHeaderDialog myDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestSwing window = new TestSwing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popMenu = new JPopupMenu();

		final JButton btnHttp = new JButton("HTTP头");
		btnHttp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myDialog.setLocationRelativeTo(btnHttp);
				myDialog.pack();
				myDialog.setVisible(true);
			}
		});
		frame.getContentPane().add(btnHttp, BorderLayout.NORTH);

		// frame.getContentPane().add(panel, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.DARK_GRAY));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnAddHeader = new JButton("添加");
		GridBagConstraints gbc_btnAddHeader = new GridBagConstraints();
		gbc_btnAddHeader.anchor = GridBagConstraints.NORTH;
		gbc_btnAddHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddHeader.weightx = 10.0;
		gbc_btnAddHeader.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddHeader.gridx = 0;
		gbc_btnAddHeader.gridy = 0;
		panel.add(btnAddHeader, gbc_btnAddHeader);
		btnAddHeader.addActionListener(new HeaderButtonActionListener());

		btnDeleteHeader = new JButton("删除");
		btnDeleteHeader.addActionListener(new HeaderButtonActionListener());
		GridBagConstraints gbc_btnDeleteHeader = new GridBagConstraints();
		gbc_btnDeleteHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteHeader.weightx = 10.0;
		gbc_btnDeleteHeader.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteHeader.gridx = 1;
		gbc_btnDeleteHeader.gridy = 0;
		panel.add(btnDeleteHeader, gbc_btnDeleteHeader);

		tblHttpHeader = new JTable();
		tblHttpHeader.setModel(new DefaultTableModel(
				new Object[][] { { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null },
						{ null, null }, { null, null }, { null, null }, },
				new String[] { "New column", "New column" }));
		GridBagConstraints gbc_tbl_header = new GridBagConstraints();
		gbc_tbl_header.gridwidth = 0;
		gbc_tbl_header.gridheight = 0;
		gbc_tbl_header.insets = new Insets(0, 0, 0, 5);
		gbc_tbl_header.fill = GridBagConstraints.BOTH;
		gbc_tbl_header.gridx = 0;
		gbc_tbl_header.gridy = 1;
		panel.add(tblHttpHeader, gbc_tbl_header);

		myDialog = new HttpHeaderDialog(frame, "HTTP头", true);
		// frame.getContentPane().add(panel);

	}

	class HeaderButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddHeader) {
				DefaultTableModel model = (DefaultTableModel) tblHttpHeader
						.getModel();
				model.addRow(new Object[] { null, null });
			}
			if (e.getSource() == btnDeleteHeader) {
				removeTableSelectedRows(tblHttpHeader);
			}
		}

	}

	private void removeTableSelectedRows(JTable table) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int numRows = table.getSelectedRows().length;
		for (int i = 0; i < numRows; i++) {
			model.removeRow(table.getSelectedRow());
		}
	}

	class HttpHeaderDialog extends JDialog {
		private JPanel panel;

		public HttpHeaderDialog(JFrame frame, String title, boolean modal) {
			super(frame, title, modal);
			setLayout(new GridBagLayout());
			btnAddHeader = new JButton("添加");
			GridBagConstraints gbc_btnAddHeader = new GridBagConstraints();
			gbc_btnAddHeader.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnAddHeader.weightx = 1.0;
			gbc_btnAddHeader.insets = new Insets(0, 0, 5, 5);
			gbc_btnAddHeader.gridx = 0;
			gbc_btnAddHeader.gridy = 0;
			add(btnAddHeader, gbc_btnAddHeader);
			btnAddHeader.addActionListener(new HeaderButtonActionListener());

			btnDeleteHeader = new JButton("删除");
			btnDeleteHeader.addActionListener(new HeaderButtonActionListener());
			GridBagConstraints gbc_btnDeleteHeader = new GridBagConstraints();
			gbc_btnDeleteHeader.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnDeleteHeader.weightx = 1.0;
			gbc_btnDeleteHeader.insets = new Insets(0, 0, 5, 0);
			gbc_btnDeleteHeader.gridx = 1;
			gbc_btnDeleteHeader.gridy = 0;
			add(btnDeleteHeader, gbc_btnDeleteHeader);

			tblHttpHeader = new JTable() {
				@Override
				public Dimension getPreferredScrollableViewportSize() {
					int height = 5 * getRowHeight();
					int width = getPreferredSize().width;
					return new Dimension(300, 200);
				}
			};
			tblHttpHeader.setModel(new DefaultTableModel(new Object[][] {},
					new String[] { "键", "值" }));
			GridBagConstraints gbc_tbl_header = new GridBagConstraints();
			gbc_tbl_header.gridwidth = 0;
			gbc_tbl_header.gridheight = 0;
			gbc_tbl_header.insets = new Insets(0, 0, 0, 5);
			gbc_tbl_header.fill = GridBagConstraints.HORIZONTAL;
			gbc_tbl_header.gridx = 0;
			gbc_tbl_header.gridy = 1;
			JScrollPane scrollPane = new JScrollPane(tblHttpHeader);
			add(scrollPane, gbc_tbl_header);

			pack();
			setResizable(false);
		}
	}

}
