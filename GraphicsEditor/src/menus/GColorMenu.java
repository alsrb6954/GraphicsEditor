package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GConstants;
import constants.GConstants.EColorMenuItem;
import frames.GDrawingPanel;

public class GColorMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;
	
	public GColorMenu() {
		super(GConstants.COLORMENU_TITLE);
		for(EColorMenuItem eMenuItem: EColorMenuItem.values()){
			JMenuItem menuItem = new JMenuItem(eMenuItem.getText());
			menuItem.setActionCommand(eMenuItem.name());
			menuItem.addActionListener(new ColorMenuHandler());
			this.add(menuItem);
		}	
	}
	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	public void setLineColor() {
		Color lineColor= JColorChooser.showDialog(null,GConstants.LINECOLOR_TITLE,null);
		if(lineColor != null){
			drawingPanel.setLineColor(lineColor);
		}
	}
	public void clearLineColor() {
		drawingPanel.setLineColor(GConstants.COLOR_LINE_DEFAULT);
	}
	public void setFillColor() {
		Color fillColor= JColorChooser.showDialog(null, GConstants.FILLCOLOR_TITLE,null);
		if(fillColor != null){
			drawingPanel.setFillColor(fillColor);
		}
	}
	public void clearFillColor() {
		drawingPanel.setFillColor(GConstants.COLOR_FILL_DEFAULT);
	}
	
	private class ColorMenuHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			switch(EColorMenuItem.valueOf(e.getActionCommand())){
			case SetLineColor: setLineColor(); break;
			case SetFillColor: setFillColor(); break;
			case ClearLineColor: clearLineColor(); break;
			case ClearFillColor: clearFillColor(); break;
			}
		}
	}
}
