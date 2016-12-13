package menus;

import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.GConstants;
import constants.GConstants.ELineMenuItem;
import frames.GDrawingPanel;

public class GLineMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;
	private BasicStroke lineStyle;
	private float lineThickness;
	private int style;
	public GLineMenu(){
		super(GConstants.FILEMENU_TITLE);
		lineStyle = new  BasicStroke();
		this.lineThickness = 0;
		this.style = 0 ;
		
		ActionHandler actionHandler = new ActionHandler();
		for(ELineMenuItem eMenuItem: ELineMenuItem.values()){
			JMenuItem menuItem = new JMenuItem(eMenuItem.getText());
			this.add(menuItem);
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eMenuItem.name());
		}
	}
	public void initialize(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	public void clear() {
		this.lineThickness = 0;
		this.lineStyle = new  BasicStroke();
		this.drawingPanel.lineStyle(lineStyle);
		this.style = 0;
	}
	public void solid() {
		this.lineStyle = new  BasicStroke(this.lineThickness,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		this.drawingPanel.lineStyle(lineStyle);
		this.style = 0;
	}
	public void dotted() {
		float dashes[] = {4};
		this.lineStyle = new  BasicStroke(this.lineThickness,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
		this.drawingPanel.lineStyle(lineStyle);
		this.style = 1;
	}
	public void lineThickness() {
		String[] options = {"OK"};
		JPanel panel = new JPanel();
		JLabel lbl = new JLabel("선 굵기를 입력해주세요 : ");
		JTextField txt = new JTextField(10);
		panel.add(lbl);
		panel.add(txt);
		int selectedOption = JOptionPane.showOptionDialog(null, panel, "lineThickness", JOptionPane.NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		this.lineThickness = Float.parseFloat(txt.getText());
		if(this.style == 0){
			this.lineStyle = new  BasicStroke(this.lineThickness,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);		
		}else if (this.style == 1){
			float dashes[] = {4};
			this.lineStyle = new  BasicStroke(this.lineThickness,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
		}
		if (selectedOption == 0) {
			drawingPanel.lineStyle(lineStyle);
		}
	}
	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			switch(ELineMenuItem.valueOf(e.getActionCommand())){
			case Clear: clear(); break;
			case Solid: solid(); break;
			case Dotted: dotted(); break;
			case Double: break;
			case LineThickness: lineThickness(); break;
			}
		}
	}
}
