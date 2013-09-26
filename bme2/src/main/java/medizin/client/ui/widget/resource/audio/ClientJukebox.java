package medizin.client.ui.widget.resource.audio;
import java.util.ArrayList;

import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.chj.gwt.client.soundmanager2.Callback;
import com.google.gwt.core.client.GWT;

public class ClientJukebox {

	public static BmeConstants constants = GWT.create(BmeConstants.class);
	
	/* For actually playing music and songs */
	private SoundFactory soundFactory;

	final AudioViewer view;

	/* The queue of requests */
	private ArrayList<String> jukeboxQueue = new ArrayList<String>();

	private String lastPlayed = null;

	Boolean isPaused = false;
	
	/* These should be injected with something like GIN */
	public ClientJukebox(SoundFactory soundFactory, final AudioViewer view){
		this.soundFactory = soundFactory;
		this.view = view;
		soundFactory.onStartup(new Callback(){

			public void execute() {
				view.setButtonsEnabled();
			}

		});
		soundFactory.onStartError(new Callback(){

			public void execute() {
				ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.soundManagerLoadingError());
				/*ErrorPanel errorPanel = new ErrorPanel();
				errorPanel.setErrorMessage("SoundManager Encountered an error while loading");*/
			}
		});

		soundFactory.setPlayingCallback(new medizin.client.ui.widget.resource.audio.SoundFactory.PlayingCallback(){

			public void fire(int now, int end) {
				String total = ""+end/60+":"+end%60;
				String current= ""+now/60+":"+now%60;
				Log.info("Text : " + lastPlayed+"  - "+current+" /"+total);
				view.getInfo().setText(lastPlayed+"  - "+current+" /"+total);
			}

		});
	}


	public void stopPlaying(){
		soundFactory.stopPlaying();
		isPaused = false;
		view.getPlayButton().setIcon("play");
		view.getInfo().setText(lastPlayed);
	}

	public void pausePlaying(){
		if(isPaused) {
			resumePlaying();
		}
		else {
			soundFactory.pausePlaying();
			isPaused = true;
			view.getPlayButton().setIcon("play");
		}
	}

	public void resumePlaying(){
		soundFactory.resumePlaying();
		isPaused = false;
		view.getPlayButton().setIcon("pause");
	}

	public boolean nextSong() {
		if (jukeboxQueue.isEmpty()) {

			/* Check if we've already stopped playing */
			stopPlaying();
			return false;
		}

		/* Pop the next song */
		String next = jukeboxQueue.remove(0);
		playRequest(next);

		/* Creates a circular queue */
		queueRequest(next);

		return true;
	}


	private void playRequest(final String request) {
		lastPlayed = ClientUtility.getFileName(request, MultimediaType.Sound);

		/* Create a callback */
		soundFactory.playSong(request, new Callback(){

			public void execute() {
				nextSong();
			}

		});

		isPaused = false;
		Log.info("Text : " + lastPlayed);
		view.getInfo().setText(lastPlayed);
		view.getPlayButton().setIcon("pause");
	}

	public boolean isSoundPlayin(){
		return soundFactory.isSoundPlaying();
	}

	public boolean queueRequest(String request) {

		if(request == null){
			return false;
		}
		return jukeboxQueue.add(request);
	}

	public ArrayList<String> getJukeboxQueue() {
		return jukeboxQueue;
	}

	public void setJukeboxQueue(ArrayList<String> jukeboxQueue) {
		this.jukeboxQueue = jukeboxQueue;
	}

	public String getLastPlayed() {
		return lastPlayed;
	}

	public void setLastPlayed(String lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	public Boolean getIsPaused() {
		return isPaused;
	}

	public void setIsPaused(Boolean isPaused) {
		this.isPaused = isPaused;
	}	
}