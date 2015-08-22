package swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class BoxLayoutTest {

	static JPanel topPanel;
	static JPanel bottomPanel;
	static JPanel middlePanel;

	static void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		JButton btn1 = new JButton("btn1");
		topPanel.add(btn1);
		JButton btn2 = new JButton("btn2");
		topPanel.add(btn2);
	}

	static void createMiddlePanel() {
		middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		
		JLabel sourceLabel = new JLabel("运动会项目：");
		sourceLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		sourceLabel.setBorder(BorderFactory.createEmptyBorder(4, 5, 0, 5));
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("100米");
		listModel.addElement("200米");
		listModel.addElement("400米");
		listModel.addElement("跳远");
		listModel.addElement("跳高");
		listModel.addElement("铅球");
		JList sourceList = new JList(listModel);
		sourceList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		sourceList.setVisibleRowCount(5);
		JScrollPane sourceListScroller = new JScrollPane(sourceList);
		sourceListScroller.setPreferredSize(new Dimension(120, 80));
		sourceListScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sourceListScroller.setAlignmentY(Component.TOP_ALIGNMENT);
		JPanel sourceListPanel = new JPanel();
		sourceListPanel.setLayout(new BoxLayout(sourceListPanel,
				BoxLayout.X_AXIS));
		sourceListPanel.add(sourceLabel);
		sourceListPanel.add(sourceListScroller);
		sourceListPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		sourceListPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
		middlePanel.add(sourceListPanel);
		
		JButton toTargetButton = new JButton(">>");
		JButton toSourceButton = new JButton("<<");
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(toTargetButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(15, 15)));
		buttonPanel.add(toSourceButton);
		buttonPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));
		middlePanel.add(buttonPanel);

		JLabel targetLabel = new JLabel("查询项目：");
		targetLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		targetLabel.setBorder(BorderFactory.createEmptyBorder(4, 5, 0, 5));
		DefaultListModel targetListModel = new DefaultListModel();
		targetListModel.addElement("100米");
		JList targetList = new JList(targetListModel);
		targetList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		targetList.setVisibleRowCount(5);
		JScrollPane targetListScroller = new JScrollPane(targetList);
		targetListScroller.setPreferredSize(new Dimension(120, 80));
		targetListScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		targetListScroller.setAlignmentY(Component.TOP_ALIGNMENT);
		JPanel targetListPanel = new JPanel();
		targetListPanel.setLayout(new BoxLayout(targetListPanel,
				BoxLayout.X_AXIS));
		targetListPanel.add(targetLabel);
		targetListPanel.add(targetListScroller);
		targetListPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		targetListPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		middlePanel.add(targetListPanel);
	}

	static void createBottomPanel() {
		JButton actionButton = new JButton("查询");
		JButton closeButton = new JButton("退出");
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(actionButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(closeButton);
		bottomPanel.add(Box.createVerticalStrut(10));
		bottomPanel.add(buttonPanel);
		bottomPanel.add(Box.createVerticalStrut(10));
	}

	public static void main(String[] args) {
		createTopPanel();
		createMiddlePanel();
		createBottomPanel();

		JPanel panelContainer = new JPanel();
		panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));

		panelContainer.add(topPanel, null);
		panelContainer.add(middlePanel, null);
		panelContainer.add(bottomPanel, null);

		JFrame frame = new JFrame("Boxlayout演示");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelContainer.setOpaque(true);
		frame.setSize(new Dimension(480, 320));
		frame.setContentPane(panelContainer);
		frame.setVisible(true);
	}
}
