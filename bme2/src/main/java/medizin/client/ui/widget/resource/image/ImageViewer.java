package medizin.client.ui.widget.resource.image;

import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ImageViewer extends Composite{

	private static ImageViewerUiBinder uiBinder = GWT
			.create(ImageViewerUiBinder.class);

	private String url = null;

	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	interface ImageViewerUiBinder extends UiBinder<Widget, ImageViewer> {
	}

	private int originalWidth;
	private int originalHeight;
	/*private int currentWidth;
	private int currentHeight;
	private int predefinedWidth;
	private int predefinedHeight;*/
	
	
	@UiField(provided = true)
	DrawingArea drawingArea;
	
	/*@UiField
	CheckBox chkChangeSize;
	
	@UiField
	Label lblCurrentSize;*/
	
	/*@UiField
	Label lblHeight;
	
	@UiField
	Label lblWidth;
	
	@UiField
	TextBox txtHeight;
	
	@UiField
	TextBox txtWidth;*/

		
	private org.vaadin.gwtgraphics.client.Image image;
	
	public ImageViewer() {
		
		drawingArea = new DrawingArea(100, 100);
		initWidget(uiBinder.createAndBindUi(this));
		
		/*lblHeight.setText(constants.height());
		lblWidth.setText(constants.width());*/
		
		/*predefinedWidth = 600;
		predefinedHeight = 500;*/
		
		/*chkChangeSize.setText(setWidthHeight(constants.changeToSize(),predefinedWidth,predefinedHeight));
		
		chkChangeSize.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				chkChangeSizeOnValueChangeHandler(event);	
				
			}
		});*/
		
		/*txtHeight.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changeHeightandWidthUsingTextBox(event);
			}

		});
		
		txtWidth.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changeHeightandWidthUsingTextBox(event);
			}
		});*/
		
	}

	
	/*private void changeHeightandWidthUsingTextBox(
			ValueChangeEvent<String> event) {
		
		try {
			currentHeight = Integer.parseInt(txtHeight.getValue(), 10);
			currentWidth = Integer.parseInt(txtWidth.getValue(), 10);
			lblCurrentSize.setText(setWidthHeight(constants.currentSize(),currentWidth,currentHeight));
			setImageAndCanvasSize(currentWidth, currentHeight);
		}catch (Exception e) {
			Window.alert("Set height and width as number.");
		}
	}*/
	
	/*private void chkChangeSizeOnValueChangeHandler(
			ValueChangeEvent<Boolean> event) {
		
		txtHeight.setText("");
		txtWidth.setText("");
		
		if(event.getValue() == true) {
			// change to predefined width and height
			chkChangeSize.setText(setWidthHeight(constants.changeToSize(),originalWidth,originalHeight));
			lblCurrentSize.setText(setWidthHeight(constants.currentSize(),predefinedWidth,predefinedHeight));
			currentWidth = predefinedWidth;
			currentHeight = predefinedHeight;
			setImageAndCanvasSize(predefinedWidth,predefinedHeight);
			
		}else {
			// change to original width and height
			chkChangeSize.setText(setWidthHeight(constants.changeToSize(),predefinedWidth,predefinedHeight));
			lblCurrentSize.setText(setWidthHeight(constants.currentSize(),originalWidth,originalHeight));
			currentWidth = originalWidth;
			currentHeight = originalHeight;
			setImageAndCanvasSize(originalWidth,originalHeight);
		}
				
	}*/

	private void setImageAndCanvasSize(int width,int height) {
		image.setHeight(height);
		image.setWidth(width);
		drawingArea.setHeight(height);
		drawingArea.setWidth(width);
	}
		
	private void init() {
		
		Log.info("Init method called ");
		Log.info("Image path : " + url);
		Image tempImage = new Image(url);
		
		originalWidth = tempImage.getWidth();
		originalHeight = tempImage.getHeight();
		
//		lblCurrentSize.setText(setWidthHeight(constants.currentSize(),originalWidth,originalHeight));
		
		drawingArea.setHeight(originalHeight);
		drawingArea.setWidth(originalWidth);
		
		image = new org.vaadin.gwtgraphics.client.Image(
				0, 0, originalWidth ,originalHeight, url);
		
		drawingArea.add(image);
		
	}

	private final String setWidthHeight(String size, int width, int height) {	
		return size.replace("(0)", width +"").replace("(1)", height + "");
	}
	
	public String getImageUrl() {
		return url;
	}

	public void renderImage() {
		Log.info("Rendering image");
		init();
	}
	public void rerenderImage() {
		image.setHref(url);
		init();
	}
	
	/*public Request<String> saveImage(McAppRequestFactory  request) {
		
		String size = String.valueOf(currentWidth).concat(",").concat(String.valueOf(currentHeight));
		
		String tempUrl = url.replace(GWT.getHostPageBaseURL(), "");
		Log.info("URL is "+ tempUrl);
		return request.questionRequest().saveQuestionImage(tempUrl,size);
	}*/


//	@Override
	public void setUrl(String url,QuestionTypes questionType) {
		Log.info("Set url : " + url);
		this.url = url;
		
		if(questionType != null && QuestionTypes.Textual.equals(questionType)) {
			renderTextualImage();
		}else {
			renderImage();	
		}
	}


	private void renderTextualImage() {
		
		Log.info("Init method called ");
		Log.info("Image path : " + url);
		
		originalWidth = 480;
		originalHeight = 480;
		
//		lblCurrentSize.setText(setWidthHeight(constants.currentSize(),originalWidth,originalHeight));
		
		drawingArea.setHeight(originalHeight);
		drawingArea.setWidth(originalWidth);
		
		image = new org.vaadin.gwtgraphics.client.Image(
				0, 0, originalWidth ,originalHeight, url);
		
		drawingArea.add(image);
		
	}


	public String getImageRelativeUrl() {
		
		if(getImageUrl() == null) {
			return null;
		}
			
		return getImageUrl().replace(GWT.getHostPageBaseURL(), "");
	}


	public void closed() {
		Log.info("Image viewer is closed");
		
	}

}
