package constants;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import shapes.GEllipse;
import shapes.GLine;
import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GSelection;
import shapes.GShape;

public class GConstants {
	public final static String MAINFRAME_TITLE = "GraphicsEditor";
	public final static String FILEMENU_TITLE = "File";
	public final static String EDITMENU_TITLE = "Edit";
	public final static String COLORMENU_TITLE = "Color";
	public static final String FILLCOLOR_TITLE ="Select fill color";
	public static final String LINECOLOR_TITLE="Select line color";
	public static final Color COLOR_LINE_DEFAULT =Color.black;
	public static final Color COLOR_FILL_DEFAULT =Color.white;
	public static Point point = new Point(15, 15);
	public static Image img = Toolkit.getDefaultToolkit().getImage("rsc/rotateCursor.png");
	public enum EAnchors {
		NN(new Cursor(Cursor.N_RESIZE_CURSOR)), 
		NE(new Cursor(Cursor.NE_RESIZE_CURSOR)), 
		NW(new Cursor(Cursor.NW_RESIZE_CURSOR)), 
		SS(new Cursor(Cursor.S_RESIZE_CURSOR)), 
		SW(new Cursor(Cursor.SW_RESIZE_CURSOR)), 
		SE(new Cursor(Cursor.SE_RESIZE_CURSOR)), 
		EE(new Cursor(Cursor.E_RESIZE_CURSOR)), 
		WW(new Cursor(Cursor.W_RESIZE_CURSOR)), 
		RR(Toolkit.getDefaultToolkit().createCustomCursor(img, point, "Rocation")), 
		MM(new Cursor(Cursor.MOVE_CURSOR));
		private Cursor cursor;
		private EAnchors(Cursor cursor) {
			this.cursor = cursor;
		}
		public Cursor getCursor() { return this.cursor; }
	};
	// JFrame attributes
	public static enum EMainFrame {
		X(100), Y(100), W(400), H(600);
		private int value;
		private EMainFrame(int value) {
			this.value = value;
		}
		public int getValue() { return this.value; }
	}
	public static enum EFileMenuItem{
		newItem("new"), 
		open("open"), 
		save("save"), 
		saveAs("saveAs"), 
		print("print"),
		close("close"), 
		exit("exit");
		private String text;
		private EFileMenuItem(String text){
			this.text = text;
		}
		public String getText() { return this.text; }
	}
	public static enum EEditMenuItem{
		cut("cut"), 
		copy("copy"), 
		paste("paste"),
		delete("delete"), 
		redo("redo"), 
		undo("undo"),
		group("group"),
		unGroup("unGroup");
		private String text;
		private EEditMenuItem(String text){
			this.text = text;
		}
		public String getText() { return this.text; }
	}
	public static enum EColorMenuItem{
		SetLineColor("SetLineColor"), 
		ClearLineColor("ClearLineColor"), 
		SetFillColor("SetFillColor"),
		ClearFillColor("ClearFillColor");
		private String text;
		private EColorMenuItem(String text){
			this.text = text;
		}
		public String getText() { return this.text; }
	}
	public static enum EDrawingType {
		TP, NP;
	}
	public static enum EToolBarButton {
		selection("rsc/select.gif", "rsc/selectSLT.gif", new GSelection()),
		rectangle("rsc/rectangle.gif", "rsc/rectangleSLT.gif", new GRectangle()),
		ellipse("rsc/ellipse.gif", "rsc/ellipseSLT.gif", new GEllipse()),
		line("rsc/line.gif", "rsc/lineSLT.gif", new GLine()),
		polygon("rsc/polygon.gif", "rsc/polygonSLT.gif", new GPolygon());
		
		private String iconName;
		private String selectedIconName;
		private GShape shape;

		private EToolBarButton(String iconName, String selectedIconName, GShape shape) {
			this.iconName = iconName;
			this.selectedIconName = selectedIconName;
			this.shape = shape;
		}
		public String getIconName(){ return this.iconName; }
		public String getSelectedIconName(){ return this.selectedIconName; }
		public GShape getShape() { return this.shape; }
	}
	

}
