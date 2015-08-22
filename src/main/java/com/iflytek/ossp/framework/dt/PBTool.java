package com.iflytek.ossp.framework.dt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.iflytek.ossp.framework.dt.utils.DateUtils;
import com.iflytek.ossp.framework.dt.utils.IOUtils;
import com.iflytek.ossp.framework.dt.utils.NormalizationUtils;
import com.iflytek.ossp.framework.dt.utils.PathUtils;
import com.iflytek.ossp.framework.dt.utils.ReflectionUtil;
import com.iflytek.ossp.framework.dt.utils.ResourceUtils;
import com.iflytek.ossp.framework.dt.utils.SeparatorUtils;
import com.iflytek.ossp.framework.dt.utils.UrlConnectionUtils;

public class PBTool {
	private static Map<String, IDLProxyObject> reqProxyObjects = new HashMap<String, IDLProxyObject>();
	private static Map<String, IDLProxyObject> respProxyObjects = new HashMap<String, IDLProxyObject>();
	private static Map<String, LinkedHashMap<String, Field>> cachedFileInfo = new HashMap<String, LinkedHashMap<String, Field>>();
	private static Map<String, TreePathInfo> treePathInfos = new TreeMap<String, PBTool.TreePathInfo>();

	private static String appDir = "";
	private static String workDir = "";
	private static String savedDir = "";
	private static String iconPath = "";
	private static Map<String, File> openPbFiles = new HashMap<String, File>();

	{
		appDir = PathUtils.getAppDir(PBTool.class);
		workDir = appDir + SeparatorUtils.getFileSeparator() + "pbtool"
				+ SeparatorUtils.getFileSeparator();
		savedDir = workDir + "saved" + SeparatorUtils.getFileSeparator();
		try {
			iconPath = ResourceUtils.loadResource("classpath:icon.png")
					.getFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResourceUtils.createDirIfNotExist(workDir);
		ResourceUtils.createDirIfNotExist(savedDir);
	}

	private JFrame frameMain;
	private JButton btnChooseReqPb, btnChooseRespPb, btnRequest, btnAddHeader;
	private JLabel lblPrompt, lblOpneFilePath;
	private JFileChooser fc;
	private String reqProtoContent, respProtoContent;
	private JComboBox cbxReqAddr, cbxReqPbType, cbxRespPbType;
	private JTree treeReq, treeResp;
	private JPopupMenu popMenu;
	private JMenuItem menuItemAdd, menuItemDelete, menuItemSavePbData;// 右键上下文
	private JMenuItem menuItemExport2Xml, menuItemExport2Json,
			menuItemImportFromXml, menuItemImportFromJson, menuItemOpen,
			menuItemSave, menuItemSaveOther;
	private TableDialog diaHeader;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PBTool window = new PBTool();
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
	public PBTool() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameMain = new JFrame();
		ImageIcon img = new ImageIcon(iconPath);
		frameMain.setIconImage(img.getImage());
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(appDir));
		frameMain.setTitle("Protobuf测试工具v1.1");
		frameMain.setBounds(100, 100, 705, 472);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frameMain.setJMenuBar(menuBar);
		JMenu menuFile = new JMenu("文件");
		menuBar.add(menuFile);
		menuItemOpen = new JMenuItem("打开");
		menuFile.add(menuItemOpen);
		menuItemSave = new JMenuItem("保存");
		menuFile.add(menuItemSave);
		menuItemSaveOther = new JMenuItem("另存为");
		menuFile.add(menuItemSaveOther);

		JMenu menuTool = new JMenu("工具");
		menuBar.add(menuTool);
		// menuItemSave = new JMenuItem("保存");
		// menuFile.add(menuItemSave);
		menuItemExport2Xml = new JMenuItem("导出（XML）");
		menuTool.add(menuItemExport2Xml);
		menuItemExport2Json = new JMenuItem("导出（JSON）");
		menuTool.add(menuItemExport2Json);
		menuItemImportFromXml = new JMenuItem("导入（XML）");
		menuTool.add(menuItemImportFromXml);
		menuItemImportFromJson = new JMenuItem("导入（JSON）");
		menuTool.add(menuItemImportFromJson);

		{
			menuItemOpen.addActionListener(new MenuItemActionListener());
			menuItemSave.addActionListener(new MenuItemActionListener());
			menuItemSaveOther.addActionListener(new MenuItemActionListener());

			menuItemExport2Xml.addActionListener(new MenuItemActionListener());
			menuItemExport2Json.addActionListener(new MenuItemActionListener());
			menuItemImportFromXml
					.addActionListener(new MenuItemActionListener());
			menuItemImportFromJson
					.addActionListener(new MenuItemActionListener());
		}

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

