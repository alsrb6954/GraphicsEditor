package frames;
import javax.swing.JMenuBar;

import menus.GColorMenu;
import menus.GEditMenu;
import menus.GFileMenu;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	// components
	private GFileMenu fileMenu;
	private GEditMenu editMenu;
	private GColorMenu colorMenu;
	// association
	private GDrawingPanel drawingPanel;
	GMenuBar() {
		fileMenu = new GFileMenu();
		this.add(fileMenu);
		editMenu = new GEditMenu();
		this.add(editMenu);
		colorMenu = new GColorMenu();
		this.add(colorMenu);
	}

	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		this.fileMenu.initialize(this.drawingPanel);
		this.editMenu.initialize(this.drawingPanel);
		this.colorMenu.initialize(this.drawingPanel);
	}
}
