package medizin.client.ui.widget.resource.image.rectangle;

import java.util.List;

import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.resource.image.rectangle.custom.Target;
import medizin.client.util.Point;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImageRectangleViewer extends Composite{

	private static ImageViewerUiBinder uiBinder = GWT
			.create(ImageViewerUiBinder.class);
	
	interface ImageViewerUiBinder extends UiBinder<Widget, ImageRectangleViewer> {
	}
	
	@UiField(provided = true)
	DrawingArea drawingArea;
	
//	@UiField
//	IconButton btnAdd;
	
	@UiField
	IconButton btnClear;
	
	@UiField
	HorizontalPanel btnHPPanel;
	
	private boolean validRectangle = false;
	private boolean btnAddClicked = false;
	private org.vaadin.gwtgraphics.client.Image vImage;
	private Target target = new Target(5, 5);
	private Rectangle currentRectangle = null;
	private Point currentPoint = null;
	private Function<Void, Void> pointClickedHandler;
	
	public ImageRectangleViewer(final String imageUrl,final int width,final int height,final List<Point> otherAnswer, final boolean displayBtnFlag) {
		
		Log.info("url :" + imageUrl + " width : " + width + " height :" + height);
		
		if(width <= 0 || height <= 0) {
			Log.error("Width and height cannot be negative or zero");
			return;
		}
		
		if(imageUrl == null || imageUrl.length() <= 0) {
			Log.error("URL cannot be null");
			return;
		}
				
		drawingArea = new DrawingArea(width, height);
		vImage = new org.vaadin.gwtgraphics.client.Image(0, 0, width, height, imageUrl);
		drawingArea.add(vImage);
		
		addOtherRectangle(otherAnswer);
		
		initWidget(uiBinder.createAndBindUi(this));
	
		if (displayBtnFlag)
		{
			drawingArea.add(target.getCircle());
			drawingArea.add(target.gethLine());
			drawingArea.add(target.getvLine());
			
			drawingArea.addMouseDownHandler(new MouseDownHandler() {

				@Override
				public void onMouseDown(MouseDownEvent event) {

					if (btnAddClicked) {
						int x = event.getRelativeX(event.getRelativeElement());
						int y = event.getRelativeY(event.getRelativeElement());
						
						Log.info("Relative [x,y] : [" + x +","+ y+ "]");
						currentPoint = new Point(x - target.getRadius(),y - target.getRadius());
						currentRectangle = addNewRectangle(new Point(x - target.getRadius(),y - target.getRadius()),"#fa7575"); //red color
						
						validRectangle = true;
						target.setVisible(false);
						btnAddClicked = false;
						if(pointClickedHandler != null) {
							pointClickedHandler.apply(null);	
						}
					}
				}

			});

			drawingArea.addMouseMoveHandler(new MouseMoveHandler() {

				@Override
				public void onMouseMove(MouseMoveEvent event) {
					int x = event.getRelativeX(event.getRelativeElement());
					int y = event.getRelativeY(event.getRelativeElement());
					target.setCenter(x, y);
				}
			});
		}		
		
		//btnAdd.setVisible(displayBtnFlag);
		btnClear.setVisible(displayBtnFlag);
		addBtnClicked();
	}
	
	
	private void addOtherRectangle(List<Point> otherAnswer) {
		
		for (Point p : otherAnswer) {
				addNewRectangle(p, "#a8ffcb"); // green color		
		}
	}
	
	private Rectangle addNewRectangle(Point point,String color) {
		Rectangle r = new Rectangle(point.getX(), point.getY(), target.getDiameter(), target.getDiameter()); 
		r.setFillColor(color);
		r.setFillOpacity(0.7);
		drawingArea.add(r);
		return r;
	}
	
	private void removeCurrentRectangle() {
		drawingArea.remove(currentRectangle);
		validRectangle = false;
	}
	
//	@UiHandler("btnAdd")
	void addBtnClicked(/*ClickEvent event*/) {
		//btnAdd.setEnabled(false);
		btnAddClicked = true;
		validRectangle = false;
	}
	
	@UiHandler("btnClear")
	void clearBtnclicked(ClickEvent event) {
		//btnAdd.setEnabled(true);
		currentPoint = null;
		target.setVisible(true);
		
		if(currentRectangle != null) { 
			removeCurrentRectangle();
		}
		currentRectangle = null;
		validRectangle = true;
		addBtnClicked();
	}
	
	public String getPoint() {

		if(currentPoint != null) {
			return currentPoint.toString();
		}
		return null;
	}

	public boolean isValidRectangle() {
		return validRectangle;
	}


	public void setCurrentPoint(Point point) {
		if(point == null) {
			return;
		}
		currentRectangle = addNewRectangle(point,"#fa7575"); //red color
		//btnAdd.setEnabled(false);
		validRectangle = true;
		target.setVisible(false);
		btnAddClicked = false;
	}
	
	public void addPointClicked(Function<Void,Void> pointClickedHandler) {
		this.pointClickedHandler = pointClickedHandler;
	}
	
	public HorizontalPanel getBtnHPPanel() {
		return btnHPPanel;
	}
}
