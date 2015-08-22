package swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * swing基础实例
 * 
 * @author HZ20232
 *
 */
public class GUIMain {
	public static void main(String args[]) throws Exception {
		MainFrame frame1 = new MainFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 一定要设置关闭

		frame1.setVisible(true);
	}
}

class MainFrame extends JFrame {
	private JLabel addrLabel;
	private JButton reqButton;
	private JComboBox addrBox;
	private JMenuBar menuBar;
	private JPanel topPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel bottomPanel;

	public MainFrame() {
		super();
		initGUI();

	}

	private void initGUI() {
	
		this.setSize(800, 500);
		this.setJMenuBar(getMenu());
		this.setTitle("Protobuf测试工具v1.0");

		GridLayout layout = new GridLayout(3,3);
		topPanel = new JPanel();
		topPanel.setLayout(layout);
		topPanel.add(getComboBox());
		topPanel.add(getLabel(), null);
		topPanel.add(getButton(), null);
		
		
	
		this.getContentPane().add(topPanel);
		
	}

	private JLabel getLabel() {
		if (addrLabel == null) {
			addrLabel = new JLabel();
			addrLabel.setBounds(10, 30, 63, 18);
			addrLabel.setText("请求地址:");
		}
		return addrLabel;
	}

	private JComboBox getComboBox() {
		if (addrBox == null) {
			addrBox = new JComboBox();
			addrBox.setEditable(true);
			addrBox.setBounds(73, 30, 560, 20);
		}
		return addrBox;
	}

	private JButton getButton() {
		if (reqButton == null) {
			reqButton = new JButton();
			reqButton.setText("请求");
			reqButton.addActionListener(new ReqButtonActionListener());
			reqButton.setBounds(643, 30, 60, 20);
		}
		return reqButton;
	}

	private class ReqButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("点击请求按钮");
		}
	}

	private JMenuBar getMenu() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			JMenu m1 = new JMenu();
			m1.setText("文件");

			JMenuItem item11 = new JMenuItem();
			item11.setText("打开");
			JMenuItem item12 = new JMenuItem();
			item12.setText("保存");
			JMenuItem item13 = new JMenuItem();
			item13.setText("退出");
			item13.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			m1.add(item11);
			m1.add(item12);
			m1.add(item13);

			menuBar.add(m1);
		}
		return menuBar;
	}
}