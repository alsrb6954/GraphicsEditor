package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.GConstants;
import constants.GConstants.EFileMenuItem;
import frames.GDrawingPanel;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	// association
	private GDrawingPanel drawingPanel;

	public GFileMenu() {
		super(GConstants.FILEMENU_TITLE);	
		ActionHandler actionHandler = new ActionHandler();
		for(EFileMenuItem eMenuItem: EFileMenuItem.values()){
			JMenuItem menuItem = new JMenuItem(eMenuItem.getText());
			this.add(menuItem);
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eMenuItem.name());
			if(eMenuItem.getText().equals(EFileMenuItem.close.getText()) 
					|| eMenuItem.getText().equals(EFileMenuItem.saveAs.getText())){
				this.addSeparator();
			}
		}
	}
	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	private void open(){
		
	}
	private String showDialog() {
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Graphics Editor", "gps");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getName();
	    }
	    return null;
	}
	private void save(){
		String fileName = showDialog();
		if(fileName == null){
			return;
		}
		File file = new File(fileName);
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(file)));
			outputStream.writeObject(drawingPanel.getShapeVector());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(EFileMenuItem.open.name())){
				open();
			} else if(e.getActionCommand().equals(EFileMenuItem.save.name())){
				save();
			}
		}
		
	}
}
