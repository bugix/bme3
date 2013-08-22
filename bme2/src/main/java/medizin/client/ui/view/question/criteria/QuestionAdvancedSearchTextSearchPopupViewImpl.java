package medizin.client.ui.view.question.criteria;

import java.util.Arrays;

import medizin.client.ui.view.renderer.CriteriaEnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.BindType;
import medizin.shared.criteria.Comparison;
import medizin.shared.criteria.PossibleFields;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeEnumConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAdvancedSearchTextSearchPopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl{

	private static QuestionAdvancedSearchTextSearchPopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchTextSearchPopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchTextSearchPopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchTextSearchPopupViewImpl> {
	}
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.TEXT_SEARCH));
	
	@UiField(provided = true)
	ValueListBox<PossibleFields> searchColumn = new ValueListBox<PossibleFields>(new CriteriaEnumRenderer<PossibleFields>());
	
	@UiField
	TextBox searchText;
	
	@UiField
	IconButton addTextSearchButton;
	
	@UiField
	IconButton closeButton;

	public QuestionAdvancedSearchTextSearchPopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		searchColumn.setValue(PossibleFields.COMMENT);
		searchColumn.setAcceptableValues(Arrays.asList(new PossibleFields[]{PossibleFields.COMMENT, PossibleFields.ANSWER_TEXT, PossibleFields.QUESTION_TEXT, PossibleFields.QUESTION_SHORT_NAME}));
		
		addTextSearchButton.setText(constants.add());
	}

	@UiHandler("addTextSearchButton")
	public void addKeywordButtonClicked(ClickEvent event)
	{
		if (validateField())
		{
			this.hide();
			AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
			criteria.setPossibleFields(searchColumn.getValue());
			criteria.setComparison(comparison.getValue());
			criteria.setBindType(bindType.getValue());
			criteria.setValue(searchText.getText());
			
			String shownValue = "";
			if (comparison.getValue().equals(Comparison.EQUALS))
				shownValue = enumConstants.getString(searchColumn.getValue().toString()) + " " + enumConstants.TEXT_SEARCH_EQUALS().toLowerCase() + " " + searchText.getText();
			else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
				shownValue = enumConstants.getString(searchColumn.getValue().toString()) + " " + enumConstants.TEXT_SEARCH_NOT_EQUALS().toLowerCase() + " " + searchText.getText();
			
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
		if (searchText.getText().isEmpty())
		{
			searchText.addStyleName("higlight_onViolation");
			return false;
		}
		else	
		{
			searchText.removeStyleName("higlight_onViolation");
			return true;
		}	
	}
}
