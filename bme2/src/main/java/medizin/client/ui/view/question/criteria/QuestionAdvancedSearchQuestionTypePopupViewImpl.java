package medizin.client.ui.view.question.criteria;

import java.util.Arrays;

import medizin.client.ui.view.renderer.CriteriaEnumRenderer;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.shared.QuestionTypes;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.BindType;
import medizin.shared.criteria.Comparison;
import medizin.shared.criteria.PossibleFields;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeEnumConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAdvancedSearchQuestionTypePopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl {

	private static QuestionAdvancedSearchQuestionTypePopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchQuestionTypePopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchQuestionTypePopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchQuestionTypePopupViewImpl> {
	}
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.QUESTION_TYPE));
	
	@UiField(provided = true)
	ValueListBox<PossibleFields> searchQuestionType = new ValueListBox<PossibleFields>(new CriteriaEnumRenderer<PossibleFields>());
	
	@UiField (provided = true)
	ValueListBox<QuestionTypes> questionTypelistBox = new ValueListBox<QuestionTypes>(new EnumRenderer<QuestionTypes>()); 
	
	@UiField
	TextBox questionTypeSearchValue;	
	
	@UiField
	IconButton addQuestionTypeClicked;
	
	@UiField
	IconButton closeButton;

	public QuestionAdvancedSearchQuestionTypePopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		searchQuestionType.setValue(PossibleFields.QUESTION_TYPE);
		searchQuestionType.setAcceptableValues(Arrays.asList(new PossibleFields[]{PossibleFields.QUESTION_TYPE, PossibleFields.QUESTION_TYPE_NAME}));
		
		questionTypelistBox.setValue(QuestionTypes.Textual);
		questionTypelistBox.setAcceptableValues(Arrays.asList(QuestionTypes.values()));
		
		addQuestionTypeClicked.setText(constants.add());
			
		searchQuestionType.addValueChangeHandler(new ValueChangeHandler<PossibleFields>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PossibleFields> event) {
				if (event.getValue().equals(PossibleFields.QUESTION_TYPE))
				{
					Document.get().getElementById("questionTypeSearchValueTr").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("questionTypelistBoxTr").getStyle().clearDisplay();
				}
				else if (event.getValue().equals(PossibleFields.QUESTION_TYPE_NAME))
				{
					Document.get().getElementById("questionTypelistBoxTr").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("questionTypeSearchValueTr").getStyle().clearDisplay();
				}
			}
		});
	}

	
	@UiHandler("addQuestionTypeClicked")
	public void addKeywordButtonClicked(ClickEvent event)
	{
		if (validateField())
		{
			this.hide();
			AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
			criteria.setComparison(comparison.getValue());
			criteria.setBindType(bindType.getValue());
			criteria.setPossibleFields(searchQuestionType.getValue());
			
			if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE))
				criteria.setValue(questionTypelistBox.getValue().toString());
			else if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE_NAME))
				criteria.setValue(questionTypeSearchValue.getValue());		
				
			String shownValue = "";
			if (comparison.getValue().equals(Comparison.EQUALS))
			{
				if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE))
					shownValue = constants.question() + " " + enumConstants.QUESTION_TYPE_EQUALS().toLowerCase() + " " + constants.questionType() + " " + questionTypelistBox.getValue().toString().toLowerCase();
				else if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE_NAME))
					shownValue = constants.question() + " " + enumConstants.QUESTION_TYPE_EQUALS().toLowerCase() + " " + constants.questionType() + " " + questionTypeSearchValue.getValue();			
			}
			else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
			{
				if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE))
					shownValue = constants.question() + " " + enumConstants.QUESTION_TYPE_NOT_EQUALS().toLowerCase() + " " + constants.questionType() + " " + questionTypelistBox.getValue().toString().toLowerCase();
				else if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE_NAME))
					shownValue = constants.question() + " " + enumConstants.QUESTION_TYPE_NOT_EQUALS().toLowerCase() + " " + constants.questionType() + " " + questionTypeSearchValue.getValue();
			}
			
			criteria.setShownValue(shownValue);
			
			delegate.advancedSearchCriteriaClicked(criteria);
		}
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event)
	{
		this.hide();
	}
	
	public void disableSearchTextBox()
	{
		Document.get().getElementById("questionTypeSearchValueTr").getStyle().setDisplay(Display.NONE);
		Document.get().getElementById("questionTypelistBoxTr").getStyle().clearDisplay();		
	}


	@Override
	public boolean validateField() {
		if (searchQuestionType.getValue().equals(PossibleFields.QUESTION_TYPE_NAME))
		{
			if (questionTypeSearchValue.getText().isEmpty())
			{
				questionTypeSearchValue.addStyleName("higlight_onViolation");
				return false;
			}
			else	
			{
				questionTypeSearchValue.removeStyleName("higlight_onViolation");
				return true;
			}
		}
		else
		{
			return true;
		}			
	}
}
