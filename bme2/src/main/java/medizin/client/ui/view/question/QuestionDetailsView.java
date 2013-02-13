package medizin.client.ui.view.question;

import java.util.Set;

import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;

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

		void deleteSelectedQuestionResource(Long qestionResourceId);

		void addNewQuestionResource(QuestionResourceClient questionResourceClient);

		void updatePicturePathInQuestion(String picturePath);

		void deleteUploadedPicture(String picturePath);

		void deleteUploadedFiles(Set<String> paths);

		void changedResourceSequence(Set<QuestionResourceClient> questionResourceClients);
	}

	AnswerListViewImpl getAnswerListViewImpl();

	

}
