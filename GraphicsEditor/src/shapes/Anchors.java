package shapes;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.Vector;

import constants.GConstants.EAnchors;



public class Anchors extends Vector<Ellipse2D.Double>{
	private static final long serialVersionUID = 1L;
	public final static int ANCHORWIDTH = 8;
	public final static int ANCHORHIEGHT = 8;	
	public Rectangle getBounds(EAnchors eAnchorType) { return get(eAnchorType.ordinal()).getBounds();	}
	public Anchors() {
		for (int i = 0; i < EAnchors.values().length-1; i++) {
			this.add(new Ellipse2D.Double(0, 0, ANCHORWIDTH, ANCHORHIEGHT));
		}
	}
	private void computeCoordinates(Rectangle r){
		for (int i = 0; i < EAnchors.values().length-1; i++) {
			switch (EAnchors.values()[i]){
			case NN:
				this.get(i).x = (r.x+r.width/2)-(ANCHORWIDTH/2);
				this.get(i).y = r.y-ANCHORHIEGHT/2;
				break;
			case NE:
				this.get(i).x = (r.x+r.width)-(ANCHORWIDTH/2);
				this.get(i).y = r.y-ANCHORHIEGHT/2;
				break;
			case NW:
				this.get(i).x = r.x-(ANCHORWIDTH/2);
				this.get(i).y = r.y-ANCHORHIEGHT/2;
				break;
			case SS:
				this.get(i).x = (r.x+r.width/2)-(ANCHORWIDTH/2);
				this.get(i).y = (r.y+r.height)-ANCHORHIEGHT/2;
				break;
			case SW:
				this.get(i).x = r.x-(ANCHORWIDTH/2);
				this.get(i).y = (r.y+r.height)-ANCHORHIEGHT/2;
				break;
			case SE:
				this.get(i).x = (r.x+r.width)-(ANCHORWIDTH/2);
				this.get(i).y = (r.y+r.height)-ANCHORHIEGHT/2;
				break;
			case EE:
				this.get(i).x = r.x+r.width-(ANCHORWIDTH/2);
				this.get(i).y = r.y+r.height/2-ANCHORHIEGHT/2;
				break;
			case WW:
				this.get(i).x = r.x-(ANCHORWIDTH/2);
				this.get(i).y = r.y+r.height/2-ANCHORHIEGHT/2;
				break;
			case RR:
				this.get(i).x = (r.x+r.width/2)-(ANCHORWIDTH/2);
				this.get(i).y = r.y-ANCHORHIEGHT*4;
				break;
			default:
				break;
			}
		}
	}
	public void draw(Graphics2D g2D, Rectangle boundRectangle){
		computeCoordinates(boundRectangle);
		for (int i = 0; i < EAnchors.values().length-1; i++) {
			g2D.draw(this.get(i));
		}
	}
	public EAnchors contains(int x, int y) {
		for(int i = 0; i < EAnchors.values().length-1; i++){
			if(this.get(i).contains(x, y))
				return EAnchors.values()[i];
		}
		return null;
	}
//	public void setTransformedShape(AffineTransform affineTrnasform) {
//		for (int i=0; i<EAnchors.values().length-1; i++) {
//			Shape transformedShape = affineTrnasform.createTransformedShape(get(i));			
//			double x = transformedShape.getBounds().getCenterX();
//			double y = transformedShape.getBounds().getCenterY();
//			Ellipse2D.Double anchor = new Ellipse2D.Double();
//			anchor.setFrameFromCenter(x, y, x+ANCHORWIDTH/2, y+ANCHORHIEGHT/2);
//			set(i, anchor);
//		}
//	}
}
