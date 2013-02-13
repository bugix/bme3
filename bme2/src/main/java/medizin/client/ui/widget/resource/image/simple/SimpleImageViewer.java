package medizin.client.ui.widget.resource.image.simple;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class SimpleImageViewer extends Composite {

	private static SimpleImageViewerUiBinder uiBinder = GWT
			.create(SimpleImageViewerUiBinder.class);

	interface SimpleImageViewerUiBinder extends
			UiBinder<Widget, SimpleImageViewer> {
	}

	@UiField
	Image image;
	
	private String url;
	
	public SimpleImageViewer(final String url) {
		initWidget(uiBinder.createAndBindUi(this));
		this.url = url;
		this.image.setUrl(new SafeUri() {
			
			@Override
			public String asString() {
				return url;
			}
		});
	}

	public String getURL() {
		return url;
	}
	
	public void setWidth(String width) {
		if(image != null) {
			
		}
	}
}
