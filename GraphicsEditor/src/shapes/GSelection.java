package shapes;

import java.util.Vector;

public class GSelection extends GRectangle{
	private static final long serialVersionUID = 1L;
	public Vector<GShape> shapes;
	
	public void init(Vector<GShape> shapes) {
		this.shapes = shapes;
	}

	public void selectShapes() {
		for(GShape shape: shapes)  {
			if(this.getShape().contains(shape.getBounds())) {
				shape.setSelected(true);
			}
		}
	}
}
