package shapes;

import java.awt.Rectangle;

import constants.GConstants.EDrawingType;

public class GRectangle extends GShape{
	private static final long serialVersionUID = 1L;
	private Rectangle rectangle;
	
	public GRectangle(){
		super(EDrawingType.TP, new Rectangle(0, 0, 0, 0));
		this.rectangle =(Rectangle)this.getShape();
	}
	public void setOrigin(int x, int y) {
		this.rectangle.setLocation(x, y);
	}
	public void setPoint(int x, int y){
	}
	public void addPoint(int x, int y) {
	}
	public void drawPoint(int x, int y) {
		this.rectangle.width = x - this.rectangle.x;
		this.rectangle.height = y - this.rectangle.y;
	}
	
	public GShape deepCopy(){
		GRectangle returnShape = new GRectangle();
		returnShape.setShape(this.shapeCopy());
		returnShape.setFillColor(fillColor);
		returnShape.setLineColor(lineColor);
		return returnShape;
	}
}
