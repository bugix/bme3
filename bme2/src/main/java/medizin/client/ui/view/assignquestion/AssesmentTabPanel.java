package medizin.client.ui.view.assignquestion;

import medizin.client.ui.view.assesment.AssesmentDetailsView.Delegate;
import medizin.client.ui.view.assesment.AssesmentDetailsView.Presenter;
import medizin.client.proxy.AssesmentProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AssesmentTabPanel extends IsWidget {
	
    void setDelegate(Delegate delegate);
    void addAssementTab(AssesmentProxy assesment);
 
    
	interface Delegate {

		void tabClicked(AssesmentProxy assesment);
	}


	AssesmentProxy getActiveTab();
	void setSelectedTab(int i);


}
