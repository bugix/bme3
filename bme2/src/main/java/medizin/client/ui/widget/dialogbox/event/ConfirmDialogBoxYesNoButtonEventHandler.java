package medizin.client.ui.widget.dialogbox.event;

import com.google.gwt.event.shared.EventHandler;

public interface ConfirmDialogBoxYesNoButtonEventHandler extends EventHandler {

	void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event);
	void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event);
}
