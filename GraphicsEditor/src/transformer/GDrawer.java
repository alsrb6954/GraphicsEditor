package transformer;

import java.awt.Graphics2D;
import java.awt.Point;

import shapes.GShape;

public class GDrawer extends GTransformer {
	public GDrawer(GShape shape) {
		super(shape);
		this.previousP = new Point();
	}
	public void initTransforming(int x, int y, Graphics2D g2D) {
		this.previousP.x = x;
		this.previousP.y = y;
		this.getShape().setOrigin(x,y);
	}
	public void keepTransforming(int x, int y, Graphics2D g2D) {
		g2D.setStroke(this.dashedLineStroke);
		this.getShape().draw(g2D);
		this.getShape().drawPoint(x,y);
		this.getShape().draw(g2D);
	}
	public void finishTransforming(int x, int y, Graphics2D g2D) {
	}
	public void continueTransforming(int x, int y, Graphics2D g2D) {
		this.getShape().addPoint(x,y);
	}

}
