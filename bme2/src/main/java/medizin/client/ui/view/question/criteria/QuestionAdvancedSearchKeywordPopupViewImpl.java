package medizin.client.ui.view.question.criteria;

import java.util.Arrays;
import java.util.List;

import medizin.client.proxy.KeywordProxy;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAdvancedSearchKeywordPopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl{

	private static QuestionAdvancedSearchKeywordPopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchKeywordPopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchKeywordPopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchKeywordPopupViewImpl> {
	}
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.KEYWORD));
	
	@UiField
	DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> keywordSuggestBox;
	
	@UiField
	IconButton addKeywordButton;
	
	@UiField
	IconButton closeButton;
	
	@UiField
	HorizontalPanel parentPanel;
		
	public QuestionAdvancedSearchKeywordPopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		addKeywordButton.setText(constants.add());
	}
	
	public void setKeywordSuggsetBoxValue(List<KeywordProxy> keywordProxyList)
	{
		DefaultSuggestOracle<KeywordProxy> suggestOracle1 = (DefaultSuggestOracle<KeywordProxy>) keywordSuggestBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(keywordProxyList);
		keywordSuggestBox.setSuggestOracle(suggestOracle1);
		
		keywordSuggestBox.setRenderer(new AbstractRenderer<KeywordProxy>() {

			@Override
			public String render(KeywordProxy object) {
				return object == null ? "" : object.getName();					
			}
		});
	}
	
	@UiHandler("addKeywordButton")
	public void addKeywordButtonClicked(ClickEvent event)
	{
		if (validateField())
		{
			this.hide();
			AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
			criteria.setPossibleFields(PossibleFields.KEYWORD);
			criteria.setComparison(comparison.getValue());
			criteria.setBindType(bindType.getValue());
			criteria.setValue(keywordSuggestBox.getText());
			
			String shownValue = "";
			if (comparison.getValue().equals(Comparison.EQUALS))
				shownValue = constants.question() + " " + enumConstants.KEYWORD_EQUALS().toLowerCase() + " " + keywordSuggestBox.getText();
			else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
				shownValue = constants.question() + " " + enumConstants.KEYWORD_NOT_EQUALS().toLowerCase() + " " + keywordSuggestBox.getText();
			
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
		if (keywordSuggestBox.getText().isEmpty())
		{
			keywordSuggestBox.getTextField().getAdvancedTextBox().addStyleName("higlight_onViolation");
			return false;
		}
		else	
		{
			keywordSuggestBox.getTextField().getAdvancedTextBox().removeStyleName("higlight_onViolation");
			return true;
		}
	}
}
