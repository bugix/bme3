package medizin.client.ui.widget.resource.image;

import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ImageViewer extends Composite {

	public static final int TEXTUAL_IMAGE_HEIGHT = 480;
	public static final int TEXTUAL_IMAGE_WIDTH = 480;

	private static ImageViewerUiBinder uiBinder = GWT.create(ImageViewerUiBinder.class);

	private String url = null;

	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	interface ImageViewerUiBinder extends UiBinder<Widget, ImageViewer> {
	}

	@UiField
	Image image;
	
	public ImageViewer() {
		initWidget(uiBinder.createAndBindUi(this));
	}
		
	private void init(Integer width,Integer height) {
		Log.info("Init method called ");
		Log.info("Image path : " + url);
		
		image.setUrl(url);
//		if(height != null) {
//			image.setHeight(height + "px");	
//		}
//		
//		if(width != null) {
//			image.setWidth(width + "px");	
//		}
	}

	public String getImageUrl() {
		return url;
	}

	private void renderImage(Integer width, Integer height) {
		Log.info("Rendering image");
		init(width,height);
	}
	
	public void setUrl(String url,Integer width, Integer height, QuestionTypes questionType) {
		Log.info("Set url : " + url);
		this.url = url;
		renderImage(width,height);
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

	public void clear() {
		image.setUrl("");
	}

	public Integer getHeight() {
		if(image == null) {
			return null;
		}
		return image.getHeight();
	}

	public Integer getWidth() {
		if(image == null) {
			return null;
		}
		return image.getWidth();
	}

	public void setUrl(String url) {
		image.setUrl(url);
				
		//TODO: set a more sensible alt text and display the message if there really is no image available
		image.setAltText(constants.sorryImageNotAvailable());
	}
	
	public void setPixelSize(int width, int height) {
		image.setPixelSize(width, height);
	}
	
	public void setSizeByWidth(int width) {
		setPixelSize(width, image.getHeight() * width / image.getWidth());
	}
	
	public void setSizeByHeight(int height) {
		setPixelSize(image.getWidth() * height / image.getHeight(), height);
	}
}
