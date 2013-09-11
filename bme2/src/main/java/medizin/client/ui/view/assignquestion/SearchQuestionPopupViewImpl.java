package medizin.client.ui.view.assignquestion;

import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchQuestionPopupViewImpl  extends PopupPanel implements SearchQuestionPopupView{

	BmeConstants constants = GWT.create(BmeConstants.class);
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, SearchQuestionPopupViewImpl> {
	}

	private Delegate delegate;

	private Presenter presenter;
	
	@UiField
	Label questionShortNameLbl;
	
	public Label getQuestionShortNameLbl() {
		return questionShortNameLbl;
	}

	public TextBox getQuestionShortNameTxt() {
		return questionShortNameTxt;
	}

	public Label getQuestionIdLbl() {
		return questionIdLbl;
	}

	public IntegerBox getQuestionIdTxt() {
		return questionIdTxt;
	}

	public Label getQuestionTypeLbl() {
		return questionTypeLbl;
	}

	public TextBox getQuestionTypeTxt() {
		return questionTypeTxt;
	}

	public Button getSearchBtn() {
		return searchBtn;
	}

	public Button getResetBtn() {
		return resetBtn;
	}

	@UiField
	TextBox questionShortNameTxt;
	
	@UiField
	Label questionIdLbl;
	
	@UiField
	IntegerBox questionIdTxt;
	
	@UiField
	Label questionTypeLbl;
	
	@UiField
	TextBox questionTypeTxt;
	
	@UiField
	Button searchBtn;
	
	@UiField
	Button resetBtn;
	
	
	public SearchQuestionPopupViewImpl()
	{
		super(false,true);
		
		add(BINDER.createAndBindUi(this));
		
		questionShortNameLbl.setText(constants.questionName());
		questionIdLbl.setText(constants.questionId());
		questionTypeLbl.setText(constants.questionType());
		searchBtn.setText(constants.searchField());
		resetBtn.setText(constants.reset());
		
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("resetBtn")
	public void reset(ClickEvent event)
	{
		reset();
	}
	
	public void reset()
	{
		questionShortNameTxt.setText("");
		questionIdTxt.setText("");
		questionTypeTxt.setText("");
	}
	
	
	
}
