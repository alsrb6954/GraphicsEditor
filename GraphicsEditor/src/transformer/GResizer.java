package transformer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

import shapes.GGroup;
import shapes.GShape;

public class GResizer extends GTransformer {
	private Point resizeAnchorP;
	private Vector<GShape> groupChildVector;
	public GResizer(GShape shape) {
		super(shape);
		this.previousP = new Point();
	}
	public void initTransforming(int x, int y, Graphics2D g2D) {
		this.previousP.x = x;
		this.previousP.y = y;
		this.resizeAnchorP = getResizeAnchor();
		this.getShape().moveReverse(this.resizeAnchorP);
		if(getShape() instanceof GGroup){
			GGroup groupChild = (GGroup)getShape();
			groupChildVector = groupChild.getShapeVector();
			for(GShape childShape : groupChildVector){
				this.affineTransform.setToTranslation(-resizeAnchorP.getX(), -resizeAnchorP.getY());
				childShape.setShape(this.affineTransform.createTransformedShape(childShape.getShape()));
			}
		}
	}
	public void keepTransforming(int x, int y, Graphics2D g2D) {
		g2D.setStroke(this.dashedLineStroke);
		
		Point2D resizeFactor = computeResizeFactor(this.previousP.x, this.previousP.y, x, y);
		AffineTransform affineTransform = g2D.getTransform();
		g2D.translate(this.resizeAnchorP.getX(), this.resizeAnchorP.getY());
		
		this.getShape().draw(g2D);
		this.getShape().resizeShape(resizeFactor);
		if(getShape() instanceof GGroup){
			GGroup groupChild = (GGroup)getShape();
			groupChildVector = groupChild.getShapeVector();
			for(GShape childShape : groupChildVector){
				Point2D CresizeFactor = computeResizeFactor(this.previousP.x, this.previousP.y, x, y);
				this.affineTransform.setToScale(CresizeFactor.getX(), CresizeFactor.getY());
				childShape.setShape(this.affineTransform.createTransformedShape(childShape.getShape()));
			}
		}
		this.getShape().draw(g2D);
		
		g2D.setTransform(affineTransform);
		
		this.previousP.x = x;
		this.previousP.y = y;
	}
	public void finishTransforming(int x, int y, Graphics2D g2D) {
		this.getShape().move(this.resizeAnchorP);
		if(getShape() instanceof GGroup){
			GGroup groupChild = (GGroup)getShape();
			groupChildVector = groupChild.getShapeVector();
			for(GShape childShape : groupChildVector){
				this.affineTransform.setToTranslation(resizeAnchorP.getX(), resizeAnchorP.getY());
				childShape.setShape(this.affineTransform.createTransformedShape(childShape.getShape()));
			}
		}
	}
	public void continueTransforming(int x, int y, Graphics2D g2D) {
	}
	private Double computeResizeFactor(int previousX, int previousY, int currentX, int currentY) {
		double deltaW = 0;
		double deltaH = 0;
		
		switch (getShape().getCurrentEAnchor()) {
			case EE: deltaW=  currentX-previousX; 	deltaH=  0; 					break;
			case WW: deltaW=-(currentX-previousX);	deltaH=  0; 					break;
			case SS: deltaW=  0;					deltaH=  currentY-previousY; 	break;
			case NN: deltaW=  0;					deltaH=-(currentY-previousY); 	break;
			case SE: deltaW=  currentX-previousX; 	deltaH=  currentY-previousY;	break;
			case NE: deltaW=  currentX-previousX; 	deltaH=-(currentY-previousY);	break;
			case SW: deltaW=-(currentX-previousX);	deltaH=  currentY-previousY;	break;			
			case NW: deltaW=-(currentX-previousX);	deltaH=-(currentY-previousY);	break;
			default: break;
		}
		double currentW = getShape().getBounds().getWidth();
		double currentH = getShape().getBounds().getHeight();
		double xFactor = 1.0;
		if(currentW > 0.0){
			xFactor = (xFactor+deltaW/currentW);
		}
		double yFactor = 1.0;
		if(currentH > 0.0){
			yFactor = (yFactor+deltaH/currentH);
		}
		
		return new Point.Double(xFactor,yFactor);
	}
//	private Point getResizeAnchor() {
//		Anchors anchors = getShape().getAnchors();
//		Point resizeAnchor = new Point();
//		switch (this.getShape().getCurrentEAnchor()) {
//		case EE: resizeAnchor.setLocation(anchors.getBounds(constants.GConstants.EAnchors.WW).getX(), this.getShape().getBounds().getX()+this.getShape().getBounds().getWidth()); break;
//		case WW: resizeAnchor.setLocation(anchors.getBounds(constants.GConstants.EAnchors.EE).getX(), this.getShape().getBounds().getX()+this.getShape().getBounds().getWidth()); break;
//		case SS: resizeAnchor.setLocation(this.getShape().getBounds().getX()+this.getShape().getBounds().getWidth(), anchors.getBounds(constants.GConstants.EAnchors.NN).getY()); break;
//		case NN: resizeAnchor.setLocation(this.getShape().getBounds().getX()+this.getShape().getBounds().getWidth(), anchors.getBounds(constants.GConstants.EAnchors.SS).getY()); break;
//		case SE: resizeAnchor.setLocation(anchors.getBounds(constants.GConstants.EAnchors.NW).getX(), anchors.getBounds(constants.GConstants.EAnchors.NW).getY()); break;
//		case NE: resizeAnchor.setLocation(anchors.getBounds(constants.GConstants.EAnchors.SW).getX(), anchors.getBounds(constants.GConstants.EAnchors.SW).getY()); break;
//		case SW: resizeAnchor.setLocation(anchors.getBounds(constants.GConstants.EAnchors.NE).getX(), anchors.getBounds(constants.GConstants.EAnchors.NE).getY()); break;
//		case NW: resizeAnchor.setLocation(anchors.getBounds(constants.GConstants.EAnchors.SE).getX(), anchors.getBounds(constants.GConstants.EAnchors.SE).getY()); break;
//		default: break;
//		}
//		return resizeAnchor;
//	}
	private Point getResizeAnchor() {
		Point resizeAnchor = new Point();
		Ellipse2D.Double tempAnchor = null;
		if (this.getShape().getCurrentEAnchor() == (constants.GConstants.EAnchors.NW)) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.SE.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.NN) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.SS.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.NE) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.SW.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.WW) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.EE.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.EE) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.WW.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.SW) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.NE.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.SS) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.NN.ordinal());
		} else if (this.getShape().getCurrentEAnchor() == constants.GConstants.EAnchors.SE) {
			tempAnchor = this.getShape().getAnchors().get(constants.GConstants.EAnchors.NW.ordinal());
		}
		resizeAnchor.setLocation(tempAnchor.x, tempAnchor.y);
		return resizeAnchor;
	}
}
