package medizin.client.ui.view.user;

import medizin.client.proxy.PersonProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TabPanel;

public interface UserDetailsView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityAcceptAnswer);
    void setDelegate(Delegate delegate);
    void setValue(PersonProxy proxy);
    public EventAccessViewImpl getEventAccessView();


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void deleteClicked();

		void editClicked();
		

		void newClicked(String institutionName);
	}

	QuestionAccessViewImpl getQuestionAccessView();
	
	InstituteAccessViewImpl getInstituteAccessView();
	
	TabPanel getUserAccessDetailPanel();
	void setUserAccessDetailPanel(TabPanel userAccessDetailPanel);
}
