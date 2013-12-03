package medizin.client.ui.widget.resource.image.polygon;

import java.util.List;

import medizin.client.ui.view.QuestionTextViewDialogBoxImpl;
import medizin.client.ui.widget.IconButton;
import medizin.client.util.ClientUtility;
import medizin.client.util.ImageWidthHeight;
import medizin.client.util.Point;
import medizin.client.util.PolygonPath;
import medizin.shared.i18n.BmeConstants;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Path;

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

public class ImagePolygonViewer extends Composite implements ImageWidthHeight {

	private static ImagePolygonViewerUiBinder uiBinder = GWT.create(ImagePolygonViewerUiBinder.class);

	interface ImagePolygonViewerUiBinder extends UiBinder<Widget, ImagePolygonViewer> {}
	
	private final BmeConstants constants = GWT.create(BmeConstants.class);

	@UiField(provided = true)
	DrawingArea drawingArea;
	
/*	@UiField
	IconButton btnPolyLine;*/
	
	@UiField
	IconButton btnClear;
	
	@UiField
	IconButton btnCloseArea;

	@UiField
	HorizontalPanel btnHPPanel;
	
	@UiField 
	IconButton info;
	
	private Function<Void, Void> pathClosedHandler;
	
	private PolygonPath currentPolygonPath = null;
	private Path currentPath = null;
	private org.vaadin.gwtgraphics.client.Image vImage;

	private final String imageUrl;

	private final List<PolygonPath> otherAnswer;

	private final boolean diplayBtnFlag;
	
	public ImagePolygonViewer(final String imageUrl,final Integer width1,final Integer height1,final List<PolygonPath> otherAnswer, boolean diplayBtnFlag) {
		
		this.imageUrl = imageUrl;
		this.otherAnswer = otherAnswer;
		this.diplayBtnFlag = diplayBtnFlag;
		
		drawingArea = new DrawingArea(0, 0);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		Log.info("url :" + imageUrl + " width : " + width1 + " height :" + height1);
		
		if(width1 == null || height1 == null) {
			ClientUtility.getImageWidthHeight(imageUrl, this);			
		}else {
			apply(width1, height1);
		}
		
		
	}

	private Path addNewPointToPath(Point point,Path path,Point startPoint) {
		
		Log.info("Point " + point) ;
		if(path == null) {
			path = new Path(point.getX(), point.getY());
			path.setFillColor("#a8ffcb"); // green
			path.setFillOpacity(0.3);
			drawingArea.add(path);
			drawingArea.bringToFront(path);			
		} else if(startPoint.withinDeltaRange(point)){
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
			p.close();
		}
	}

	/*@UiHandler("btnPolyLine")*/
	void clickedPolyLine(/*ClickEvent event*/) {
		/*btnPolyLine.setEnabled(false);*/
		currentPolygonPath = new PolygonPath();
	}
	
	@UiHandler("btnClear")
	void clearBtnclicked(ClickEvent event) {
		//btnPolyLine.setEnabled(true);
		currentPolygonPath = null;
		if(currentPath != null) {
			currentPath.close();
			drawingArea.remove(currentPath);
		}
		currentPath = null;
		clickedPolyLine();
		enabledCloseAreaBtn();
	}
	
	@UiHandler("btnCloseArea") 
	void clickedBtnCloseArea(ClickEvent event) {
		if(currentPath != null && currentPolygonPath != null && currentPolygonPath.isClosed() == false) {
			currentPath.close();
			currentPolygonPath.closed();
		}
		enabledCloseAreaBtn();
		if(pathClosedHandler != null) {
			pathClosedHandler.apply(null);
		}
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
//				currentPath.close();
//				currentPolygonPath.addPoint(point);
//				currentPolygonPath.closed();
				clickedBtnCloseArea(null);
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
			//btnPolyLine.setEnabled(true);
			currentPolygonPath = null;
			currentPath = null;
		}
		
