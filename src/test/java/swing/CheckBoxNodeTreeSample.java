package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

class TreeNodeVector<E> extends Vector<E> {
	String name;

	TreeNodeVector(String name) {
		this.name = name;
	}

	TreeNodeVector(String name, E elements[]) {
		this.name = name;
		for (int i = 0, n = elements.length; i < n; i++) {
			add(elements[i]);
		}
	}

	public String toString() {
		return "[" + name + "]";
	}
}

class LeafCellEditor extends DefaultTreeCellEditor {

	public LeafCellEditor(JTree tree, DefaultTreeCellRenderer renderer,
			TreeCellEditor editor) {
		super(tree, renderer, editor);
	}

	public boolean isCellEditable(EventObject event) {
		boolean returnValue = super.isCellEditable(event);
		if (returnValue) {
			Object node = tree.getLastSelectedPathComponent();
			if ((node != null) && (node instanceof TreeNode)) {
				TreeNode treeNode = (TreeNode) node;
				returnValue = treeNode.isLeaf();
			}
		}
		return returnValue;
	}
}

class CheckBoxNode {
	String text;

	boolean selected;

	public CheckBoxNode(String text, boolean selected) {
		this.text = text;
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean newValue) {
		selected = newValue;
	}

	public String getText() {
		return text;
	}

	public void setText(String newValue) {
		text = newValue;
	}

	public String toString() {
		return getClass().getName() + "[" + text + "/" + selected + "]";
	}
}

class CheckBoxNodeRenderer implements TreeCellRenderer {
	private JCheckBox leafRenderer = new JCheckBox();

	private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

	protected JCheckBox getLeafRenderer() {
		return leafRenderer;
	}

	public CheckBoxNodeRenderer() {
		Font fontValue;
		fontValue = UIManager.getFont("Tree.font");
		if (fontValue != null) {
			leafRenderer.setFont(fontValue);
		}
		Boolean booleanValue = (Boolean) UIManager
				.get("Tree.drawsFocusBorderAroundIcon");
		leafRenderer.setFocusPainted((booleanValue != null)
				&& (booleanValue.booleanValue()));
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		Component returnValue;
		if (leaf) {
			String stringValue = tree.convertValueToText(value, selected,
					expanded, leaf, row, false);
			leafRenderer.setText(stringValue);
			leafRenderer.setSelected(false);
			leafRenderer.setEnabled(tree.isEnabled());
			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
				Object userObject = ((DefaultMutableTreeNode) value)
						.getUserObject();
				if (userObject instanceof CheckBoxNode) {
					CheckBoxNode node = (CheckBoxNode) userObject;
					leafRenderer.setText(node.getText());
					leafRenderer.setSelected(node.isSelected());
				}
			}
			returnValue = leafRenderer;
		} else {
			returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,
					value, selected, expanded, leaf, row, hasFocus);
		}
		return returnValue;
	}
}

class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {
	CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();

	ChangeEvent changeEvent = null;

	JTree tree;

	public CheckBoxNodeEditor(JTree tree) {
		this.tree = tree;
	}

	public Object getCellEditorValue() {
		JCheckBox checkbox = renderer.getLeafRenderer();
		CheckBoxNode checkBoxNode = new CheckBoxNode(checkbox.getText(),
				checkbox.isSelected());
		return checkBoxNode;
	}

	public boolean isCellEditable(EventObject event) {
		boolean returnValue = false;
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = tree.getPathForLocation(mouseEvent.getX(),
					mouseEvent.getY());
			if (path != null) {
				Object node = path.getLastPathComponent();
				if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
					Object userObject = treeNode.getUserObject();
					returnValue = ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
				}
			}
		}
		return returnValue;
	}

	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row) {

		Component editor = renderer.getTreeCellRendererComponent(tree, value,
				true, expanded, leaf, row, true);

		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (stopCellEditing()) {
					fireEditingStopped();
				}
			}
		};
		if (editor instanceof JCheckBox) {
			((JCheckBox) editor).addItemListener(itemListener);
		}
		return editor;
	}
}

public class CheckBoxNodeTreeSample {
	public static void main(String args[]) {
		JFrame frame = new JFrame("CheckBox Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CheckBoxNode accessibilityOptions[] = { new CheckBoxNode("A", false),
				new CheckBoxNode("B", true) };
		CheckBoxNode browsingOptions[] = { new CheckBoxNode("C", true),
				new CheckBoxNode("D", true), new CheckBoxNode("E", true),
				new CheckBoxNode("F", false) };
		Vector<CheckBoxNode> accessVector = new TreeNodeVector<CheckBoxNode>(
				"G", accessibilityOptions);
		Vector<CheckBoxNode> browseVector = new TreeNodeVector<CheckBoxNode>(
				"H", browsingOptions);
		Object rootNodes[] = { accessVector, browseVector };
		Vector<Object> rootVector = new TreeNodeVector<Object>("Root",
				rootNodes);
		JTree tree = new JTree(rootVector);

		CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
		tree.setCellRenderer(renderer);

		tree.setCellEditor(new CheckBoxNodeEditor(tree));
		tree.setEditable(true);
		JScrollPane scrollPane = new JScrollPane(tree);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(300, 150);
		frame.setVisible(true);
	}
}