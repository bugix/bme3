package medizin.client.ui.view.question.criteria;

import java.util.Arrays;
import java.util.List;

import medizin.client.proxy.QuestionEventProxy;
import medizin.client.ui.view.renderer.CriteriaEnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.BindType;
import medizin.shared.criteria.Comparison;
import medizin.shared.criteria.PossibleFields;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeEnumConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAdvancedSearchQuestionEventPopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl {

	private static QuestionAdvancedSearchQuestionEventPopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchQuestionEventPopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchQuestionEventPopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchQuestionEventPopupViewImpl> {
	}
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.QUESTION_EVENT));
	
	@UiField
	DefaultSuggestBox<QuestionEventProxy, EventHandlingValueHolderItem<QuestionEventProxy>> questionEventSuggestBox;
	
	@UiField
	IconButton addQuestionEventButton;
	
	@UiField
	IconButton closeButton;

	public QuestionAdvancedSearchQuestionEventPopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		addQuestionEventButton.setText(constants.add());
	}
	
	public void setQuestionEventSuggsetBoxValue(List<QuestionEventProxy> questionEventProxyList)
	{
		DefaultSuggestOracle<QuestionEventProxy> suggestOracle1 = (DefaultSuggestOracle<QuestionEventProxy>) questionEventSuggestBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(questionEventProxyList);
		questionEventSuggestBox.setSuggestOracle(suggestOracle1);
		
		questionEventSuggestBox.setRenderer(new AbstractRenderer<QuestionEventProxy>() {

			@Override
			public String render(QuestionEventProxy object) {
				return object == null ? "" : object.getEventName();					
			}
		});
	}
	
	@UiHandler("addQuestionEventButton")
	public void addQuestionEventButtonClicked(ClickEvent event)
	{
		if (validateField())
		{
			this.hide();
			AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
			criteria.setPossibleFields(PossibleFields.QUESTION_EVENT);
			criteria.setComparison(comparison.getValue());
			criteria.setBindType(bindType.getValue());
			criteria.setValue(questionEventSuggestBox.getText());
			
			String shownValue = "";
			if (comparison.getValue().equals(Comparison.EQUALS))
				shownValue = constants.question() + " " + enumConstants.QUESTION_EVENT_EQUALS().toLowerCase() + " " + questionEventSuggestBox.getText();
			else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
				shownValue = constants.question() + " " + enumConstants.QUESTION_EVENT_NOT_EQUALS().toLowerCase() + " " + questionEventSuggestBox.getText();
			
			criteria.setShownValue(shownValue);
				
			delegate.advancedSearchCriteriaClicked(criteria);
		}
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event)
	{
		this.hide();
	}

	@Override
	public boolean validateField() {
		if (questionEventSuggestBox.getText().isEmpty())
		{
			questionEventSuggestBox.getTextField().getAdvancedTextBox().addStyleName("higlight_onViolation");
			return false;
		}
		else	
		{
			questionEventSuggestBox.getTextField().getAdvancedTextBox().removeStyleName("higlight_onViolation");
			return true;
		}	
	}
}
