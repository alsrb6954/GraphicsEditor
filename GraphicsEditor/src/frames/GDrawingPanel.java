package frames;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import constants.GConstants;
import constants.GConstants.EAnchors;
import constants.GConstants.EDrawingType;
import shapes.GGroup;
import shapes.GSelection;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GTransformer;


public class GDrawingPanel extends JPanel {
	// attributes
	private static final long serialVersionUID = 1L;
	private boolean bDraw, bClick;
	public boolean getbDraw() { return bDraw; }
	public void setbDraw(boolean bDraw) { this.bDraw = bDraw; }
	private Color fillColor, lineColor;
	private BasicStroke lineStyle;
	// object states
	private static enum EState {idle, drawingTP, drawingNP, transforming}; 
	private EState eState;
	// components
	private Stack<Vector<GShape>> redo;
	private Stack<Vector<GShape>> undo;
	private Vector<GShape> shapeVector;
	private Vector<GShape> groupVector;
	public Vector<GShape> getShapeVector() { return shapeVector; }
	public void setShapeVector(Vector<GShape> shapeVector) {
		this.shapeVector = shapeVector;
		repaint();
	}
	private MousEventHandler mousEventHandler;
	// associative attributes
	private GShape selectedShape;
	public void setSelectedShape( GShape selectedShape){
		this.selectedShape = selectedShape;
	}
	// working objects;
	private GShape currentShape;
	private GTransformer currentTransformer;
	
	public GDrawingPanel() {
		super();
		//attributes
		this.bDraw = false;
		this.bClick = false;
		eState = EState.idle;
		this.fillColor = GConstants.COLOR_FILL_DEFAULT;
		this.lineColor = GConstants.COLOR_LINE_DEFAULT;
		//components
		this.redo = new Stack<Vector<GShape>>();
		this.undo = new Stack<Vector<GShape>>();
		this.shapeVector = new Vector<GShape>();
		this.groupVector = new Vector<GShape>();
		this.mousEventHandler = new MousEventHandler();
		this.addMouseListener(mousEventHandler);
		this.addMouseMotionListener(mousEventHandler);
		// working variables
		this.selectedShape = null;
		this.currentShape = null;
		this.currentTransformer = null;
	}
	public void initialize() {
	}	
	
	public void paint(Graphics g) {
		super.paint(g);
		for(GShape shape: this.shapeVector){
			shape.draw((Graphics2D)g);
		}
	}
	public void newCanvas(){
		this.shapeVector.removeAllElements();
		this.repaint();
		this.bDraw = false;
	}

	public void resetSelected(){
		for(GShape shape: this.shapeVector){
			shape.setSelected(false);
		}
		this.repaint();
	}
	private void initTransforming(int x, int y){
		if(this.currentShape == null) {
			this.currentShape = this.selectedShape.clone();
			this.currentShape.setFillColor(fillColor);
			this.currentShape.setLineColor(lineColor);
			this.currentShape.setLineStyle(lineStyle);
			this.currentTransformer = new GDrawer(this.currentShape);
		} else if (this.currentShape.getCurrentEAnchor() == EAnchors.MM){
			this.currentTransformer = new GMover(this.currentShape);
		} else if (this.currentShape.getCurrentEAnchor() == EAnchors.RR){	
			this.currentTransformer = new GRotator(this.currentShape);
		} else{
			this.currentTransformer = new GResizer(this.currentShape);
		}
		Graphics2D g2D = (Graphics2D)this.getGraphics();
		g2D.setXORMode(this.getBackground());
		this.currentTransformer.setGroupVector(groupVector);
		this.currentTransformer.initTransforming(x,y,g2D);
	}
	private void keepTransforming(int x, int y){
		Graphics2D g2D = (Graphics2D)this.getGraphics();
		g2D.setXORMode(this.getBackground());
		this.currentTransformer.keepTransforming(x,y,g2D);
	}
	private void continueTransforming(int x, int y){
		Graphics2D g2D = (Graphics2D)this.getGraphics();
		g2D.setXORMode(this.getBackground());
		this.currentTransformer.continueTransforming(x,y,g2D);
	}
	@SuppressWarnings("unchecked")
	private void finishTransforming(int x, int y){
		Graphics2D g2D = (Graphics2D)this.getGraphics();
		g2D.setXORMode(this.getBackground());
		if(!(currentTransformer.getShape() instanceof GSelection)){
			this.currentTransformer.finishTransforming(x,y,g2D);
			if(this.currentTransformer.getClass().getSimpleName().equals("GDrawer")){
				this.undo.add((Vector<GShape>)shapeVector.clone());
				this.shapeVector.addElement(this.currentShape);
			} else{
				this.undo.add((Vector<GShape>)shapeVector.clone());
			}
			this.redo.clear();
		}else {
			((GSelection)currentTransformer.getShape()).init(shapeVector);
			((GSelection)currentTransformer.getShape()).selectShapes();
		}
		this.currentShape.setSelected(true);
		this.bDraw = true;
		this.repaint();
		
	}
	@SuppressWarnings("unchecked")
	public Vector<GShape> cut() {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		Vector<GShape> selectShape = new Vector<GShape>();
		for (int i = this.shapeVector.size(); i > 0; i--) {
			GShape shape = shapeVector.get(i - 1);
			if (shape.isSelected()) {
				selectShape.add(shape);
				this.shapeVector.remove(shape);
			}
		}
		repaint();
		return selectShape;
	}

