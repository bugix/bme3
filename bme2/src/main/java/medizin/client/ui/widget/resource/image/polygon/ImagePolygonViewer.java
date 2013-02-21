package medizin.client.ui.widget.resource.image.polygon;

import java.util.List;

import medizin.client.util.Point;
import medizin.client.util.PolygonPath;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Path;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ImagePolygonViewer extends Composite {

	private static ImagePolygonViewerUiBinder uiBinder = GWT
			.create(ImagePolygonViewerUiBinder.class);

	interface ImagePolygonViewerUiBinder extends
			UiBinder<Widget, ImagePolygonViewer> {
	}

	@UiField(provided = true)
	DrawingArea drawingArea;
	
	@UiField
	Button btnPolyLine;
	
	@UiField
	Button btnClear;
	
	private PolygonPath currentPolygonPath = null;
	private Path currentPath = null;
	private org.vaadin.gwtgraphics.client.Image vImage;
	
	public ImagePolygonViewer(final String imageUrl,final int width,final int height,final List<PolygonPath> otherAnswer) {
		
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
		
		addOtherPolygons(otherAnswer);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		drawingArea.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {

				if (btnPolyLine.isEnabled() == false) {
					int x = event.getRelativeX(event.getRelativeElement());
					int y = event.getRelativeY(event.getRelativeElement());
					
					Log.info("Relative [x,y] : [" + x +","+ y+ "]");
					addNewPath(new Point(x, y));
				}
			}
		});

		drawingArea.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

			}
		});
	}

	private Path addNewPointToPath(Point point,Path path,Point startPoint) {
		
		Log.info("Point " + point) ;
		if(path == null) {
			path = new Path(point.getX(), point.getY());
			path.setFillColor("#a8ffcb"); // green
			path.setFillOpacity(0.3);
			drawingArea.add(path);
			drawingArea.bringToFront(path);			
		}else if(startPoint.withinDeltaRange(point)){
			path.close();
		} else {
			path.lineTo(point.getX(), point.getY());
		}
		return path;
	}

	private void addOtherPolygons(List<PolygonPath> otherAnswer) {
		
		for (PolygonPath polygonPath : otherAnswer) {
			
			List<Point> points = polygonPath.getPoints();
			Point startPoint = points.get(0);
			Path p = addNewPointToPath(startPoint, null, startPoint);
			
			for (int i = 1; i < points.size(); i++) {
				addNewPointToPath(points.get(i), p, startPoint);
			}
		}
	}

	@UiHandler("btnPolyLine")
	void clickedPolyLine(ClickEvent event) {
		btnPolyLine.setEnabled(false);
		currentPolygonPath = new PolygonPath();
	}
	
	@UiHandler("btnClear")
	void clearBtnclicked(ClickEvent event) {
		btnPolyLine.setEnabled(true);
		currentPolygonPath = null;
		if(currentPath != null) {
			currentPath.close();
			drawingArea.remove(currentPath);
		}
		currentPath = null;
	}
	
	private void addNewPath(Point point) {
		Log.info("client " + point) ;

		if (currentPath == null) {
			currentPath = new Path(point.getX(), point.getY());
			currentPath.setFillColor("#fa7575"); // red
			currentPath.setFillOpacity(0.3);
			drawingArea.add(currentPath);
			drawingArea.bringToFront(currentPath);
		} else {
			
			if(currentPolygonPath.getPoint(0).withinDeltaRange(point)) {
				currentPath.close();
				currentPolygonPath.addPoint(point);
				currentPolygonPath.closed();
				return;
			}else if(currentPolygonPath.isClosed() == false){
				currentPath.lineTo(point.getX(), point.getY());
			}else {
				Log.info("Do nothing");
			}
			
		}
		currentPolygonPath.addPoint(point);
		
		if(currentPolygonPath.isClosed() == false && currentPolygonPath.checkForPolygon() == false) {
		 	currentPath.close();
			drawingArea.remove(currentPath);
			btnPolyLine.setEnabled(true);
			currentPolygonPath = null;
			currentPath = null;
		}
	}

	public String getPoints() {
		
		if(currentPolygonPath != null) {
			return currentPolygonPath.toString();
		}
		return null;
	}

	public boolean isValidPolygon() {
		return currentPolygonPath != null && currentPolygonPath.isClosed();
	}
}
