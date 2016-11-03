package shapes;

import java.awt.Graphics2D;
import java.awt.Polygon;

import constants.GConstants.EDrawingType;

public class GPolygon extends GShape{
	private Polygon polygon;
	
	public GPolygon(){
		super(EDrawingType.NP);
		this.polygon = new Polygon();
		this.shape = polygon;
	}
	@Override
	public void initDrawing(int x, int y, Graphics2D g2D) {
		this.polygon.addPoint(x, y);
		this.polygon.addPoint(x, y);
	}
	@Override
	public void keepDrawing(int x, int y, Graphics2D g2D) {
		g2D.drawPolyline(this.polygon.xpoints, this.polygon.ypoints, this.polygon.npoints);
		this.polygon.xpoints[this.polygon.npoints-1] = x;
		this.polygon.ypoints[this.polygon.npoints-1] = y;
		g2D.drawPolyline(this.polygon.xpoints, this.polygon.ypoints, this.polygon.npoints);
	}
	@Override
	public void continueDrawing(int x, int y, Graphics2D g2d) {
		this.polygon.addPoint(x, y);
	}
	@Override
	public void finishDrawing(int x, int y, Graphics2D g2D) {
		g2D.drawPolyline(this.polygon.xpoints, this.polygon.ypoints, this.polygon.npoints);
		this.draw(g2D);
	}
	@Override
	public void draw(Graphics2D g2D) {
		g2D.draw(this.polygon);
	}
	
}
