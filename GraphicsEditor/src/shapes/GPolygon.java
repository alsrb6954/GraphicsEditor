package shapes;

import java.awt.Polygon;

import constants.GConstants.EDrawingType;

public class GPolygon extends GShape{
	private static final long serialVersionUID = 1L;
	private Polygon polygon;
	
	public GPolygon(){
		super(EDrawingType.NP, new Polygon());
		this.polygon =(Polygon)this.getShape();
	}
	public void setOrigin(int x, int y) {
		this.polygon.addPoint(x, y);
		this.polygon.addPoint(x, y);
	}
	public void setPoint(int x, int y) {
	}
	public void addPoint(int x, int y) {
		this.polygon.addPoint(x, y);
	}
	public void drawPoint(int x, int y) {
		this.polygon.xpoints[this.polygon.npoints-1] = x;
		this.polygon.ypoints[this.polygon.npoints-1] = y;
	}
	public GShape deepCopy(){
		GPolygon returnShape = new GPolygon();
		returnShape.setShape(this.shapeCopy());
		returnShape.setFillColor(fillColor);
		returnShape.setLineColor(lineColor);
		return returnShape;
	}
}
