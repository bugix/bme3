package medizin.client.ui.widget.resource.image.rectangle.custom;

import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;

public class Target {

	private int centerX = 30;
	private int centerY = 30;
	private final int iRadius = 20;
	private final int eRadius = 5;
	
	private Circle circle = new Circle(centerX, centerY, 20);
	private Line hLine = new Line(gethLineX1(),gethLineY1(),gethLineX2(),gethLineY2());//new Line(centerX, centerY - radius , centerX, centerY + radius);
	private Line vLine = new Line(getvLineX1(),getvLineY1(),getvLineX2(),getvLineY2()); //new Line(centerX - radius, centerY, centerX + radius, centerY);
	
	public Target(int centerX,int centerY) {
	
		circle.setFillOpacity(0.3);
	}
	
	private int getvLineY2() {
		return centerY;
	}

	private int getvLineX2() {
		return centerX + iRadius;
	}

	private int getvLineY1() {
		return centerY;
	}

	private int getvLineX1() {
		return centerX - iRadius;
	}

	private int gethLineY2() {
		return centerY + iRadius;
	}

	private int gethLineX2() {
		return centerX;
	}

	private int gethLineY1() {
		return centerY - iRadius;
	}

	private int gethLineX1() {
		return centerX;
	}

	public void setCenter(int x,int y) {
		this.centerX = x;
		this.centerY = y;
		
		translateToNewCenter();
	}
	
	
	private void translateToNewCenter() {
		circle.setX(centerX);
		circle.setY(centerY);
		
		hLine.setX1(gethLineX1());
		hLine.setY1(gethLineY1());
		hLine.setX2(gethLineX2());
		hLine.setY2(gethLineY2());
		
		vLine.setX1(getvLineX1());
		vLine.setY1(getvLineY1());
		vLine.setX2(getvLineX2());
		vLine.setY2(getvLineY2());
		
	}

	public VectorObject getCircle() {
		return circle;
	}

	public VectorObject gethLine() {
		return hLine;
	}

	public VectorObject getvLine() {
		return vLine;
	}

	public int getDiameter() {
		return 2*eRadius;
	}

	public void setVisible(boolean visible) {
		circle.setVisible(visible);
		hLine.setVisible(visible);
		vLine.setVisible(visible);
	}

	public int getRadius() {
		return eRadius;
	}
	
}
