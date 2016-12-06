package frames;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import constants.GConstants;
import constants.GConstants.EAnchors;
import constants.GConstants.EDrawingType;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GTransformer;


public class GDrawingPanel extends JPanel {
	// attributes
	private static final long serialVersionUID = 1L;
	private boolean bDraw;
	public boolean getbDraw() { return bDraw; }
	public void setbDraw(boolean bDraw) { this.bDraw = bDraw; }
	private Color fillColor, lineColor;
	// object states
	private static enum EState {idle, drawingTP, drawingNP, transforming}; 
	private EState eState;
	// components
	private Vector<GShape> shapeVector;
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
		eState = EState.idle;
		this.fillColor = GConstants.COLOR_FILL_DEFAULT;
		this.lineColor = GConstants.COLOR_LINE_DEFAULT;
		//components
		this.shapeVector = new Vector<GShape>();
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

	private void resetSelected(){
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
			this.currentTransformer = new GDrawer(this.currentShape);
		} else if (this.currentShape.getCurrentEAnchor() == EAnchors.MM){
			this.currentTransformer = new GMover(this.currentShape);
		} else if (this.currentShape.getCurrentEAnchor() == EAnchors.RR){	
			this.currentTransformer = new GRotator(this.currentShape);
		} else{
			this.currentTransformer = new GResizer(this.currentShape);
		}
		this.resetSelected();
		Graphics2D g2D = (Graphics2D)this.getGraphics();
		g2D.setXORMode(this.getBackground());
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
	private void finishTransforming(int x, int y){
		Graphics2D g2D = (Graphics2D)this.getGraphics();
		g2D.setXORMode(this.getBackground());
		this.currentTransformer.finishTransforming(x,y,g2D);	

		if(this.currentTransformer.getClass().getSimpleName().equals("GDrawer")){
			this.shapeVector.addElement(this.currentShape);
		}
		this.bDraw = true;
		this.currentShape.setSelected(true);
		this.repaint();
		
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

	public void setLineColor(Color lineColor) {
		if (currentShape != null) {
			currentShape.setLineColor(lineColor);
			repaint();
		} else {
			this.lineColor = lineColor;
		}
	}
	public void setFillColor(Color fillColor) {
		if (currentShape != null) {
			currentShape.setFillColor(fillColor);
			repaint();
		} else {
			this.fillColor = fillColor;
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
			if(eState == EState.idle){
				currentShape = onShape(e.getX(), e.getY());
				if(currentShape == null){
					if(selectedShape.geteDrawingType() == EDrawingType.TP){
						initTransforming(e.getX(), e.getY());
						eState = EState.drawingTP;
					}
				}else {
					initTransforming(e.getX(), e.getY());
					eState = EState.transforming;
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(eState == EState.drawingTP){
				finishTransforming(e.getX(), e.getY());
				eState = EState.idle;
			} else if (eState == EState.transforming){
				finishTransforming(e.getX(), e.getY());
				eState = EState.idle;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
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
