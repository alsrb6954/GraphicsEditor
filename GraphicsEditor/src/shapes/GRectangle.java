package shapes;

import java.awt.Rectangle;

import constants.GConstants.EDrawingType;

public class GRectangle extends GShape{
	private static final long serialVersionUID = 1L;
	private Rectangle rectangle;
	private int px,py;
	public GRectangle(){
		super(EDrawingType.TP, new Rectangle(0, 0, 0, 0));
		this.rectangle =(Rectangle)this.getShape();
	}
	public void setOrigin(int x, int y) {
		this.rectangle.setLocation(x, y);
		this.px = x;
		this.py = y;
	}
	public void setPoint(int x, int y){
	}
	public void addPoint(int x, int y) {
	}
	public void drawPoint(int x, int y) {
		this.rectangle.width = Math.abs(x - this.px);
		this.rectangle.height = Math.abs(y - this.py);
		if(this.px>x){
			if(this.py>y){
				this.rectangle.setLocation(x, y);
			} else{
				this.rectangle.x = x;
			}
		}else {
			if(this.py>y){
				this.rectangle.y = y;
			}
		}
	}
	
	public GShape deepCopy(){
		GRectangle returnShape = new GRectangle();
		returnShape.setShape(this.shapeCopy());
		returnShape.setFillColor(fillColor);
		returnShape.setLineColor(lineColor);
		return returnShape;
	}
}
