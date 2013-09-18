package medizin.client.ui.view.question;

import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionFilterViewImpl extends PopupPanel{

	private static QuestionFilterPopupUiBinder uiBinder = GWT
			.create(QuestionFilterPopupUiBinder.class);

	interface QuestionFilterPopupUiBinder extends
			UiBinder<Widget, QuestionFilterViewImpl> {
	}

	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField
	CheckBox questionText;
	
	@UiField
	CheckBox instructionText;
	
	@UiField
	CheckBox keywordText;
	
	@UiField
	CheckBox showNew;

	@UiField
	IconButton resetButton;
	
	@UiField 
	Label labelSearch;
	
	@UiField
	HorizontalPanel labelPanel;
	
	@UiField
	HorizontalPanel buttonPanel;
	
	@UiField
	HorizontalPanel mainPanel;
	
	@UiHandler("resetButton")
	void onClick(ClickEvent e) {
		questionText.setValue(false);
		instructionText.setValue(false);
		keywordText.setValue(false);
		showNew.setValue(false);
	}

	public QuestionFilterViewImpl() {
		super(true);

		add(uiBinder.createAndBindUi(this));
		labelSearch.setText(constants.searchFor());
		labelSearch.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		labelPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		/*setSize("235px", "100px");*/
	}

	public CheckBox getQuestionText() {
		return questionText;
	}

	public CheckBox getInstructionText() {
		return instructionText;
	}

	public CheckBox getKeywordText() {
		return keywordText;
	}

	public CheckBox getShowNew() {
		return showNew;
	}
}
