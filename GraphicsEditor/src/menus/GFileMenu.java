package menus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.GConstants;
import constants.GConstants.EFileMenuItem;
import frames.GDrawingPanel;
import shapes.GShape;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	// association
	private GDrawingPanel drawingPanel;
	private File file;
	private boolean newFile;

	public GFileMenu() {
		super(GConstants.FILEMENU_TITLE);
		this.file = null;
		this.newFile = true;
		ActionHandler actionHandler = new ActionHandler();
		for(EFileMenuItem eMenuItem: EFileMenuItem.values()){
			JMenuItem menuItem = new JMenuItem(eMenuItem.getText());
			this.add(menuItem);
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eMenuItem.name());
			if(eMenuItem.getText().equals(EFileMenuItem.open.getText()) 
					|| eMenuItem.getText().equals(EFileMenuItem.saveAs.getText())){
				this.addSeparator();
			}
		}
	}
	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	@SuppressWarnings("unchecked")
	public void inputStream(){
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(file)));
			drawingPanel.setShapeVector((Vector<GShape>)inputStream.readObject());
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	public void outputStream(){
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(file)));
			outputStream.writeObject(drawingPanel.getShapeVector());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// new
	public void newCanvas() {
		int confirm = -1;
		if(drawingPanel.getbDraw()){
			confirm = JOptionPane.showConfirmDialog(null, "변경내용을 저장하시겠습니까?");
		} 
		if(confirm == JOptionPane.OK_OPTION){
			save();
		}
		this.newFile = true;
		drawingPanel.newCanvas();
	}
	// open
	private File showOpenDialog() {
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Graphics Editor", "gps");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}
	private void open(){
		int confirm = -1;
		if(drawingPanel.getbDraw()){
			confirm = JOptionPane.showConfirmDialog(null, "변경내용을 저장하시겠습니까?");
		} 
		if(confirm == JOptionPane.OK_OPTION){ save(); } 
		else if(confirm == JOptionPane.CANCEL_OPTION){ return; }
		file = showOpenDialog();
		if (file == null) {
			return;
		} else {
			inputStream();		
		}
		this.newFile = false;
		drawingPanel.setbDraw(false);
	}
	// save & saveAs
	private String showSaveDialog() {
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Graphics Editor", "gps");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getName();
		}
		return null;
	}
	private void save() {
		if(!newFile){
			outputStream();
			drawingPanel.setbDraw(false);
		} else{
			saveAs();
		}
	}
	public void saveAs() {
		String fileName = showSaveDialog();
		String extension = ".gps";
		if (fileName == null) {
			return;
		} else if (fileName != null) {
			if (fileName.contains(extension)) {
				file = new File(fileName);
			} else {
				file = new File(fileName + extension);
			}
			outputStream();
		}
		this.newFile = false;
		drawingPanel.setbDraw(false);
	}

	// print(아직 덜 구현)
	public void print() {
		PrinterJob print = PrinterJob.getPrinterJob();
		if (!print.printDialog())
			return;
		try { print.print(); } 
		catch (PrinterException pe) { System.out.println("프린터 에러 " + pe.getMessage()); }
	}
	// close
	public void close() {
		int confirm = -1;
		if(drawingPanel.getbDraw()){
			confirm = JOptionPane.showConfirmDialog(null, "변경내용을 저장하시겠습니까?");
		} 
		if(confirm == JOptionPane.OK_OPTION){
			save();
		}
		drawingPanel.newCanvas();
	}
	// exit
	public void exit() {
		int confirm = -1;
		if(drawingPanel.getbDraw()){
			confirm = JOptionPane.showConfirmDialog(null, "변경내용을 저장하시겠습니까?");
		} 
		if(confirm == JOptionPane.OK_OPTION){ save(); } 
		else if(confirm == JOptionPane.CANCEL_OPTION){ return; }
		System.exit(1);
	}
	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals(EFileMenuItem.open.name())){
				open();
			} else if (e.getActionCommand().equals(EFileMenuItem.newItem.name())) {
				newCanvas();
			} else if (e.getActionCommand().equals(EFileMenuItem.save.name())) {
				save();
			} else if (e.getActionCommand().equals(EFileMenuItem.saveAs.name())) {
				saveAs();
			} else if (e.getActionCommand().equals(EFileMenuItem.print.name())) {
				print();
			} else if (e.getActionCommand().equals(EFileMenuItem.close.name())) {
				close();
			} else if (e.getActionCommand().equals(EFileMenuItem.exit.name())) {
				exit();
			}
		}
	}

}
