package medizin.client.ui.view.question.criteria;

import java.util.Arrays;

import medizin.client.ui.view.renderer.CriteriaEnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.BindType;
import medizin.shared.criteria.Comparison;
import medizin.shared.criteria.PossibleFields;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeEnumConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class QuestionAdvancedSearchDatePopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl {

	private static final DateTimeFormat SHORT_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");

	private static QuestionAdvancedSearchDatePopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchDatePopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchDatePopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchDatePopupViewImpl> {
	}

	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>());
	
	@UiField(provided = true)
	ValueListBox<PossibleFields> dateSearchColumn = new ValueListBox<PossibleFields>(new CriteriaEnumRenderer<PossibleFields>());
	
	@UiField
	DateBox dateValue;
	
	@UiField
	IconButton addDateSearchClicked;
	
	@UiField
	IconButton closeButton;
	
	public QuestionAdvancedSearchDatePopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(Comparison.values()));
		
		dateSearchColumn.setValue(PossibleFields.CREATED_QUESTION_DATE);
		dateSearchColumn.setAcceptableValues(Arrays.asList(new PossibleFields[]{PossibleFields.CREATED_QUESTION_DATE, PossibleFields.CHANGED_QUESTION_DATE, PossibleFields.USED_IN_MC_DATE}));
		
		addDateSearchClicked.setText(constants.add());
	}

	@UiHandler("addDateSearchClicked")
	public void addKeywordButtonClicked(ClickEvent event)
	{
		if (validateField())
		{
			this.hide();
			AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
			criteria.setPossibleFields(dateSearchColumn.getValue());
			criteria.setComparison(comparison.getValue());
			criteria.setBindType(bindType.getValue());
			criteria.setValue(SHORT_FORMAT.format(dateValue.getValue()));
			
			String shownDate = SHORT_FORMAT.format(dateValue.getValue());
			
			String shownValue = "";
			if (comparison.getValue().equals(Comparison.EQUALS))
				shownValue = enumConstants.getString(dateSearchColumn.getValue().toString()) + " " + enumConstants.EQUALS() + " " + shownDate;
			else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
				shownValue = enumConstants.getString(dateSearchColumn.getValue().toString()) + " " + enumConstants.NOT_EQUALS() + " " + shownDate;
			else if (comparison.getValue().equals(Comparison.MORE))
				shownValue = enumConstants.getString(dateSearchColumn.getValue().toString()) + " " + enumConstants.MORE() + " " + shownDate;
			else if (comparison.getValue().equals(Comparison.LESS))
				shownValue = enumConstants.getString(dateSearchColumn.getValue().toString()) + " " + enumConstants.LESS() + " " + shownDate;
			
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
		try
		{
			SHORT_FORMAT.format(dateValue.getValue());
			dateValue.removeStyleName("higlight_onViolation");
			return true;
		}
		catch (Exception e)
		{
			dateValue.addStyleName("higlight_onViolation");
			return false;
		}
	}

}
