package menus;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GConstants;
import constants.GConstants.EEditMenuItem;
import frames.GDrawingPanel;

public class GEditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	// association
	private GDrawingPanel drawingPanel;
	
	public GEditMenu() {
		super(GConstants.EDITMENU_TITLE);
		for(EEditMenuItem eMenuItem: EEditMenuItem.values()){
			JMenuItem menuItem = new JMenuItem(eMenuItem.getText());
			this.add(menuItem);
			if(eMenuItem.getText().equals(EEditMenuItem.delete.getText()) 
					|| eMenuItem.getText().equals(EEditMenuItem.undo.getText())){
				this.addSeparator();
			}
		}	
	}
	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
}
