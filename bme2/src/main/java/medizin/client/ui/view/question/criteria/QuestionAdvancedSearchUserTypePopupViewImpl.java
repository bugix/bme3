package medizin.client.ui.view.question.criteria;

import java.util.Arrays;
import java.util.List;

import medizin.client.proxy.PersonProxy;
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

public class QuestionAdvancedSearchUserTypePopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl{

	private static QuestionAdvancedSearchUserTypePopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchUserTypePopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchUserTypePopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchUserTypePopupViewImpl> {
	}
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<PossibleFields> possibleFieldListBox = new ValueListBox<PossibleFields>(new CriteriaEnumRenderer<PossibleFields>(CriteriaEnumRenderer.Type.USER_TYPE));
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.USERTYPE));
	
	@UiField
	DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> personSuggestBox;
	
	@UiField
	IconButton addUserTypeButton;
	
	@UiField
	IconButton closeButton;

	public QuestionAdvancedSearchUserTypePopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		possibleFieldListBox.setValue(PossibleFields.AUTHOR);
		possibleFieldListBox.setAcceptableValues(Arrays.asList(new PossibleFields[]{PossibleFields.AUTHOR, PossibleFields.REVIEWER}));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		addUserTypeButton.setText(constants.add());
	}

	public void setPersonSuggsetBoxValue(List<PersonProxy> personProxyList)
	{
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) personSuggestBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(personProxyList);
		personSuggestBox.setSuggestOracle(suggestOracle1);
		
		personSuggestBox.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				return object == null ? "" : (object.getName() + " " + object.getPrename());					
			}
		});
	}
	
	@UiHandler("addUserTypeButton")
	public void addKeywordButtonClicked(ClickEvent event)
	{
		if (validateField())
		{
			this.hide();
			AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
			criteria.setPossibleFields(possibleFieldListBox.getValue());
			criteria.setComparison(comparison.getValue());
			criteria.setBindType(bindType.getValue());
			criteria.setValue(personSuggestBox.getText().replaceAll(" ", ""));
			
			String shownValue = ""; 
			if (comparison.getValue().equals(Comparison.EQUALS))
				shownValue = constants.question() + " " + possibleFieldListBox.getValue().toString().toLowerCase() + " " + enumConstants.USERTYPE_EQUALS() + " " + personSuggestBox.getText();
			else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
				shownValue = constants.question() + " " + possibleFieldListBox.getValue().toString().toLowerCase() + " " + enumConstants.USERTYPE_NOT_EQUALS() + " " + personSuggestBox.getText();
				
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
		if (personSuggestBox.getText().isEmpty())
		{
			personSuggestBox.getTextField().getAdvancedTextBox().addStyleName("higlight_onViolation");
			return false;
		}
		else	
		{
			personSuggestBox.getTextField().getAdvancedTextBox().removeStyleName("higlight_onViolation");
			return true;
		}		
	}
}
