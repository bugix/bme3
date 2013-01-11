package medizin.client.ui.view.question;

import medizin.client.ui.view.assesment.QuestionSumPerPersonViewImpl;
import medizin.client.ui.view.assesment.QuestionTypeCountViewImpl;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionDetailsView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityQuestionDetails);
    void setDelegate(Delegate delegate);
    void setValue(QuestionProxy proxy);
   // public EventAccessViewImpl getEventAccessView();


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void deleteClicked();

		void editClicked();
		

		void newClicked();
	}

	AnswerListViewImpl getAnswerListViewImpl();

	

}
