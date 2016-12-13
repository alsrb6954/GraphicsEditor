package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import constants.GConstants;
import constants.GConstants.EAnchors;
import constants.GConstants.EDrawingType;


abstract public class GShape implements Serializable{
	private static final long serialVersionUID = 1L;
	// attributes
	private EDrawingType eDrawingType;
	private boolean selected; 
	private EAnchors currentEAnchor;
	protected Color lineColor;
	protected Color fillColor;
	private BasicStroke lineStyle;
	private int style;
	// components
	private Shape shape;
	private Anchors anchors;
	// working variables
	//protected int px, py;
	protected AffineTransform affineTransform;
	// getters & setters
	public EDrawingType geteDrawingType() { return eDrawingType; }
	public boolean isSelected() { return selected; }
	public void setSelected(boolean selected) { this.selected = selected; }
	public EAnchors getCurrentEAnchor() { return currentEAnchor; }
	
	public Shape getShape() { return shape; }
	public void setShape(Shape shape) {this.shape = shape;}
	public Anchors getAnchors() { return anchors; }
//	public void setAnchors(Anchors anchors) { this.anchors = anchors; }
	
	public void setStyle(int style) { this.style = style; }
	public void setLineStyle(BasicStroke lineStyle) { this.lineStyle = lineStyle; }
	public void setLineColor(Color lineColor) { this.lineColor = lineColor; }
	public void setFillColor(Color fillColor) { this.fillColor = fillColor; }
	
	// constructors
	public GShape(EDrawingType eDrawingType, Shape shape){
		this.eDrawingType = eDrawingType;
		this.selected = false;
		this.currentEAnchor = null;
		this.lineColor = GConstants.COLOR_LINE_DEFAULT;
		this.lineStyle = new BasicStroke();
		this.style = 0;
		
		this.shape = shape;
		this.anchors = new Anchors();
		
		//this.px = this.py = 0;
		this.affineTransform = new AffineTransform();
	}
	
	public GShape clone(){
		try { return this.getClass().newInstance();	} 
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	// methods
	public void draw(Graphics2D g2D) {
		if (fillColor != null) {
			g2D.setColor(fillColor);
			g2D.fill(this.shape);
		}
		if (lineColor != null) {
			if(this.style == 1){
				g2D.setStroke(this.lineStyle);
			}
			g2D.setColor(lineColor);
		}
		g2D.draw(this.shape);
		if(this.selected){
			this.anchors.draw(g2D, this.shape.getBounds());
		}
	}
	public Rectangle getBounds(){
		return shape.getBounds();
	}
	public EAnchors contains(int x, int y) {
		this.currentEAnchor = null;
		if(this.selected) {
			this.currentEAnchor = this.anchors.contains(x,y);
			if(this.currentEAnchor != null)
				return this.currentEAnchor;
		}
		if(this.shape.intersects(x, y, 5, 5)){
			this.currentEAnchor = EAnchors.MM;
		}
		return this.currentEAnchor;
	}
	
	public void moveShape(int x, int y){
		this.affineTransform.setToTranslation(x, y);
		this.setShape(this.affineTransform.createTransformedShape(this.getShape()));
	}
	public void resizeShape(Point2D resizeFactor) {
		this.affineTransform.setToScale(resizeFactor.getX(), resizeFactor.getY());
		this.setShape(this.affineTransform.createTransformedShape(getShape()));
	}
	public void moveReverse(Point2D resizeAnchor) {
		this.affineTransform.setToTranslation(-resizeAnchor.getX(), -resizeAnchor.getY());
		this.setShape(this.affineTransform.createTransformedShape(this.getShape()));
	}	
	public void move(Point2D resizeAnchor) {
		this.affineTransform.setToTranslation(resizeAnchor.getX(), resizeAnchor.getY());
		this.setShape(this.affineTransform.createTransformedShape(this.getShape()));
	}
	public void rotateShape(double rotationAngle, Point centerP) {
		this.affineTransform.setToRotation(Math.toRadians(rotationAngle), centerP.getX(), centerP.getY());
		this.setShape(this.affineTransform.createTransformedShape(this.getShape()));
	}
	protected Shape shapeCopy(){
		AffineTransform affineTransform = new AffineTransform();
		Shape newShape = affineTransform.createTransformedShape(this.getShape());
		return newShape;
	}
	
	abstract public void setOrigin(int x, int y);
	abstract public void setPoint(int x, int y);
	abstract public void addPoint(int x, int y);
	abstract public void drawPoint(int x, int y);
	abstract public GShape deepCopy();
}
