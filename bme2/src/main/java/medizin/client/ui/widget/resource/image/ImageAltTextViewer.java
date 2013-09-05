package medizin.client.ui.widget.resource.image;

import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class ImageAltTextViewer extends Composite {

	private final HorizontalPanel mainPanel = new HorizontalPanel();
	
	private final Image image = new Image();
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private void addStyle(){
		mainPanel.addStyleName("imageAltTextViewerVpCss");
		image.addStyleName("imageAltText");
	}
	
	public ImageAltTextViewer() {
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		image.setAltText(constants.sorryImageNotAvailable());		
		mainPanel.add(image);		
		initWidget(mainPanel);
		addStyle();
	}
	
	public ImageAltTextViewer(String msg) {
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		image.setAltText(msg);		
		mainPanel.add(image);		
		initWidget(mainPanel);
		addStyle();
	}
}