		enabledCloseAreaBtn();
		
	}

	private void enabledCloseAreaBtn() {
		if(currentPolygonPath != null) {
			if(currentPolygonPath.isClosed() == true) {
				btnCloseArea.setEnabled(false);
			} else if(currentPolygonPath.size() >= 3) {
				btnCloseArea.setEnabled(true);
			}  else {
				btnCloseArea.setEnabled(false);
			}
		} else {
			btnCloseArea.setEnabled(false);
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

	@Override
	public void apply(Integer width, Integer height) {
		if(width <= 0 || height <= 0) {
			Log.error("Width and height cannot be negative or zero");
			return;
		}
		
		if(imageUrl == null || imageUrl.length() <= 0) {
			Log.error("URL cannot be null");
			return;
		}
		
		drawingArea.setWidth(width);
		drawingArea.setHeight(height);
		vImage = new org.vaadin.gwtgraphics.client.Image(0, 0, width, height, imageUrl);
		drawingArea.add(vImage);
		
		addOtherPolygons(otherAnswer);
		
		clickedPolyLine();
		//initWidget(uiBinder.createAndBindUi(this));
		if(diplayBtnFlag) {
			drawingArea.addMouseDownHandler(new MouseDownHandler() {

				@Override
				public void onMouseDown(MouseDownEvent event) {

					/*if (btnPolyLine.isEnabled() == false) {*/
						int x = event.getRelativeX(event.getRelativeElement());
						int y = event.getRelativeY(event.getRelativeElement());
						
						Log.info("Relative [x,y] : [" + x +","+ y+ "]");
						Log.info("--Client [x,y] : " + event.getClientX() +" , " + event.getClientY());
						Log.info("--Client [x,y] : " + event.getScreenX() +" , " + event.getScreenY());
						
						if(currentPath == null) {
							infoDialogBox(event.getClientX(),event.getClientY());
						}
						addNewPath(new Point(x, y));
						
					/*}*/
				}
			});

			drawingArea.addMouseMoveHandler(new MouseMoveHandler() {

				@Override
				public void onMouseMove(MouseMoveEvent event) {

				}
			});
		}
 				
		btnClear.setVisible(diplayBtnFlag);
		//btnPolyLine.setVisible(diplayBtnFlag);		
	}

	public void setCurrentPolygon(PolygonPath polygonPath) {
		if(polygonPath == null || polygonPath.getPoints().isEmpty()) {
			Log.info("No polygon path");
			return;
		}
		
		currentPolygonPath = new PolygonPath();
		for (Point point : polygonPath.getPoints()) {
			addNewPath(point);
		}
		
		//btnPolyLine.setEnabled(false);
	}

	public void addPathClosedHandler(Function<Void, Void> pathClosedHandler) {
		this.pathClosedHandler = pathClosedHandler;
	}
	
	public HorizontalPanel getBtnHPPanel() {
		return btnHPPanel;
	}
	
	@UiHandler("info")
	public void fileInfoClicked(ClickEvent event)
	{
		QuestionTextViewDialogBoxImpl dialogBox = new QuestionTextViewDialogBoxImpl();
		dialogBox.setHtmlText(constants.showInImageInfo());
		dialogBox.setWidth("350px");
		dialogBox.show();
		dialogBox.setLeftTopPosition((info.getElement().getAbsoluteLeft()-dialogBox.getOffsetWidth()+30), (info.getAbsoluteTop()+20));
		
	}
	
	private void infoDialogBox(int leftValue, int topValue) {
		QuestionTextViewDialogBoxImpl dialogBox = new QuestionTextViewDialogBoxImpl();
		dialogBox.setHtmlText(constants.showInImageInfo());
		dialogBox.setWidth("350px");
		dialogBox.show();
		dialogBox.setLeftTopPosition(leftValue-dialogBox.getOffsetWidth()+16,topValue+6);
	}
}
