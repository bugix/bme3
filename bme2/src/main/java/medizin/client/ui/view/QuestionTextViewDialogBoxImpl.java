package medizin.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class QuestionTextViewDialogBoxImpl extends DialogBox {

	private static QuestionTextViewDialogBoxImplUiBinder uiBinder = GWT
			.create(QuestionTextViewDialogBoxImplUiBinder.class);

	interface QuestionTextViewDialogBoxImplUiBinder extends
			UiBinder<Widget, QuestionTextViewDialogBoxImpl> {
	}
	
	/*@UiField
	public HorizontalPanel questionTextHorizontalPanel;*/
	
	@UiField
	public HTML questionHtmlText;

	public QuestionTextViewDialogBoxImpl() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setGlassEnabled(false);
		getElement().addClassName("questionViewDialogBox");
	}

	public void setHtmlText(String text)
	{
		questionHtmlText.setHTML(new SafeHtmlBuilder().appendHtmlConstant(text).toSafeHtml());
	}

	public void addToOffsetWidth(int i) {
		setWidth((questionHtmlText.getParent().getOffsetWidth() + 10) + "px");
	}

}
