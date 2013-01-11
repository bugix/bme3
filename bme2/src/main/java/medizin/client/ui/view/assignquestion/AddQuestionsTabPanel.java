package medizin.client.ui.view.assignquestion;

import medizin.client.ui.view.assesment.AssesmentDetailsView.Delegate;
import medizin.client.ui.view.assesment.AssesmentDetailsView.Presenter;
import medizin.client.proxy.AssesmentProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AddQuestionsTabPanel extends IsWidget {
	
    void setDelegate(Delegate delegate);
 
    
	interface Delegate {

		void tabQuestionClicked(int index);
	}


	int getActiveTab();


}
