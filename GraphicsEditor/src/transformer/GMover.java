package transformer;

import java.awt.Graphics2D;
import java.awt.Point;

import shapes.GShape;

public class GMover extends GTransformer {
	public GMover(GShape shape) {
		super(shape);
		this.previousP = new Point();
	}
	public void initTransforming(int x, int y, Graphics2D g2D) {
		previousP.x = x;
		previousP.y = y;
		//this.getShape().setPoint(x, y);
		this.getShape().draw(g2D);
	}
	public void keepTransforming(int x, int y, Graphics2D g2D) {
		g2D.setStroke(this.dashedLineStroke);
		this.getShape().draw(g2D);
		this.affineTransform.setToTranslation(x-previousP.x, y-previousP.y);
		this.getShape().setShape(this.affineTransform.createTransformedShape(this.getShape().getShape()));
		this.getShape().draw(g2D);
		previousP.x = x;
		previousP.y = y;
	}
	public void finishTransforming(int x, int y, Graphics2D g2D) {
	}
	public void continueTransforming(int x, int y, Graphics2D g2D) {
	}

}
