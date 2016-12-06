package transformer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import shapes.GShape;

abstract public class GTransformer {
	private GShape shape;
	protected Point previousP, anchorP;
	protected BasicStroke dashedLineStroke; 
	protected AffineTransform affineTransform;
	
	public Point getAnchorP() { return anchorP; }
	protected GShape getShape() { return this.shape; }
	
	public GTransformer(GShape shape){
		this.shape = shape;
		this.previousP = new Point(0, 0);
		this.anchorP = new Point(0, 0);
		this.affineTransform = new AffineTransform();
		float dashes[] = {4};
		this.dashedLineStroke = new BasicStroke(1,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
	}
	abstract public void initTransforming(int x, int y, Graphics2D g2D);
	abstract public void keepTransforming(int x, int y, Graphics2D g2D);
	abstract public void finishTransforming(int x, int y, Graphics2D g2D);
	abstract public void continueTransforming(int x, int y, Graphics2D g2D);
}
