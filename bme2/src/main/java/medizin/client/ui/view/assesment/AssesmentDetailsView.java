package medizin.client.ui.view.assesment;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AssesmentDetailsView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityAcceptAnswer);
    void setDelegate(Delegate delegate);
    void setValue(AssesmentProxy proxy);
   // public EventAccessViewImpl getEventAccessView();


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void deleteClicked();

		void editClicked();
		

		void newClicked(String institutionName);
	}

	QuestionTypeCountViewImpl getQuestionTypeCountViewImpl();

	QuestionSumPerPersonViewImpl getQuestionSumPerPersonViewImpl();

	StudentViewImpl getStudentViewImpl();
	//QuestionAccessViewImpl getQuestionAccessView();
}
