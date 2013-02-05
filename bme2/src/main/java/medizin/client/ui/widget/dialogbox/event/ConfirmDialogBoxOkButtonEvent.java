package medizin.client.ui.widget.dialogbox.event;

import com.google.gwt.event.shared.GwtEvent;

public class ConfirmDialogBoxOkButtonEvent extends GwtEvent<ConfirmDialogBoxOkButtonEventHandler> {

	public ConfirmDialogBoxOkButtonEvent() {
		
	}
	public static Type<ConfirmDialogBoxOkButtonEventHandler> TYPE = new Type<ConfirmDialogBoxOkButtonEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ConfirmDialogBoxOkButtonEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ConfirmDialogBoxOkButtonEventHandler handler) {
		handler.onOkButtonClicked(this);		
	}
}
