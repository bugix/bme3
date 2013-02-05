package medizin.client.ui.widget.dialogbox.event;

import com.google.gwt.event.shared.GwtEvent;

public class ConfirmDialogBoxYesNoButtonEvent extends GwtEvent<ConfirmDialogBoxYesNoButtonEventHandler> {

	private final Boolean flag;
	public ConfirmDialogBoxYesNoButtonEvent(Boolean flag) {
		this.flag = flag;
	}
	public static Type<ConfirmDialogBoxYesNoButtonEventHandler> TYPE = new Type<ConfirmDialogBoxYesNoButtonEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ConfirmDialogBoxYesNoButtonEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ConfirmDialogBoxYesNoButtonEventHandler handler) {
		if (flag)
			handler.onYesButtonClicked(this);
		else 
			handler.onNoButtonClicked(this);
	}

	public Boolean getFlag() {
		return flag;
	}	
}
