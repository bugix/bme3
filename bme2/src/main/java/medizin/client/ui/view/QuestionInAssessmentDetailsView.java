package medizin.client.ui.view;

import medizin.client.proxy.QuestionProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface QuestionInAssessmentDetailsView extends IsWidget {
	
	interface Delegate {
			
	}

	void setDelegate(Delegate delegate);
	 
	void setValue(QuestionProxy proxy);
	
	public VerticalPanel getAnswerVerticalPanel();
		
}
