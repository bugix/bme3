package medizin.client.ui.widget.resource.video;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

public class VideoViewer extends Composite {

	private static VideoViewerUiBinder uiBinder = GWT
			.create(VideoViewerUiBinder.class);

	interface VideoViewerUiBinder extends UiBinder<Widget, VideoViewer> {
	}

	@UiField
	public HorizontalPanel videoPanel;

	VideoWidget videoPlayer = null;

	private final String url;

	public VideoViewer(String description) {
		initWidget(uiBinder.createAndBindUi(this));
		this.url = description;
		
		if (description == null || description.equals(""))
			return;
		if (videoPlayer == null)
			videoPlayer = new VideoWidget(true, true, "");

		description = description + "?date=" + new Date().getTime();
		videoPlayer.addStyleName("videoBorder");
		List<VideoSource> sources = new ArrayList<VideoSource>();

		Log.info("setVideoMediaContent Video Source path" + description);

		sources.add(new VideoSource(description));
		videoPlayer.setSources(sources);
		videoPlayer.setPixelSize(480, 270);

		videoPanel.add(videoPlayer);
	}
	
	public void closed() {
		Log.info("Video player is closed.");
	}

	public String getURL() {
		return url;
	}

}
