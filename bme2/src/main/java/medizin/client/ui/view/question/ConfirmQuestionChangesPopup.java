package medizin.client.ui.view.question;

import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmQuestionChangesPopup extends DialogBox {

	private static ConfirmQuestionChangesPopupUiBinder uiBinder = GWT
			.create(ConfirmQuestionChangesPopupUiBinder.class);

	private final BmeConstants constants = GWT.create(BmeConstants.class);

	private final Function<Boolean, Void> withNewMajorVersion;
	
	interface ConfirmQuestionChangesPopupUiBinder extends
			UiBinder<Widget, ConfirmQuestionChangesPopup> {
	}

	/*private Delegate delegate;*/
	
	public ConfirmQuestionChangesPopup(Function<Boolean, Void> function/*Delegate delegate*/) {
		
		setWidget(uiBinder.createAndBindUi(this));
		/*this.delegate = delegate;*/
		setWidth("400px");
		center();
		setGlassEnabled(true);
		setModal(true);
		//setTitle(constants.confirmText());
		setText(constants.confirmText());
		cancel.setText(constants.cancel());
		this.withNewMajorVersion = function;		
	}


	@UiField
	IconButton saveChange;
	
	@UiField
	IconButton saveNew;
	
	@UiField
	Button cancel;
	
	
	@UiHandler ("cancel")
	void cancelClicked (ClickEvent event){
		Log.info("cancel click");
		super.hide();
	}
	
	
	@UiHandler ("saveNew")
	void saveNewClicked (ClickEvent event){
		withNewMajorVersion.apply(true);
		//delegate.saveClicked(false);
		super.hide();
	}
	
	@UiHandler ("saveChange")
	void saveChangesClicked (ClickEvent event){
		withNewMajorVersion.apply(false);
		//delegate.saveClicked(true);
		super.hide();
	}


}
