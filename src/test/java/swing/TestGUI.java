package swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.JButton;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.LineBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JSeparator;

import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.ProtoSchemaParser;
import com.squareup.protoparser.Type;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestGUI {

	private JFrame frameMain;
	private JButton btnChooseReqPb, btnChooseRespPb;
	private JComboBox cbxReqPbType, cbxRespPbType;
	private JLabel lblError;
	private JFileChooser fc;
	private File reqPBFile, respPBFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGUI window = new TestGUI();
					window.frameMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameMain = new JFrame();
		fc = new JFileChooser();
		frameMain.setTitle("Protobuf测试工具v1.0");
		frameMain.setBounds(100, 100, 705, 472);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frameMain.setJMenuBar(menuBar);

		JMenu menuFile = new JMenu("文件");
		menuBar.add(menuFile);

		JMenuItem menuItemOpen = new JMenuItem("打开");
		menuFile.add(menuItemOpen);

		JMenuItem menuItemExit = new JMenuItem("�?�?");
		menuFile.add(menuItemExit);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		frameMain.getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 0.5;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frameMain.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel label = new JLabel("接口请求地址�?");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.weightx = 0.5;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.anchor = GridBagConstraints.SOUTHWEST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);

		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.SOUTH;
		gbc_comboBox.weightx = 20.0;
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		panel.add(comboBox, gbc_comboBox);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] { "3.0" }));
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.weightx = 0.5;
		gbc_comboBox_1.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 2;
		gbc_comboBox_1.gridy = 0;
		panel.add(comboBox_1, gbc_comboBox_1);

		JButton btnNewButton = new JButton("请求");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnNewButton, gbc_btnNewButton);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.weighty = 20.0;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		frameMain.getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		panel_1.add(splitPane);

		JPanel panel_3 = new JPanel();
		splitPane.setLeftComponent(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel_3.rowHeights = new int[] { -17, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		btnChooseReqPb = new JButton("选择请求协议");
		GridBagConstraints gbc_btnChooseReqPb = new GridBagConstraints();
		gbc_btnChooseReqPb.gridwidth = 2;
		gbc_btnChooseReqPb.insets = new Insets(0, 0, 0, 5);
		gbc_btnChooseReqPb.gridx = 0;
		gbc_btnChooseReqPb.gridy = 0;
		panel_3.add(btnChooseReqPb, gbc_btnChooseReqPb);

		{
			btnChooseReqPb.addActionListener(new FileChooseActionListener());
		}

		cbxReqPbType = new JComboBox();
		GridBagConstraints gbc_cbxReqPbType = new GridBagConstraints();
		gbc_cbxReqPbType.gridwidth = 0;
		gbc_cbxReqPbType.insets = new Insets(0, 0, 0, 5);
		gbc_cbxReqPbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxReqPbType.gridx = 2;
		gbc_cbxReqPbType.gridy = 0;
		panel_3.add(cbxReqPbType, gbc_cbxReqPbType);

		JLabel label_1 = new JLabel("");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 3;
		gbc_label_1.gridy = 0;
		panel_3.add(label_1, gbc_label_1);

		JPanel panel_4 = new JPanel();
		splitPane.setRightComponent(panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel_4.rowHeights = new int[] { 0, 0 };
		gbl_panel_4.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_4.setLayout(gbl_panel_4);

		btnChooseRespPb = new JButton("选择响应协议");
		GridBagConstraints gbc_btnChooseRespPb = new GridBagConstraints();
		gbc_btnChooseRespPb.gridwidth = 2;
		gbc_btnChooseRespPb.insets = new Insets(0, 0, 0, 5);
		gbc_btnChooseRespPb.gridx = 0;
		gbc_btnChooseRespPb.gridy = 0;
		panel_4.add(btnChooseRespPb, gbc_btnChooseRespPb);

		{
			btnChooseRespPb.addActionListener(new FileChooseActionListener());
		}

		JLabel label_2 = new JLabel("");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 0, 5);
		gbc_label_2.gridx = 2;
		gbc_label_2.gridy = 0;
		panel_4.add(label_2, gbc_label_2);

		cbxRespPbType = new JComboBox();
		GridBagConstraints gbc_cbxRespPbType = new GridBagConstraints();
		gbc_cbxRespPbType.gridwidth = 0;
		gbc_cbxRespPbType.insets = new Insets(0, 0, 0, 5);
		gbc_cbxRespPbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxRespPbType.gridx = 2;
		gbc_cbxRespPbType.gridy = 0;
		panel_4.add(cbxRespPbType, gbc_cbxRespPbType);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.weighty = 1.0;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		frameMain.getContentPane().add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

		lblError = new JLabel("信息");
		lblError.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(lblError);
	}

	private class FileChooseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fc.showOpenDialog(frameMain);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				List<Type> protoTypes = getProtoType(file);
				if (e.getSource() == btnChooseReqPb) {
					reqPBFile = file;
					fillComboBox(cbxReqPbType, protoTypes);
				} else if (e.getSource() == btnChooseRespPb) {
					respPBFile = file;
					fillComboBox(cbxRespPbType, protoTypes);
				}

			} else {
				// cancle
			}
		}

	}

	private void fillComboBox(JComboBox cbx, List<Type> types) {
		cbx.removeAllItems();
		for (Type t : types) {
			cbx.addItem(t.getName());
		}
	}

	private List<Type> getProtoType(File file) {
		List<Type> types = new ArrayList<Type>();
		FileInputStream is = null;
		ProtoFile protoFile;
		try {
			is = new FileInputStream(file);
			protoFile = ProtoSchemaParser.parseUtf8("jprotobuf_autogenerate",
					is);
			types = protoFile.getTypes();
		} catch (Throwable e) {
			processError(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					processError(e);
				}
			}
		}
		return types;
	}

	private void processError(Throwable e) {
		lblError.setText(e.toString());
	}

}
