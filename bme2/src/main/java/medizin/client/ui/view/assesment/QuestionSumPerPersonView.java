package medizin.client.ui.view.assesment;

import medizin.client.proxy.QuestionAccessProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;


import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionSumPerPersonView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityEvent);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteQuestionSumPerPersonClicked(QuestionSumPerPersonProxy questionSumPerPerson);
		//void editQuestionSumPerPersonClicked();
		void addNewQuestionSumPerPersonClicked();
		void moveQuestionSumPerPersonDown(QuestionSumPerPersonProxy QuestionSumPerPerson);
		void moveQuestionSumPerPersonUp(QuestionSumPerPersonProxy QuestionSumPerPerson);
	}

    CellTable<QuestionSumPerPersonProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    


}
