package transformer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import shapes.GGroup;
import shapes.GShape;

public class GMover extends GTransformer {
	private Vector<GShape> groupChildVector;
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
		this.getShape().moveShape(x-previousP.x, y-previousP.y);
		if(getShape() instanceof GGroup){
			GGroup groupChild = (GGroup)getShape();
			groupChildVector = groupChild.getShapeVector();
			for(GShape childShape : groupChildVector){
				affineTransform.setToTranslation(x-this.previousP.x, y-this.previousP.y);
				childShape.setShape(affineTransform.createTransformedShape(childShape.getShape()));
			}
		}
		this.getShape().draw(g2D);
		previousP.x = x;
		previousP.y = y;
	}
	public void finishTransforming(int x, int y, Graphics2D g2D) {
	}
	public void continueTransforming(int x, int y, Graphics2D g2D) {
	}

}
