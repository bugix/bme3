package medizin.client.ui.view.question.criteria;

import java.util.Arrays;
import java.util.List;

import medizin.client.proxy.McProxy;
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
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class QuestionAdvancedSearchMCPopupViewImpl extends QuestionAdvancedSearchAbstractPopupViewImpl{

	private static QuestionAdvancedSearchMCPopupViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchMCPopupViewImplUiBinder.class);

	interface QuestionAdvancedSearchMCPopupViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchMCPopupViewImpl> {
	}
	

	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeEnumConstants enumConstants = GWT.create(BmeEnumConstants.class);
	
	@UiField(provided = true)
	ValueListBox<BindType> bindType = new ValueListBox<BindType>(new CriteriaEnumRenderer<BindType>());
	
	@UiField(provided = true)
	ValueListBox<Comparison> comparison = new ValueListBox<Comparison>(new CriteriaEnumRenderer<Comparison>(CriteriaEnumRenderer.Type.MC));
	
	@UiField(provided = true)
	public ValueListBox<McProxy> mcValue=new ValueListBox<McProxy>(new AbstractRenderer<McProxy>() {

		@Override
		public String render(McProxy object) {
			return object==null ?" ": object.getMcName();
		}
	});
		
	@UiField
	IconButton addMcButton;
	
	@UiField
	IconButton closeButton;
	
	public QuestionAdvancedSearchMCPopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		bindType.setValue(BindType.AND);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		
		comparison.setValue(Comparison.EQUALS);
		comparison.setAcceptableValues(Arrays.asList(new Comparison[]{Comparison.EQUALS, Comparison.NOT_EQUALS}));
		
		addMcButton.setText(constants.add());
	}
	
	@UiHandler("addMcButton")
	public void addMcButtonClicked(ClickEvent event)
	{
		this.hide();
		AdvancedSearchCriteria criteria = new AdvancedSearchCriteria();
		criteria.setPossibleFields(PossibleFields.MC);
		criteria.setComparison(comparison.getValue());
		criteria.setBindType(bindType.getValue());
		criteria.setValue(mcValue.getValue().getMcName());
		
		String shownValue = "";
		if (comparison.getValue().equals(Comparison.EQUALS))
			shownValue = constants.question() + " " + enumConstants.MC_EQUALS().toLowerCase() + " " + mcValue.getValue().getMcName();
		else if (comparison.getValue().equals(Comparison.NOT_EQUALS))
			shownValue = constants.question() + " " + enumConstants.MC_NOT_EQUALS().toLowerCase() + " " + mcValue.getValue().getMcName();
		
		criteria.setShownValue(shownValue);
			
		delegate.advancedSearchCriteriaClicked(criteria);
	}
	
	public void setProxyToMcListBox(List<McProxy> mcProxyList)
	{
		if (mcProxyList.size() > 0)
			mcValue.setValue(mcProxyList.get(0));
		
		mcValue.setAcceptableValues(mcProxyList);
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event)
	{
		this.hide();
	}

	@Override
	public boolean validateField() {
		// TODO Auto-generated method stub
		return false;
	}

}
