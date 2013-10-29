package medizin.client.ui.view.question;

import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmQuestionChangesPopup extends DialogBox {

	private static ConfirmQuestionChangesPopupUiBinder uiBinder = GWT.create(ConfirmQuestionChangesPopupUiBinder.class);

	private final BmeConstants constants = GWT.create(BmeConstants.class);

	private final ConfirmQuestionHandler handler;
	
	interface ConfirmQuestionChangesPopupUiBinder extends UiBinder<Widget, ConfirmQuestionChangesPopup> {}
	
	public ConfirmQuestionChangesPopup(ConfirmQuestionHandler handler, boolean isAdminOrInstitutionAdmin) {
		
		setWidget(uiBinder.createAndBindUi(this));
		setWidth("400px");
		center();
		setGlassEnabled(true);
		setModal(true);
		//setTitle(constants.confirmText());
		setText(constants.confirmText());
		this.handler = handler;		
		
		if(isAdminOrInstitutionAdmin == false) {
			forceActiveChkBox.removeFromParent();
		}
	}


	@UiField
	IconButton saveChange;
	
	@UiField
	IconButton saveNew;
	
	@UiField
	Button cancel;
	
	@UiField
	CheckBox forceActiveChkBox;
	
	@UiHandler ("cancel")
	void cancelClicked (ClickEvent event){
		Log.info("cancel click");
		super.hide();
	}
	
	
	@UiHandler ("saveNew")
	void saveNewClicked (ClickEvent event){
		handler.confirmQuestionClicked(true, forceActiveChkBox.getValue());
		super.hide();
	}
	
	@UiHandler ("saveChange")
	void saveChangesClicked (ClickEvent event){
		handler.confirmQuestionClicked(false, forceActiveChkBox.getValue());
		super.hide();
	}


	public interface ConfirmQuestionHandler {
		
		void confirmQuestionClicked(boolean isWithNewMajorVersion,boolean isForceActive);
		
	}
}
