package transformer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import shapes.GGroup;
import shapes.GShape;

public class GRotator extends GTransformer {
	private Point centerP;
	private Vector<GShape> groupChildVector;
	public GRotator(GShape shape) {
		super(shape);
		previousP = new Point();
	}
	public void initTransforming(int x, int y, Graphics2D g2D) {
		this.previousP.x = x;
		this.previousP.y = y;
		this.centerP = new Point((int) this.getShape().getBounds().getCenterX(), (int)this.getShape().getBounds().getCenterY());
	}
	public void keepTransforming(int x, int y, Graphics2D g2D) {
		g2D.setStroke(this.dashedLineStroke);
		
		double rotationAngle = computeRotationAngle(this.centerP, this.previousP, new Point(x, y));
		AffineTransform affineTransform = g2D.getTransform();
		g2D.translate(this.getAnchorP().getX(), this.getAnchorP().getY());
		
		this.getShape().draw(g2D);
		this.getShape().rotateShape(rotationAngle, this.centerP);
		if(getShape() instanceof GGroup){
			GGroup groupChild = (GGroup)getShape();
			groupChildVector = groupChild.getShapeVector();
			for(GShape childShape : groupChildVector){
				this.affineTransform.setToRotation(Math.toRadians(rotationAngle), centerP.getX(), centerP.getY());
				childShape.setShape(this.affineTransform.createTransformedShape(childShape.getShape()));
			}
		}
		this.getShape().draw(g2D);
		
		g2D.setTransform(affineTransform);
		
		this.previousP.x = x;
		this.previousP.y = y;
	}
	public void finishTransforming(int x, int y, Graphics2D g2D) {
	}
	public void continueTransforming(int x, int y, Graphics2D g2D) {
	}
	private double computeRotationAngle(Point startP, Point previousP, Point currentP) {
		double startAngle = Math.toDegrees(
				Math.atan2(startP.getX()-previousP.getX(), startP.getY()-previousP.getY()));
		double endAngle = Math.toDegrees(
				Math.atan2(startP.getX()-currentP.getX(), startP.getY()-currentP.getY()));
		double angle = startAngle-endAngle;
		if (angle<0) angle += 360;
		return angle;
	}

}