	public Vector<GShape> copy() {
		Vector<GShape> selectShape = new Vector<GShape>();
		for (GShape shape: this.shapeVector) {
			if (shape.isSelected()) {
				selectShape.add(shape.deepCopy());
			}
		}
		return selectShape;
	}
	@SuppressWarnings("unchecked")
	public void paste(Vector<GShape> shapes) {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		for(GShape shape: shapes){
			this.shapeVector.add(shape);
		}
		repaint();
	}
	@SuppressWarnings("unchecked")
	public void delete() {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		for(int i = this.shapeVector.size(); i > 0 ; i--){
			GShape shape = this.shapeVector.get(i-1);
			if(shape.isSelected()){
				this.shapeVector.remove(shape);
			}
			repaint();
		}
	}
	@SuppressWarnings("unchecked")
	public void undo() {
		if(undo.size() != 0){
			this.redo.add((Vector<GShape>) shapeVector.clone());
			this.shapeVector = this.undo.pop();
			repaint();
		}
	}
	@SuppressWarnings("unchecked")
	public void redo() {
		if(redo.size() != 0){
			this.undo.add((Vector<GShape>) shapeVector.clone());
			this.shapeVector = this.redo.pop();
			repaint();
		}
	}
	@SuppressWarnings("unchecked")
	public void group(GGroup group) {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		boolean check = false;
    	for(int i = shapeVector.size(); i > 0; i--) {
    		GShape shape = shapeVector.get(i - 1);
    		if(shape.isSelected()) {
    			shape.setSelected(false);	
    			if(shape instanceof GGroup){
    				for(GShape groupShape: groupVector){
    					group.addShape(groupShape);
    				}
        			shapeVector.remove(shape);
    			}else{
        			group.addShape(shape);
        			shapeVector.remove(shape);
    			}
    			check = true;
    		}
    	}
    	if(check) {
    		group.setSelected(true);
    		shapeVector.add(group);
    	}
    	this.groupVector = group.getShapeVector();
    	repaint();
	}
	@SuppressWarnings("unchecked")
	public void unGroup() {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		Vector<GShape>ungroupVector = new Vector<>();
    	for(int i = shapeVector.size(); i > 0; i--) {
    		GShape shape = shapeVector.get(i - 1); 
    		if(shape instanceof GGroup && shape.isSelected()) {
    			for(GShape childShape : this.groupVector) {
    				childShape.setSelected(true);
    				ungroupVector.add(childShape);
    			}
    			shapeVector.remove(shape);
    		}
    	}
    	shapeVector.addAll(ungroupVector);
		repaint();
	}
	@SuppressWarnings("unchecked")
	public void selectAll() {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		for (GShape shape : shapeVector) {
			shape.setSelected(true);	
		}
		repaint();
	}

