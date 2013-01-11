package medizin.client.ui.view.assesment;

import medizin.client.proxy.QuestionAccessProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.server.domain.QuestionTypeCountPerExam;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionTypeCountView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityEvent);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteQuestionTypeCountClicked(QuestionTypeCountPerExamProxy questionTypeCountPerExam);
		//void editQuestionTypeCountClicked();
		void addNewQuestionTypeCountClicked();
		void moveDown(QuestionTypeCountPerExamProxy questionTypeCount);
		void moveUp(QuestionTypeCountPerExamProxy questionTypeCount);
	}

    CellTable<QuestionTypeCountPerExamProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    


}
