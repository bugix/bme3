package medizin.client.ui.widget.resource.audio;

import com.chj.gwt.client.soundmanager2.Callback;

public interface SoundFactory {

	public interface PlayingCallback {
		void fire(int now, int end);
	}

	public void playSong(String song, Callback callback);

	public void stopPlaying();

	public boolean isSoundPlaying();

	public void pausePlaying();

	public void resumePlaying();

	public void setPlayingCallback(final PlayingCallback playing);

	public void onStartup(Callback callback);

	public void onStartError(Callback callback);

}