package shapes;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

import constants.GConstants.EDrawingType;

public class GGroup extends GShape {
	private static final long serialVersionUID = 1L;
	private Vector<GShape> shapeVector;
	
	public Vector<GShape> getShapeVector() { return shapeVector; }

	public GGroup() {
		super(EDrawingType.TP, new Rectangle());
		this.shapeVector = new Vector<GShape>();
	}

	public void setOrigin(int x, int y) {
	}

	public void setPoint(int x, int y) {
	}

	public void addPoint(int x, int y) {
	}

	public void drawPoint(int x, int y) {
	}

	public void addShape(GShape shape) {
		this.shapeVector.add(shape);
		if (this.shapeVector.size() == 1) {
			this.setShape(shape.getBounds());
		} else {
			this.setShape(getShape().getBounds().createUnion(shape.getBounds()));
		}
	}

	public void draw(Graphics2D g2D) {
		for (GShape shape : shapeVector) {
			shape.draw(g2D);
		}
		if (this.isSelected()) {
			g2D.draw(this.getShape());
			this.setSelected(true);
			super.draw(g2D);
		}
	}
	public GShape deepCopy() {
		return null;
	}

}