		JLabel label = new JLabel("接口请求地址：");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.weightx = 0.5;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);

		cbxReqAddr = new JComboBox();
		initReqAddr();
		cbxReqAddr.setEditable(true);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.weightx = 20.0;
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.fill = GridBagConstraints.BOTH;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		panel.add(cbxReqAddr, gbc_comboBox);

		btnAddHeader = new JButton("HTTP头");
		btnAddHeader.addActionListener(new ButtonActionListener());
		GridBagConstraints gbc_btnAddHeader = new GridBagConstraints();
		gbc_btnAddHeader.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddHeader.fill = GridBagConstraints.BOTH;
		gbc_btnAddHeader.weightx = 1.0;
		gbc_btnAddHeader.gridx = 2;
		gbc_btnAddHeader.gridy = 0;
		panel.add(btnAddHeader, gbc_btnAddHeader);

		btnRequest = new JButton("请求");
		btnRequest.addActionListener(new ButtonActionListener());
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.weightx = 1.0;
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnRequest, gbc_btnNewButton);

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
		gbl_panel_3.columnWidths = new int[] { 0, 31, 0, 0, 0, 0 };
		gbl_panel_3.rowHeights = new int[] { -17, 0, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		btnChooseReqPb = new JButton("选择请求协议");
		GridBagConstraints gbc_btnChooseReqPb = new GridBagConstraints();
		gbc_btnChooseReqPb.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChooseReqPb.weightx = 0.5;
		gbc_btnChooseReqPb.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseReqPb.gridx = 0;
		gbc_btnChooseReqPb.gridy = 0;
		panel_3.add(btnChooseReqPb, gbc_btnChooseReqPb);

		{
			btnChooseReqPb.addActionListener(new FileChooseActionListener());
		}

		cbxReqPbType = new JComboBox();
		cbxReqPbType.addActionListener(new ComboBoxActionListener());
		GridBagConstraints gbc_cbxReqPbType = new GridBagConstraints();
		gbc_cbxReqPbType.weightx = 20.0;
		gbc_cbxReqPbType.gridwidth = 0;
		gbc_cbxReqPbType.insets = new Insets(0, 0, 5, 5);
		gbc_cbxReqPbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxReqPbType.gridx = 1;
		gbc_cbxReqPbType.gridy = 0;
		panel_3.add(cbxReqPbType, gbc_cbxReqPbType);

		JLabel label_1 = new JLabel("");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 3;
		gbc_label_1.gridy = 0;
		panel_3.add(label_1, gbc_label_1);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 6;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel_3.add(scrollPane, gbc_scrollPane);

		treeReq = new JTree();
		treeReq.setCellRenderer(new LeafFieldNodeCellRenderer());// 自定义节点的展示界面
		treeReq.setCellEditor(new LeafFieldNodeEditor(treeReq));
		treeReq.setEditable(true);
		treeReq.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("空") {
			{
			}
		}));
		treeReq.addMouseListener(new TreeEventListener(treeReq));
		scrollPane.setViewportView(treeReq);

		JPanel panel_4 = new JPanel();
		splitPane.setRightComponent(panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 119, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_4.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_4.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 0.0,
				1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel_4.setLayout(gbl_panel_4);

		btnChooseRespPb = new JButton("选择响应协议");
		GridBagConstraints gbc_btnChooseRespPb = new GridBagConstraints();
		gbc_btnChooseRespPb.weightx = 0.5;
		gbc_btnChooseRespPb.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChooseRespPb.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseRespPb.gridx = 0;
		gbc_btnChooseRespPb.gridy = 0;
		panel_4.add(btnChooseRespPb, gbc_btnChooseRespPb);

		{
			btnChooseRespPb.addActionListener(new FileChooseActionListener());
		}

		cbxRespPbType = new JComboBox();
		cbxRespPbType.addActionListener(new ComboBoxActionListener());
		GridBagConstraints gbc_cbxRespPbType = new GridBagConstraints();
		gbc_cbxRespPbType.weightx = 20.0;
		gbc_cbxRespPbType.gridwidth = 0;
		gbc_cbxRespPbType.insets = new Insets(0, 0, 5, 5);
		gbc_cbxRespPbType.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxRespPbType.gridx = 1;
		gbc_cbxRespPbType.gridy = 0;
		panel_4.add(cbxRespPbType, gbc_cbxRespPbType);

		JLabel label_2 = new JLabel("");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 4;
		gbc_label_2.gridy = 0;
		panel_4.add(label_2, gbc_label_2);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 8;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		panel_4.add(scrollPane_1, gbc_scrollPane_1);

		treeResp = new JTree();
		treeResp.setEditable(true);
		treeResp.addMouseListener(new TreeEventListener(treeResp));
		treeResp.setCellRenderer(new LeafFieldNodeCellRenderer());// 自定义节点的展示界面
		treeResp.setCellEditor(new LeafFieldNodeEditor(treeResp));
		treeResp.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("空") {
			private static final long serialVersionUID = -1948778900894534171L;
		}));
		scrollPane_1.setViewportView(treeResp);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.weighty = 1.0;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		frameMain.getContentPane().add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 24, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 15, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		lblPrompt = new JLabel("信息");
		lblPrompt.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.weightx = 50.0;
		gbc_lblError.insets = new Insets(0, 0, 0, 5);
		gbc_lblError.anchor = GridBagConstraints.WEST;
		gbc_lblError.gridx = 0;
		gbc_lblError.gridy = 0;
		panel_2.add(lblPrompt, gbc_lblError);

		JLabel lblAuthor = new JLabel("by jdzhan@live.com");
		lblAuthor.setForeground(Color.BLUE);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		panel_2.add(lblAuthor, gbc_lblNewLabel);

		lblOpneFilePath = new JLabel();
		lblOpneFilePath.setForeground(Color.BLACK);
		GridBagConstraints gbc_lblOpenFilePath = new GridBagConstraints();
		gbc_lblOpenFilePath.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblOpenFilePath.gridwidth = 0;
		gbc_lblOpenFilePath.gridx = 0;
		gbc_lblOpenFilePath.gridy = 1;
		panel_2.add(lblOpneFilePath, gbc_lblOpenFilePath);

		diaHeader = new TableDialog(frameMain, "编辑HTTP头", true);

		// ////////////////////////////////////
		popMenuInit();
	}

	private void initReqAddr() {
		String fileName = workDir + "addr.txt";
		File file = new File(fileName);
		if (!file.exists()) {
			return;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				if (StringUtils.isEmpty(line)) {
					continue;
				}
				cbxReqAddr.addItem(line);
			}
			br.close();
		} catch (Exception e) {
			processError(e);
		} finally {
			IOUtils.close(br);
		}
	}

	private Map<String, String> getHeaderMapFromFile(String fileName) {
		Map<String, String> headers = new HashMap<String, String>();
		File file = new File(fileName);
		if (!file.exists()) {
			return headers;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					fileName)));
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				String[] arr = line.split("~");
				if (arr.length == 2) {
					headers.put(arr[0], arr[1]);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.close(br);
		}
		return headers;
	}

	private void fillTree(JTree tree, String msg, boolean isReq) {
		String path = "[" + msg + "]";
		IDLProxyObject proxy = getProxyObject(msg);
		treePathInfos.put(path, new TreePathInfo(path, "", proxy.getTarget()
				.getClass(), null));

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new UserData(
				msg, path));
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		LinkedHashMap<String, Field> fields = getFields(proxy.getTarget()
				.getClass());
		for (Entry<String, Field> entry : fields.entrySet()) {
			Class<?> type = entry.getValue().getType();
			path = "[" + msg + ", " + entry.getKey() + "]";
			treePathInfos.put(path, new TreePathInfo(path, entry.getKey(),
					type, proxy.getTarget().getClass()));
			UserData userObject = new UserData(entry.getKey(), path);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(
					userObject);

			root.add(child);

		}

		model.setRoot(root);
		if (tree == treeReq) {
			fetchCachedData();
		}
		PBUtils.reloadTree(tree);
	}

	private void fetchCachedData() {
		String msg = cbxReqPbType.getSelectedItem().toString();
		if (openPbFiles.containsKey(msg)) {
			openData(openPbFiles.get(msg));
		} else {
			lblOpneFilePath.setText("");
		}
	}

	private void parseProroFile(Map<String, IDLProxyObject> map, JComboBox cbx,
			String protoContent) {
		try {
			cbx.removeAllItems();
			map.clear();
			Map<String, IDLProxyObject> objs = ProtoIDLProxy
					.create(protoContent);
			map.putAll(objs);
			for (Entry<String, IDLProxyObject> entry : objs.entrySet()) {
				cbx.addItem(entry.getKey());
			}
		} catch (Throwable e1) {
			processError(e1);
		}
	}

	private void clearLabelInfo() {
		lblPrompt.setText("信息");
		lblPrompt.setForeground(Color.BLACK);
	}

	private void processError(Throwable e) {
		e.printStackTrace();
		lblPrompt.setText(e.toString());
		lblPrompt.setForeground(Color.RED);
	}

	private void popMenuInit() {
		popMenu = new JPopupMenu();
		menuItemAdd = new JMenuItem("添加repeated字段");
		menuItemDelete = new JMenuItem("删除");
		menuItemSavePbData = new JMenuItem("保存PB数据");
		menuItemAdd.addActionListener(new TreeAddViewMenuEvent(treeReq));
		menuItemDelete.addActionListener(new TreeAddViewMenuEvent(treeReq));
		menuItemSavePbData.addActionListener(new TreeAddViewMenuEvent(treeReq));
		popMenu.add(menuItemDelete);
	}

	class MenuItemActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == menuItemOpen) {// 打开
				int returnVal = fc.showOpenDialog(frameMain);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					lblOpneFilePath.setText("当前数据文件：" + file.getPath());
					// String msg = cbxReqPbType.getSelectedItem().toString();
					// openPbFiles.put(msg, file);
					openData(file);
				}
			}
			if (e.getSource() == menuItemSave) {
				lblPrompt.setText("保存...");
				try {
					saveData();
					lblPrompt.setText("数据已保存");
				} catch (Exception exp) {
					processError(exp);
				}
			}
			if (e.getSource() == menuItemSaveOther) {// 另存为
				int returnVal = fc.showSaveDialog(frameMain);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					SavedObject saved = fetchSavedObject();
					byte[] data = SerializationUtils.serialize(saved);
					ResourceUtils.saveFileBeforeDeleteOld(file, data);
				}
			}
		}
	}

	// private void saveAddr() {
	// if (cbxReqAddr == null) {
	// return;
	// }
	// int count = cbxReqAddr.getItemCount();
	// if (count <= 0) {
	// return;
	// }
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < count; i++) {
	// Object addr = cbxReqAddr.getItemAt(i);
	// if (addr == null || StringUtils.isEmpty(addr.toString())) {
	// continue;
	// }
	// sb.append(addr.toString() + "\r\n");
	// }
	// String fileName = workDir + "addr.txt";
	// ResourceUtils.saveFileBeforeDeleteOld(fileName,
	// com.iflytek.ossp.framework.dt.utils.StringUtils.encodeUTF8(sb
	// .toString()));
	// }

	private SavedObject fetchSavedObject() {
		SavedObject saved = new SavedObject();
		String url = "";
		if (cbxReqAddr.getSelectedIndex() > -1) {
			url = cbxReqAddr.getSelectedItem().toString();
		}
		Map<String, String> headers = diaHeader.getHeaders();
		byte[] reqData = null;
		try {
			reqData = fetchPbDataFromTree(treeReq);
		} catch (Exception e) {
			reqData = null;
		}
		byte[] respData = null;
		try {
			respData = fetchPbDataFromTree(treeResp);
		} catch (Exception e) {
			respData = null;
		}

		String reqMsg = "";
		if (cbxReqPbType.getSelectedIndex() > -1) {
			reqMsg = cbxReqPbType.getSelectedItem().toString();
		}
		String respMsg = "";
		if (cbxRespPbType.getSelectedIndex() > -1) {
			respMsg = cbxRespPbType.getSelectedItem().toString();
		}

		saved.setReqUrl(url);
		saved.setHeaders(headers);
		saved.setReqData(reqData);
		saved.setReqProto(reqProtoContent);
		saved.setReqMsg(reqMsg);
		saved.setRespData(respData);
		saved.setRespProto(respProtoContent);
		saved.setRespMsg(respMsg);
		return saved;
	}

	private void openData(File file) {
		try {
			byte[] data = IOUtils.read(file);
			SavedObject saved = (SavedObject) SerializationUtils
					.deserialize(data);
			String url = saved.getReqUrl();
			Map<String, String> headers = saved.getHeaders();
			String reqPb = saved.getReqProto();
			byte[] reqData = saved.getReqData();
			String reqMsg = saved.getReqMsg();
			String respPb = saved.getRespProto();
			byte[] respData = saved.getRespData();
			String respMsg = saved.getRespMsg();

			if (!StringUtils.isEmpty(reqPb)) {
				reqProtoContent = reqPb;
				parseProroFile(reqProxyObjects, cbxReqPbType, reqPb);
			}
			if (!StringUtils.isEmpty(respPb)) {
				respProtoContent = respPb;
				parseProroFile(respProxyObjects, cbxRespPbType, respPb);
			}

			// cbxReqAddr.addItem(url);
			// cbxReqAddr.setSelectedItem(url);
			cbxReqPbType.setSelectedItem(reqMsg);
			cbxRespPbType.setSelectedItem(respMsg);
			diaHeader.setHeaders(headers);
			reqProtoContent = reqPb;
			if (reqData != null) {
				setPbDataToTree(treeReq, reqMsg, reqData);
			}
			if (respData != null) {
				setPbDataToTree(treeResp, respMsg, respData);
			}
			openPbFiles.put(reqMsg, file);
			lblOpneFilePath.setText("当前数据文件：" + file.toString());
		} catch (Exception e) {
			processError(e);
		}
	}

	private void saveData() {
		String msg = cbxReqPbType.getSelectedItem().toString();
		String fileName = "";
		if (!openPbFiles.containsKey(msg)) {// 已经打开了文件
			fileName = msg + ".data";
			fileName = savedDir + fileName;
		} else {
			fileName = openPbFiles.get(msg).getPath();
		}

		SavedObject saved = fetchSavedObject();
		byte[] data = SerializationUtils.serialize(saved);
		ResourceUtils.saveFileBeforeDeleteOld(fileName, data);
		lblOpneFilePath.setText("当前数据文件：" + fileName);
	}

	class FileChooseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {// 选择PB文件
			// FileFilter filter = new
			// FileNameExtensionFilter("Protobuf文件","proto");
			// fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(frameMain);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (e.getSource() == btnChooseReqPb) {
					reqProtoContent = ProtoIDLProxy.linkProtoFile(file);
					parseProroFile(reqProxyObjects, cbxReqPbType,
							reqProtoContent);
				} else if (e.getSource() == btnChooseRespPb) {
					respProtoContent = ProtoIDLProxy.linkProtoFile(file);
					parseProroFile(respProxyObjects, cbxRespPbType,
							respProtoContent);
				}
			} else {
				// cancle
			}
		}

	}

	class ButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnRequest) {
				buttonRequestEvent(e);
			}
			if (e.getSource() == btnAddHeader) {
				diaHeader.setLocationRelativeTo(frameMain);
				diaHeader.pack();
				diaHeader.setVisible(true);
			}
		}

	}

	/**
	 * 请求按钮的事件。
	 * 
	 * @param e
	 */
	private void buttonRequestEvent(ActionEvent e) {
		if (!check()) {
			return;
		}
		clearLabelInfo();
		byte[] data;
		long start = System.currentTimeMillis();
		try {
			data = fetchPbDataFromTree(treeReq);
			String url = cbxReqAddr.getSelectedItem().toString();
			// 请求接口
			Map<String, String> headers = diaHeader.getHeaders();
			headers.put("Content-Type", "application/protobuf");
			data = UrlConnectionUtils.post(url, data, "UTF-8", 30000, headers);
			setPbDataToTree(treeResp, cbxRespPbType.getSelectedItem()
					.toString(), data);
		} catch (Throwable exp) {
			processError(exp);
		} finally {
			long end = System.currentTimeMillis();
			String info = "请求耗时：" + (end - start) + "毫秒 " + lblPrompt.getText();
			lblPrompt.setText(info);
		}
	}

	private boolean check() {
		if (cbxReqAddr.getSelectedItem() == null
				|| StringUtils.isEmpty(cbxReqAddr.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frameMain, "请输入合法的请求地址", "提示",
					JOptionPane.PLAIN_MESSAGE);
			return false;
		}

		if (cbxReqPbType.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(frameMain, "请选择请求协议", "提示",
					JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		if (cbxRespPbType.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(frameMain, "请选择响应协议", "提示",
					JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		return true;
	}

	private void expandNodeFromObject(DefaultMutableTreeNode parent,
			Object target) {
		LinkedHashMap<String, Field> fields = PBUtils
				.getAllFieldsByOrder2(target.getClass());
		for (Entry<String, Field> entry : fields.entrySet()) {
			String fieldName = entry.getKey();
			Field field = entry.getValue();
			Object fieldValue = ReflectionUtil.getFieldValue(field, target);
			if (fieldValue == null) {
				continue;
			}

			Class<?> fieldType = field.getType();
			String path = PBUtils.getPath(parent.getPath(), fieldName);
			TreePathInfo pathInfo = new TreePathInfo(path, fieldName,
					fieldType, target.getClass());// 注意第三个参数fieldType;
			treePathInfos.put(path, pathInfo);
			if (ReflectionUtil.isCollection(fieldType)) {
				List<?> list = (List<?>) fieldValue;
				if (CollectionUtils.isEmpty(list)) {
					continue;
				}
				Class<?> elementType = PBUtils.getElementType(fieldName,
						fieldType, target.getClass());
				for (Object object : list) {
					if (PBUtils.isLeafType(elementType)) {
						UserData userObject = new UserData(fieldName, path);
						userObject.setValue(object);
						DefaultMutableTreeNode childNode = PBUtils
								.addChildNode(parent, userObject);
						expandNodeFromObject(childNode, userObject.getValue());
					} else {
						String childMsg = PBUtils.getMsgName(elementType);
						IDLProxyObject childProxy = getProxyObject(childMsg);
						if (childProxy == null) {
							continue;
						}
						childProxy = childProxy.newInstnace();
						childProxy.setTarget(object);
						UserData userObject = new UserData(fieldName, path);
						userObject.setValue(childProxy);
						DefaultMutableTreeNode childNode = PBUtils
								.addChildNode(parent, userObject);
						expandNodeFromObject(childNode, object);
					}
				}
			} else if (PBUtils.isLeafType(fieldType)) {
				UserData userObject = new UserData(fieldName, path);
				userObject.setValue(fieldValue);
				DefaultMutableTreeNode childNode = PBUtils.addChildNode(parent,
						userObject);
				expandNodeFromObject(childNode, userObject.getValue());
			} else {
				String childMsg = PBUtils.getMsgName(fieldType);
				IDLProxyObject childProxy = getProxyObject(childMsg);
				if (childProxy == null) {
					continue;
				}
				childProxy = childProxy.newInstnace();
				childProxy.setTarget(fieldValue);
				UserData userObject = new UserData(fieldName, path);
				userObject.setValue(childProxy);
				DefaultMutableTreeNode childNode = PBUtils.addChildNode(parent,
						userObject);
				expandNodeFromObject(childNode, fieldValue);
			}
		}
	}

	private void setPbDataToTree(JTree tree, String msg, byte[] data)
			throws IOException {
		IDLProxyObject proxy = getProxyObject(msg);
		proxy = proxy.decode(data);
		Object target = proxy.getTarget();
		DefaultMutableTreeNode root = PBUtils.convertToTreeNode(tree.getModel()
				.getRoot());
		root.removeAllChildren();

		expandNodeFromObject(root, target);

		PBUtils.setNodeUserObjectValue(root, proxy);
		PBUtils.reloadTree(tree);
	}

	private IDLProxyObject getProxyObject(String msg) {
		IDLProxyObject proxy = reqProxyObjects.get(msg);
		if (proxy == null) {
			proxy = respProxyObjects.get(msg);
		}
		return proxy;
	}

	// private void walkClass(String fieldName, Field field, Object target) {
	// Class<?> fieldType = field.getType();
	// if (ReflectionUtil.isCollection(fieldType)) {
	// List<?> list = (List<?>) ReflectionUtil
	// .getFieldValue(field, target);
	// if (CollectionUtils.isEmpty(list)) {
	// return;
	// }
	// Class<?> elementType = PBUtils.getElementType(fieldName, fieldType,
	// target.getClass());
	// for (Object object : list) {
	// walkClass(fieldName, field, object);
	// }
	// } else if (PBUtils.isLeafType(fieldType)) {
	// Object value = ReflectionUtil.getFieldValue(field, target);
	// System.out.println(fieldName + " value:"
	// + ReflectionUtil.getFieldValue(field, target));
	// } else {
	// // System.out.println("isClass");
	// }
	// }

	private LinkedHashMap<String, Field> getFields(Class<?> type) {
		LinkedHashMap<String, Field> fields = cachedFileInfo.get(type
				.getSimpleName());
		if (MapUtils.isEmpty(fields)) {
			fields = PBUtils.getAllFieldsByOrder(type);
			cachedFileInfo.put(type.getSimpleName(), fields);
		}
		return fields;
	}

	private byte[] fetchPbDataFromTree(JTree tree) {
		byte[] data = null;
		TreeModel model = tree.getModel();
		Object root = model.getRoot();
		String msg = root.toString();
		IDLProxyObject proxy = getProxyObject(msg).newInstnace();
		PBUtils.setNodeUserObjectValue(root, proxy);
		walkTree(model, root);
		try {
			data = proxy.encode();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return data;
	}

	protected void walkTree(TreeModel model, Object o) {
		int count = model.getChildCount(o);
		for (int i = 0; i < count; i++) {
			Object child = model.getChild(o, i);
			DefaultMutableTreeNode childNode = PBUtils.convertToTreeNode(child);
			UserData childData = fetchUserObject(childNode);
			DefaultMutableTreeNode parentNode = PBUtils
					.convertToTreeNode(childNode.getParent());
			UserData parantData = fetchUserObject(parentNode);
			IDLProxyObject parentProxy = (IDLProxyObject) parantData.getValue();
			if (model.isLeaf(child)) {
				if (childData.getValue() == null)
					continue;
				parentProxy.put(childData.getName(), childData.getValue());
			} else {
				String msg = childData.getFieldMsgName();
				IDLProxyObject childProxy = reqProxyObjects.get(msg)
						.newInstnace();
				childData.setValue(childProxy);
				parentProxy.put(childData.getName(), childProxy.getTarget());
				walkTree(model, child);
			}
		}
	}

	private UserData fetchUserObject(DefaultMutableTreeNode node) {
		Object obj = node.getUserObject();
		if (obj instanceof UserData) {
			return (UserData) obj;
		}
		throw new ClassCastException();
	}

	class ComboBoxActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbxReqPbType) {
				if (cbxReqPbType.getSelectedIndex() > -1) {
					fillTree(treeReq,
							cbxReqPbType.getSelectedItem().toString(), true);
				}
			} else if (e.getSource() == cbxRespPbType) {
				if (cbxRespPbType.getSelectedIndex() > -1) {
					fillTree(treeResp, cbxRespPbType.getSelectedItem()
							.toString(), false);
				}
			} else {
				//
			}
		}

	}

	/**
	 * 树事件的监听器。
	 * 
	 * @author jdzhan,2015年7月4日
	 *
	 */
	class TreeEventListener implements MouseListener {

		private JTree adaptee;

		public TreeEventListener(JTree adaptee) {
			this.adaptee = adaptee;
		}

		private void reqTreeDoubleLeftClickEvent(MouseEvent e) {
			TreePath path = adaptee.getPathForLocation(e.getX(), e.getY());
			if (path == null) {
				return;
			}
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) adaptee
					.getLastSelectedPathComponent();
			if (!currNode.isLeaf())
				return;
			TreePathInfo info = treePathInfos.get(path.toString());
			Class<?> type = null;
			if (info.isCollection()
					&& !ReflectionUtil.isBaseType(info.getElementType())) {
				type = info.getElementType();
			} else {
				if (!ReflectionUtil.isBaseType(info.getType())
						&& !ProtoIDLProxy.isEnumReadable(info.getType())) {
					type = info.getType();
				}
			}

			LinkedHashMap<String, Field> fields = null;
			if (type != null) {
				fields = cachedFileInfo.get(type.getSimpleName());
			}
			if (fields == null) {
				fields = PBUtils.getAllFieldsByOrder(type);
			}

			for (Entry<String, Field> entry : fields.entrySet()) {

				Class<?> childType = entry.getValue().getType();
				String childPath = PBUtils.getPath(path.toString(),
						entry.getKey());

				treePathInfos.put(childPath,
						new TreePathInfo(childPath, entry.getKey(), childType,
								type));

				UserData userObject = new UserData(entry.getKey(), childPath);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(
						userObject);

				currNode.add(child);
			}

			adaptee.updateUI();
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {// 左键双击操做
				if (e.getSource() == treeReq) {
					reqTreeDoubleLeftClickEvent(e);
				}
			}
		}

		public String getProtoMsgName(Class<?> type) {
			return PBUtils.getMsgName(type);
		}

		public void mousePressed(MouseEvent e) {
			if (!SwingUtilities.isRightMouseButton(e))
				return;
			if (e.getSource() != treeReq) {
				return;
			}
			TreePath path = adaptee.getPathForLocation(e.getX(), e.getY());
			if (path == null) {
				return;
			}
			TreePathInfo pathInfo = treePathInfos.get(path.toString());
			if (pathInfo != null && pathInfo.isCollection()) {
				adaptee.setSelectionPath(path);
				popMenu.add(menuItemAdd);
			} else {
				popMenu.remove(menuItemAdd);
			}
			if (((DefaultMutableTreeNode) adaptee
					.getLastSelectedPathComponent()).isRoot()) {
				popMenu.add(menuItemSavePbData);
			} else {
				popMenu.remove(menuItemSavePbData);
			}
			popMenu.show(adaptee, e.getX(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * popmenu点击右键的增加处理
	 */
	class TreeAddViewMenuEvent implements ActionListener {

		private JTree adaptee;

		public TreeAddViewMenuEvent(JTree adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == menuItemAdd) {// 添加repeated字段
				DefaultMutableTreeNode lastSelected = ((DefaultMutableTreeNode) this.adaptee
						.getLastSelectedPathComponent());
				String name = lastSelected.getUserObject().toString();
				String path = PBUtils.getPath(lastSelected.getPath());
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
						new UserData(name, path));
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) lastSelected
						.getParent();
				parent.insert(childNode, parent.getIndex(lastSelected) + 1);

			} else if (e.getSource() == menuItemDelete) {// 删除树节点
				DefaultMutableTreeNode lastSelected = ((DefaultMutableTreeNode) this.adaptee
						.getLastSelectedPathComponent());
				lastSelected.removeFromParent();
			} else if (e.getSource() == menuItemSavePbData) {// 删除树节点
				int returnVal = fc.showSaveDialog(frameMain);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					byte[] data = fetchPbDataFromTree(adaptee);
					ResourceUtils.saveFileBeforeDeleteOld(file, data);
				}
			}
			this.adaptee.updateUI();

		}
	}

	/**
	 * 树路径的一些附加信息。
	 * 
	 * @author jdzhan,2015年7月5日
	 *
	 */
	class TreePathInfo {
		private String path;
		private String fieldName;
		private Class<?> fieldType;
		private Class<?> elementType;
		private Class<?> objectType;

		public TreePathInfo(String path, String fieldName, Class<?> fieldType,
				Class<?> objectType) {
			this.path = path;
			this.fieldName = fieldName;
			this.fieldType = fieldType;
			this.objectType = objectType;
		}

		public Class<?> getType() {
			return fieldType;
		}

		public void setType(Class<?> type) {
			this.fieldType = type;
		}

		public boolean isCollection() {
			return ReflectionUtil.isCollection(fieldType);
		}

		public boolean isEnum() {
			return ProtoIDLProxy.isEnumReadable(fieldType);
		}

		public Class<?> getElementType() {
			if (!isCollection())
				throw new RuntimeException(fieldName
						+ "is not a Collection object.");
			return PBUtils.getElementType(fieldName, fieldType, objectType);
		}

		public String getClassName() {
			if (fieldType != null) {
				return fieldType.getSimpleName();
			}
			return "";
		}

		public String getElementClassName() {
			if (elementType != null) {
				return elementType.getSimpleName();
			}
			return "";
		}
	}

	/**
	 * 树节点保存的用户数据。
	 * 
	 * @author jdzhan,2015年7月5日
	 *
	 */
	class UserData {
		private String name;
		private String path;
		private Object value;

		public UserData(String name, String path) {
			this.name = name;
			this.path = path;
			setDefault();
		}

		/**
		 * TODO：设置默认值,required字段问题
		 */
		private void setDefault() {
			Class<?> type = getFieldType();
			if (type == null) {
				throw new RuntimeException();
			}
			if (ProtoIDLProxy.isEnumReadable(type)) {
				Field[] fields = type.getFields();
				if (fields == null || fields.length == 0) {
					return;
				}
				value = fields[0].getName().toString();
			}
			if (type.isAssignableFrom(String.class)) {// 字符串默认为空串。
				value = "";
			}
		}

		public TreePathInfo getPathInfo() {
			if (StringUtils.isEmpty(path)) {
				return null;
			}
			TreePathInfo pathInfo = treePathInfos.get(path);
			if (pathInfo == null) {
				return null;
			}
			return pathInfo;
		}

		public String getFieldMsgName() {
			Class<?> type = getFieldType();
			if (type == null) {
				throw new RuntimeException();
			}
			String msgName = PBUtils.getMsgName(type);
			return msgName;
		}

		public String getPath() {
			return path;
		}

		public boolean isEnum() {
			Class<?> type = getFieldType();
			if (type == null) {
				throw new RuntimeException();
			}
			return ProtoIDLProxy.isEnumReadable(type);
		}

		public Class<?> getFieldType() {
			TreePathInfo pathInfo = getPathInfo();
			if (pathInfo == null) {
				throw new RuntimeException("name=" + name + ",path=" + path);
			}
			Class<?> type = null;
			if (pathInfo.isCollection()) {
				type = pathInfo.getElementType();
			} else {
				type = pathInfo.getType();
			}
			return type;
		}

		public boolean isLeafFieldNode() {
			TreePathInfo pathInfo = getPathInfo();
			if (pathInfo == null) {
				throw new RuntimeException("name=" + name + ",path=" + path);
			}
			Class<?> type = null;
			if (pathInfo.isCollection()) {
				type = pathInfo.getElementType();
			} else {
				type = pathInfo.getType();
			}

			if (type == null) {
				throw new RuntimeException();
			}

			return PBUtils.isLeafType(type);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * 自定义树节点如何展现。
	 * 
	 * @author jdzhan,2015年7月5日
	 *
	 */
	class LeafFieldNodeCellRenderer implements TreeCellRenderer {
		JLabel lblName, lblType, lblPath;
		JTextField tfDeafaultValue;
		JComboBox cbxEnumValue;
		JPanel renderer;
		DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
		Font fontValue;
		int fieldType = -1;// 0 defalut,1 enum

		public LeafFieldNodeCellRenderer() {
			fontValue = UIManager.getFont("Tree.font");
			renderer = new JPanel(new GridBagLayout());
			renderer.setFont(fontValue);

			lblName = new JLabel();
			lblName.setEnabled(true);
			lblName.setFont(fontValue);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			renderer.add(lblName, gbc, 0);

			lblType = new JLabel();
			lblType.setFont(fontValue);
			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			lblType.setForeground(Color.BLUE);
			renderer.add(lblType, gbc, 1);

			lblPath = new JLabel();
			lblPath.setVisible(false);

			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			renderer.add(lblPath, gbc, 2);
		}

		private void initDefaultFieldRenderer() {
			GridBagConstraints gbc = new GridBagConstraints();
			tfDeafaultValue = new JTextField(30);
			tfDeafaultValue.setBorder(null);
			tfDeafaultValue.setFont(fontValue);
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 3;
			gbc.gridy = 0;
			gbc.weightx = 50;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			if (fieldType > -1) {
				renderer.remove(3);
			}
			fieldType = 0;
			renderer.add(tfDeafaultValue, gbc, 3);
		}

		private void initEnumFieldRenderer(Field[] fields) {
			GridBagConstraints gbc = new GridBagConstraints();
			cbxEnumValue = new JComboBox();
			cbxEnumValue.setFont(fontValue);
			cbxEnumValue.setBorder(new EmptyBorder(0, 0, 0, 0));
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 3;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			if (fieldType > -1) {// 说明已经设置
				renderer.remove(3);
			}
			fieldType = 1;// 枚举字段的渲染
			renderer.add(cbxEnumValue, gbc, 3);

			{
				for (Field field : fields) {
					cbxEnumValue.addItem(field.getName());
				}
			}
			UIManager.put(
					"ComboBox.background",
					new ColorUIResource(UIManager
							.getColor("TextField.background")));
			UIManager.put(
					"ComboBox.foreground",
					new ColorUIResource(UIManager
							.getColor("TextField.foreground")));
		}

		public UserData getUserObjectFromEditor() {
			UserData object = new UserData(lblName.getText(), lblPath.getText());
			String value = null;
			if (fieldType == 0) {// 默认
				value = tfDeafaultValue.getText();
			}
			if (fieldType == 1) {// 枚举
				value = cbxEnumValue.getSelectedItem().toString();
			}
			object.setValue(PBUtils.dynamicCast(value, object.getFieldType()));
			return object;
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			Component returnValue = defaultRenderer
					.getTreeCellRendererComponent(tree, value, selected,
							expanded, leaf, row, hasFocus);

			if (!leaf) {
				return returnValue;
			}

			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
				Object userObject = ((DefaultMutableTreeNode) value)
						.getUserObject();

				if (userObject instanceof UserData) {
					UserData uo = (UserData) userObject;
					if (uo.isLeafFieldNode()) {
						lblName.setText(uo.getName());
						lblType.setText(" [" + uo.getFieldMsgName() + "] ");
						lblPath.setText(uo.getPath());
						Object setVal = uo.getValue();
						String setText = "";
						if (setVal != null) {
							if (ReflectionUtil.isByteArray(uo.getFieldType())) {
								setText = com.iflytek.ossp.framework.dt.utils.StringUtils
										.decodeUTF8((byte[]) setVal);
							} else {
								setText = setVal.toString();
							}
						}

						if (uo.isEnum()) {
							Field[] fields = uo.getFieldType().getFields();
							initEnumFieldRenderer(fields);// 可选的设置
							cbxEnumValue.setSelectedItem(setText);// 保存用户选的值
						} else {
							initDefaultFieldRenderer();
							tfDeafaultValue.setText(setText);
						}
						if (selected) {
							// renderer.setBackground(Color.GRAY);
							renderer.setBorder(BorderFactory
									.createLineBorder(Color.GRAY));
						} else {
							renderer.setBackground(UIManager.getColor(treeReq
									.getForeground()));
							renderer.setBorder(null);
						}
						returnValue = renderer;
					}
				}
			}
			return returnValue;
		}
	}

	/**
	 * 树节点编辑器
	 * 
	 * @author jdzhan,2015年7月5日
	 *
	 */
	class LeafFieldNodeEditor extends AbstractCellEditor implements
			TreeCellEditor {
		private static final long serialVersionUID = 6573973293274777046L;
		private LeafFieldNodeCellRenderer renderer = new LeafFieldNodeCellRenderer();
		ChangeEvent changeEvent = null;
		private JTree tree;

		public LeafFieldNodeEditor(JTree tree) {
			this.tree = tree;
		}

		@Override
		public Object getCellEditorValue() {
			UserData userObject = renderer.getUserObjectFromEditor();
			return userObject;
		}

		@Override
		public boolean isCellEditable(EventObject event) {
			boolean returnValue = false;
			if (event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) event;
				TreePath path = tree.getPathForLocation(mouseEvent.getX(),
						mouseEvent.getY());
				if (path != null) {
					TreePathInfo pathInfo = treePathInfos.get(path.toString());
					Object node = path.getLastPathComponent();
					if ((node != null)
							&& (node instanceof DefaultMutableTreeNode)) {
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
						Object userObject = treeNode.getUserObject();

						returnValue = ((treeNode.isLeaf())
								&& (userObject instanceof UserData) && ((UserData) userObject)
								.isLeafFieldNode());
					}
				}
			}
			return returnValue;
		}

		@Override
		public Component getTreeCellEditorComponent(JTree tree, Object value,
				boolean isSelected, boolean expanded, boolean leaf, int row) {
			Component editor = renderer.getTreeCellRendererComponent(tree,
					value, true, expanded, leaf, row, true);

			KeyListener keyListener = new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {// textField按下Enter键的时候保存数据（UserObject）
						if (stopCellEditing()) {
							fireEditingStopped();
						}
					}
				}
			};

			MouseListener mouseListener = new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (stopCellEditing()) {
						fireEditingStopped();
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}
			};
			ItemListener itemListener = new ItemListener() {
				public void itemStateChanged(ItemEvent itemEvent) {
					if (stopCellEditing()) {
						fireEditingStopped();
					}
				}
			};
			if (editor instanceof JPanel) {
				Object tmp = ((JPanel) editor).getComponent(3);
				if (tmp instanceof JTextField) {
					JTextField textField = (JTextField) tmp;
					textField.addKeyListener(keyListener);
					textField.addMouseListener(mouseListener);
				}
				if (tmp instanceof JComboBox) {
					JComboBox comboBox = (JComboBox) tmp;
					comboBox.addItemListener(itemListener);
				}

			}
			return editor;
		}
	}

	private void removeTableSelectedRows(JTable table) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int numRows = table.getSelectedRows().length;
		for (int i = 0; i < numRows; i++) {
			model.removeRow(table.getSelectedRow());
		}
	}

	class TableDialog extends JDialog {
		private JTable tblHttpHeader;
		private JButton btnAddHeader, btnDeleteHeader;

		public TableDialog(JFrame frame, String title, boolean modal) {
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

		public Map<String, String> getHeaders() {
			Map<String, String> map = new HashMap<String, String>();
			DefaultTableModel model = (DefaultTableModel) tblHttpHeader
					.getModel();

			int rc = model.getRowCount();
			for (int i = 0; i < rc; i++) {
				Object key = model.getValueAt(i, 0);
				Object val = model.getValueAt(i, 1);
				if (key != null && val != null) {
					map.put(key.toString(), val.toString());
				}
			}

			return map;
		}

		public void setHeaders(Map<String, String> headers) {
			tblHttpHeader.setModel(new DefaultTableModel(new Object[][] {},
					new String[] { "键", "值" }));
			DefaultTableModel model = (DefaultTableModel) tblHttpHeader
					.getModel();
			for (Entry<String, String> entry : headers.entrySet()) {
				model.addRow(new Object[] { entry.getKey(), entry.getValue() });
			}
		}

		public void setTableMode(Map<String, String> model) {

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
	}
}
