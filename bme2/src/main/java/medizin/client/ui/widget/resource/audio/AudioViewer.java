package medizin.client.ui.widget.resource.audio;

import medizin.client.ui.widget.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AudioViewer extends Composite {

	private static VideoViewerUiBinder uiBinder = GWT
			.create(VideoViewerUiBinder.class);

	interface VideoViewerUiBinder extends UiBinder<Widget, AudioViewer> {
	}

	@UiField
	public HorizontalPanel audioPanel;
	
	@UiField
	IconButton play;
	
	/*@UiField
	IconButton pause;*/
	
	@UiField
	IconButton stop;
	
	@UiField
	Label info;

	private final ClientJukebox clientJukebox;
	private final String url;

	public AudioViewer(final String description) {
		initWidget(uiBinder.createAndBindUi(this));
		this.url = description;
		clientJukebox = new ClientJukebox(new SMSoundFactory(), this);
		clientJukebox.queueRequest(description);
		
		play.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				if(clientJukebox.getIsPaused()){
					clientJukebox.resumePlaying();
				}
				else if(clientJukebox.isSoundPlayin()){
					clientJukebox.pausePlaying();
				}
				else{
					clientJukebox.nextSong();
				}
			}
		});

		stop.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				clientJukebox.stopPlaying();
			}

		});
	}

	public void setButtonsEnabled() {
		play.setEnabled(true);
		//pause.setEnabled(true);
		stop.setEnabled(true);
		
	}

	public Label getInfo() {
		return info;
	}

	public IconButton getPlayButton() {
		return play;
	}

	public void closed() {
		Log.info("Audio player closed");
		clientJukebox.stopPlaying();
	}

	public String getURL() {
		return url;
	}

}
