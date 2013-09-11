package medizin.client.ui.view;

import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface SystemOverviewView extends IsWidget {
    
	public interface Delegate {
       
    }

	void setDelegate(Delegate delegate);

	void setAcceptQuestionAndAnswer(Long acceptQuestionCount, Long acceptAnswerCount);

	void setQuestionTypesCountByAssessment(String mcName, String closedDate, Map<String, String> quesitonTypeCountMap);

	void setQuestionTypesCountByAssessmentExaminer(String mcName, String closedDate, Map<String, String> quesitonTypeCountMap, SystemOverviewExaminerSubView examinerSubView);
	
	void addMainLabel();
	
	VerticalPanel getMainVerticalPanel();
}
