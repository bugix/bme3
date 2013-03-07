package medizin.client.ui.widget.resource.video;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import medizin.client.ui.widget.process.AppLoader;
import medizin.client.ui.widget.process.ApplicationLoadingPopupView;

import com.allen_sauer.gwt.log.client.Log;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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

	private final int width = 500;
	private final int height = 500;
	
	public VideoViewer(String description) {
		initWidget(uiBinder.createAndBindUi(this));
		this.url = description;
		
		if (description == null || description.equals(""))
			return;
		
		/*
		if (videoPlayer == null)
			videoPlayer = new VideoWidget(true, true, "");

		description = description + "?date=" + new Date().getTime();
		videoPlayer.addStyleName("videoBorder");
		List<VideoSource> sources = new ArrayList<VideoSource>();

		Log.info("setVideoMediaContent Video Source path" + description);

		sources.add(new VideoSource(description));
		videoPlayer.setSources(sources);
		videoPlayer.setPixelSize(480, 270);

		videoPanel.add(videoPlayer);*/
		
		//spec display video
		if(description == null || description.equals(""))
			return;
		
		//get extension , If it is MP$ than play via flash player else play via HTML5 Player,will work for firefox
		
		String temp[]=description.split("/");
		
		String temp1[]=temp[temp.length-1].split("\\.");
		
		if(temp1[1].equalsIgnoreCase("mp4"))
		{
			playViaFlashPlayer(description);
		}
		else
		{
			playViaHTML5Player(description);
		}
	}
	
	public void playViaHTML5Player(String url)
	{
		videoPanel.clear();
		videoPlayer=null;

		if(videoPlayer == null)
			videoPlayer = new VideoWidget(true, true, "");
 
		url=url + "?date=" + new Date().getTime();
		videoPlayer.addStyleName("videoBorder");
		List<VideoSource> sources = new ArrayList<VideoSource>();
		Log.info("setVideoMediaContent Video Source path" +url);
		sources.add(new VideoSource(url));
		videoPlayer.setSources(sources);
		videoPlayer.setPixelSize(width, height);
		videoPanel.add(videoPlayer);
	}
	
	public void playViaFlashPlayer(String url)
	{
		videoPanel.clear();
		url=url + "?date=" + new Date().getTime();
		
	    Log.info("Base URL :"+  GWT.getHostPageBaseURL());
	    final SimplePanel panel = new SimplePanel();   // create panel to hold the player
	    
		try {
			final AbstractMediaPlayer player  = new FlashMediaPlayer(GWT.getHostPageBaseURL()+url, false);
			panel.setWidget(player); 
			panel.setHeight(height + "px");
			panel.setWidth(width + "px");
			panel.getElement().addClassName("videoSize");
			Log.info("player element"+player.getElement().getParentElement());
			player.addLoadingProgressHandler(new LoadingProgressHandler() {
	
				@Override
				public void onLoadingProgress(LoadingProgressEvent event) {
					Log.info("Progress : " + event.getProgress());
					ApplicationLoadingPopupView.showApplicationLoadingPopup(true);
					if(event.getProgress() == 1.0d)
					{
						try {
							Log.info("player element"+player.getElement().getElementsByTagName("embed").getItem(0));
							AppLoader.setShowLoader(false);	
							player.getElement().getElementsByTagName("embed").getItem(0).getStyle().setHeight(500, Unit.PX);
							ApplicationLoadingPopupView.showApplicationLoadingPopup(false);
							player.playMedia();
						} catch (PlayException e) {
							Log.error("cannot play video");
							Log.error("Error " + e.getMessage());
						}
					}
				}
			});
		} catch(LoadException e) {
		     // catch loading exception and alert user
		     Window.alert("An error occured while loading");
		} catch(PluginVersionException e) {
		     // required plugin version is not available, alert user
		     // possibly providing a link to the plugin download page.
		     panel.setWidget(new HTML(".. some nice message telling the user to download plugin first .."));
		} catch(PluginNotFoundException e) {
		     // catch PluginNotFoundException and display a friendly notice.
		     panel.setWidget(PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer));
		}
		videoPanel.add(panel);
	}
	
	public void closed() {
		Log.info("Video player is closed.");
	}

	public String getURL() {
		return url;
	}

	public String getRelativeURL() {
		return url.replace(GWT.getHostPageBaseURL(),"");
	}

}
