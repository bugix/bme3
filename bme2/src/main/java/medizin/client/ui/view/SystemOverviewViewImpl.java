package medizin.client.ui.view;

import java.util.Iterator;
import java.util.Map;

import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SystemOverviewViewImpl extends Composite implements  SystemOverviewView  {

	private static SystemOverviewViewImplUiBinder uiBinder = GWT
			.create(SystemOverviewViewImplUiBinder.class);

	interface SystemOverviewViewImplUiBinder extends
			UiBinder<Widget, SystemOverviewViewImpl> {
	}

	@UiField
	VerticalPanel mainVerticalPanel;
	
	private Delegate delegate;
	
	private BmeMessages messages = GWT.create(BmeMessages.class);
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	public SystemOverviewViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public void setAcceptQuestionAndAnswer(Long acceptQuestionCount, Long acceptAnswerCount)
	{
		Label label = new Label();
		label.setText(messages.acceptQuestionAndAnswer(acceptQuestionCount, acceptAnswerCount));
		label.addStyleName("acceptQueAnswerLabel");
		mainVerticalPanel.add(label);		
	}
	
	public void addMainLabel()
	{
		Label mainLabel = new Label();
		mainLabel.setText(constants.systemOverviewReachInMsg() + " : ");
		mainLabel.addStyleName("systemOverviewReachinLbl");
		mainVerticalPanel.add(mainLabel);
	}
	
	public void setQuestionTypesCountByAssessment(String mcName, String closedDate, Map<String, String> quesitonTypeCountMap)
	{
		SystemOverviewSubView subView = new SystemOverviewSubViewImpl();
		subView.getMcNameLbl().setText(mcName + " : ");
		subView.getClosedDateLbl().setText(constants.closedDate() + " : " + closedDate);
		
		Iterator<String> keyIterator = quesitonTypeCountMap.keySet().iterator();
		
		while (keyIterator.hasNext())
		{
			String key = keyIterator.next();
			String value = quesitonTypeCountMap.get(key);
			
			String questionTypeValue = value + " " + key + " " + constants.question() + " ";
			subView.getQuestionTypeVP().add(new Label(questionTypeValue));
		}
		
		mainVerticalPanel.add(subView);
	}
	
	public void setQuestionTypesCountByAssessmentExaminer(String mcName, String closedDate, Map<String, String> quesitonTypeCountMap, SystemOverviewExaminerSubView examinerSubView)
	{
		SystemOverviewSubView subView = new SystemOverviewSubViewImpl();
		subView.getMcNameLbl().setText(mcName + " : ");
		subView.getClosedDateLbl().setText(constants.closedDate() + " : " + closedDate);
		
		Iterator<String> keyIterator = quesitonTypeCountMap.keySet().iterator();
		
		while (keyIterator.hasNext())
		{
			String key = keyIterator.next();
			String value = quesitonTypeCountMap.get(key);
			
			String questionTypeValue = value + " " + key + " " + constants.question() + " ";
			subView.getQuestionTypeVP().add(new Label(questionTypeValue));
		}
		
		examinerSubView.getMcMsgLabel().setText(constants.systemOverviewReachInMsg());
		examinerSubView.getExaminerVerticalPanel().add(subView);
	}

	public VerticalPanel getMainVerticalPanel() {
		return mainVerticalPanel;
	}

	public void setMainVerticalPanel(VerticalPanel mainVerticalPanel) {
		this.mainVerticalPanel = mainVerticalPanel;
	}	
}