	private GShape onShape(int x, int y){
		for(GShape shape: this.shapeVector){
			EAnchors eAnchors = shape.contains(x, y);
			if(eAnchors != null)
				return shape;
		}
		return null;
	}
	private void changeCursor(GShape shape){
		if(shape == null){
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return;
		}
		this.setCursor(shape.getCurrentEAnchor().getCursor());
	}

	@SuppressWarnings("unchecked")
	public void setLineColor(Color lineColor) {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		int i = 0;
		for(GShape shape : shapeVector){
			if(shape.isSelected()){
				if(shape instanceof GGroup){
					for(GShape childShape : groupVector){
						childShape.setLineColor(lineColor);
					}
				} else{
					shape.setLineColor(lineColor);
				}
				i = 1;
			}
			repaint();
		}
		if(i == 0){
			this.lineColor = lineColor;
		}
	}
	@SuppressWarnings("unchecked")
	public void setFillColor(Color fillColor) {
		this.undo.add((Vector<GShape>)shapeVector.clone());
		int i = 0;
		for(GShape shape : shapeVector){
			if(shape.isSelected()){
				if(shape instanceof GGroup){
					for(GShape childShape : groupVector){
						childShape.setFillColor(fillColor);
					}
				} else{
					shape.setFillColor(fillColor);
				}
				i = 1;
			}
			repaint();
		}
		if(i == 0){
			this.fillColor = fillColor;
		}
	}

	@SuppressWarnings("unchecked")
	public void lineStyle(BasicStroke lineStyle) {
		this.undo.add((Vector<GShape>) shapeVector.clone());
		for (GShape shape : shapeVector) {
			if (shape.isSelected()) {
				shape.setStyle(1);
				if (shape instanceof GGroup) {
					for (GShape childShape : groupVector) {
						childShape.setLineStyle(lineStyle);
					}
				} else {
					shape.setLineStyle(lineStyle);
				}
			}
			repaint();
		}
	}

	class MousEventHandler implements MouseMotionListener, MouseInputListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 1){
				mouse1Clicked(e);
			} else if (e.getClickCount() == 2){
				mouse2Clicked(e);
			}
		}
		private void mouse1Clicked(MouseEvent e){
			if(eState == EState.idle){
				currentShape = onShape(e.getX(), e.getY());
				if(currentShape == null){
					if(selectedShape.geteDrawingType() == EDrawingType.NP){
						initTransforming(e.getX(), e.getY());
						eState = EState.drawingNP;
					}
				}
			} else if(eState == EState.drawingNP){
				continueTransforming(e.getX(),e.getY());
			}
		}
		private void mouse2Clicked(MouseEvent e){
			if(eState == EState.drawingNP){
				finishTransforming(e.getX(), e.getY());
				eState = EState.idle;
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			bClick =true;
			if(eState == EState.idle){
				currentShape = onShape(e.getX(), e.getY());
				if(currentShape == null){
					resetSelected();
					if(selectedShape.geteDrawingType() == EDrawingType.TP){
						initTransforming(e.getX(), e.getY());
						eState = EState.drawingTP;
					}
				}else {
					if(e.isShiftDown()){
						initTransforming(e.getX(), e.getY());
						eState = EState.transforming;
					}else{
						resetSelected();
						initTransforming(e.getX(), e.getY());
						eState = EState.transforming;
					}
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(eState == EState.drawingTP){
				if(!bClick){
					finishTransforming(e.getX(), e.getY());
				}
				eState = EState.idle;
			} else if (eState == EState.transforming){
				finishTransforming(e.getX(), e.getY());
				eState = EState.idle;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			bClick = false;
			if(eState == EState.drawingTP){
				keepTransforming(e.getX(), e.getY());
			} else if (eState == EState.transforming){
				keepTransforming(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(eState == EState.drawingNP){
				keepTransforming(e.getX(), e.getY());
			} else if(eState == EState.idle){
				GShape shape = onShape(e.getX(), e.getY());
				changeCursor(shape);
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
