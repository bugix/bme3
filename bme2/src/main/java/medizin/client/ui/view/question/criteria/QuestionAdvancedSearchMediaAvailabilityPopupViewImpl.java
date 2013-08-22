package medizin.client.ui.view.question.criteria;

import java.util.Arrays;

import medizin.client.proxy.KeywordProxy;
import medizin.client.ui.view.renderer.CriteriaEnumRenderer;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.shared.MultimediaType;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAdvancedSearchMediaAvailabilityPopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl {

	private static QuestionAdvancedSearchMediaAvailabilityPopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchMediaAvailabilityPopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchMediaAvailabilityPopupViewImplUiBinder
			extends
			UiBinder<Widget, QuestionAdvancedSearchMediaAvailabilityPopupViewImpl> {
	}

	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.MEDIA_AVAILABILITY));
	
	@UiField (provided = true)
	ValueListBox<MultimediaType> mediaListBox = new ValueListBox<MultimediaType>(new EnumRenderer<MultimediaType>());
	
	@UiField
	IconButton addMediaAvailablityButton;
	
	@UiField
	IconButton closeButton;
	
	public QuestionAdvancedSearchMediaAvailabilityPopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		mediaListBox.setValue(MultimediaType.Image);
		mediaListBox.setAcceptableValues(Arrays.asList(MultimediaType.values()));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		addMediaAvailablityButton.setText(constants.add());
	}
	
	@UiHandler("addMediaAvailablityButton")
	public void addKeywordButtonClicked(ClickEvent event)
	{
		this.hide();
		AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
		criteria.setPossibleFields(PossibleFields.MEDIA_AVAILABILITY);
		criteria.setComparison(comparison.getValue());
		criteria.setBindType(bindType.getValue());
		criteria.setValue(mediaListBox.getValue().toString());
		
		String shownValue = "";
		if (comparison.getValue().equals(Comparison.EQUALS))
			shownValue = constants.question() + " " + enumConstants.MEDIA_AVAILABILITY_EQUALS().toLowerCase() + " " + mediaListBox.getValue().toString();
		else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
			shownValue = constants.question() + " " + enumConstants.MEDIA_AVAILABILITY_NOT_EQUALS().toLowerCase() + " " + mediaListBox.getValue().toString();
		
		criteria.setShownValue(shownValue);
		delegate.advancedSearchCriteriaClicked(criteria);
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event)
	{
		this.hide();
	}

	@Override
	public boolean validateField() {
		return false;
	}
}
