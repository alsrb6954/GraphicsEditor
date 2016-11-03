package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import constants.GConstants.EDrawingType;

public class GEllipse extends GShape {
	private Ellipse2D ellipse;
	public GEllipse(){
		super(EDrawingType.TP);
		this.ellipse = new Ellipse2D.Double(0, 0, 0, 0);
		this.shape = ellipse;
	}
	@Override
	public void initDrawing(int x, int y, Graphics2D g2D) {
		this.ellipse.setFrameFromDiagonal(x, y, x, y);
	}

	@Override
	public void keepDrawing(int x, int y, Graphics2D g2D) {
		this.draw(g2D);
		this.ellipse.setFrameFromDiagonal(this.ellipse.getX(), this.ellipse.getY(), x, y);;
		this.draw(g2D);
	}
	@Override
	public void draw(Graphics2D g2D) {
		g2D.draw(ellipse);	
	}
	
	@Override
	public void finishDrawing(int x, int y, Graphics2D g2D) {

	}
	@Override
	public void continueDrawing(int x, int y, Graphics2D g2d) {
		// TODO Auto-generated method stub
		
	}

}
