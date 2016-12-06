package shapes;

import java.awt.geom.Ellipse2D;

import constants.GConstants.EDrawingType;

public class GEllipse extends GShape {
	private static final long serialVersionUID = 1L;
	private Ellipse2D ellipse;
	public GEllipse(){
		super(EDrawingType.TP, new Ellipse2D.Double(0, 0, 0, 0));
		this.ellipse = (Ellipse2D)getShape();
	}
	public void setOrigin(int x, int y) {
		this.ellipse.setFrameFromDiagonal(x, y, x, y);
	}
	public void setPoint(int x, int y) {
		this.px = x;
		this.py = y;
	}
	public void addPoint(int x, int y) {
	}
	public void drawPoint(int x, int y) {
		this.ellipse.setFrameFromDiagonal(this.ellipse.getX(), this.ellipse.getY(), x, y);
	}
}
