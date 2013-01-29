package medizin.client.ui.view.question;

import medizin.client.ui.view.question.QuestionEditView.Delegate;
import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmQuestionChangesPopup extends DialogBox {

	private static ConfirmQuestionChangesPopupUiBinder uiBinder = GWT
			.create(ConfirmQuestionChangesPopupUiBinder.class);

	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	interface ConfirmQuestionChangesPopupUiBinder extends
			UiBinder<Widget, ConfirmQuestionChangesPopup> {
	}

	private Delegate delegate;
	
	public ConfirmQuestionChangesPopup(Delegate delegate) {
		
		setWidget(uiBinder.createAndBindUi(this));
		this.delegate = delegate;
		setWidth("400px");
		center();
		setGlassEnabled(true);
		setModal(true);
		//setTitle(constants.confirmText());
		setText(constants.confirmText());
		
	}


@UiField
IconButton saveChange;

@UiField
IconButton saveNew;


@UiHandler ("saveNew")
void saveNewClicked (ClickEvent event){
	delegate.saveClicked(false);
	super.hide();
}

@UiHandler ("saveChange")
void saveChangesClicked (ClickEvent event){
	delegate.saveClicked(true);
	super.hide();
}


}
