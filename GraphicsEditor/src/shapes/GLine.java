package shapes;

import java.awt.geom.Line2D;

import constants.GConstants.EDrawingType;

public class GLine extends GShape {
	private static final long serialVersionUID = 1L;
	private Line2D line;
	public GLine(){
		super(EDrawingType.TP, new Line2D.Double(0, 0, 0, 0));
		this.line = (Line2D)getShape();
	}
	public void setOrigin(int x, int y) {
		this.line.setLine(x, y, x, y);
	}
	public void setPoint(int x, int y) {
	}
	public void addPoint(int x, int y) {
	}
	public void drawPoint(int x, int y) {
		this.line.setLine(this.line.getX1(), this.line.getY1(), x, y);
	}
	public GShape deepCopy(){
		GLine returnShape = new GLine();
		returnShape.setShape(this.shapeCopy());
		returnShape.setFillColor(fillColor);
		returnShape.setLineColor(lineColor);
		return returnShape;
	}
}
