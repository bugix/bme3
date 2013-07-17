package medizin.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionTextViewDialogBoxImpl extends DialogBox {

	private static QuestionTextViewDialogBoxImplUiBinder uiBinder = GWT
			.create(QuestionTextViewDialogBoxImplUiBinder.class);

	interface QuestionTextViewDialogBoxImplUiBinder extends
			UiBinder<Widget, QuestionTextViewDialogBoxImpl> {
	}
	
	@UiField
	public HorizontalPanel questionTextHorizontalPanel;

	public QuestionTextViewDialogBoxImpl() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setGlassEnabled(false);
		getElement().addClassName("questionViewDialogBox");
	}


}
