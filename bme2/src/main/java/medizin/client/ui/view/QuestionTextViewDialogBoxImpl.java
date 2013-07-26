package medizin.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionTextViewDialogBoxImpl extends PopupPanel {

	private static QuestionTextViewDialogBoxImplUiBinder uiBinder = GWT
			.create(QuestionTextViewDialogBoxImplUiBinder.class);

	interface QuestionTextViewDialogBoxImplUiBinder extends
			UiBinder<Widget, QuestionTextViewDialogBoxImpl> {
	}
	
	@UiField
	public HTML questionHtmlText;

	public QuestionTextViewDialogBoxImpl() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setGlassEnabled(false);
		getElement().addClassName("questionViewPopupPanel");
	}

	public void setHtmlText(String text)
	{
		questionHtmlText.setHTML(new SafeHtmlBuilder().appendHtmlConstant(text).toSafeHtml());
	}
	
	public void setLeftTopPosition(int leftValue, int topValue)
	{
		getElement().getStyle().setLeft(leftValue, Unit.PX);
		getElement().getStyle().setTop(topValue, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}
}
