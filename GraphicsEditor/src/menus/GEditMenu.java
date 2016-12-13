package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GConstants;
import constants.GConstants.EEditMenuItem;
import frames.GDrawingPanel;
import shapes.GGroup;
import shapes.GShape;

public class GEditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	// association
	private GDrawingPanel drawingPanel;
	private Vector<GShape> copyVector;
	public GEditMenu() {
		super(GConstants.EDITMENU_TITLE);
		copyVector = new Vector<GShape>();
		
		ActionHandler actionHandler = new ActionHandler();
		for(EEditMenuItem eMenuItem: EEditMenuItem.values()){
			JMenuItem menuItem = new JMenuItem(eMenuItem.getText());
			this.add(menuItem);
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eMenuItem.name());
			if (eMenuItem.getText().equals(EEditMenuItem.delete.getText())
					|| eMenuItem.getText().equals(EEditMenuItem.undo.getText())
					|| eMenuItem.getText().equals(EEditMenuItem.unGroup.getText())) {
				this.addSeparator();
			}
		}	
	}
	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	public void cut() {
		this.copyVector.clear();
		this.copyVector.addAll(this.drawingPanel.cut());
	}
	public void copy() {
		this.copyVector.clear();
		this.copyVector.addAll(this.drawingPanel.copy());
	}
	public void paste() {
		this.drawingPanel.paste(this.copyVector);
	}
	public void delete() {
		this.drawingPanel.delete();
	}
	public void undo() {
		this.drawingPanel.undo();
	}
	public void redo() {
		this.drawingPanel.redo();
	}	
	public void group() {
		drawingPanel.group(new GGroup());
	}
	public void unGroup() {
		drawingPanel.unGroup();
	}
	public void selectCancle() {
		this.drawingPanel.resetSelected();
	}
	public void selectAll() {
		this.drawingPanel.selectAll();
	}
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			switch(EEditMenuItem.valueOf(e.getActionCommand())){
			case cut: cut(); break;
			case copy: copy(); break;
			case paste: paste(); break;
			case delete: delete(); break;
			case redo: redo(); break;
			case undo: undo(); break;
			case group: group(); break;
			case unGroup: unGroup(); break;
			case SelectCancle: selectCancle(); break;
			case SelectAll: selectAll(); break;
			}
		}
	}
}
