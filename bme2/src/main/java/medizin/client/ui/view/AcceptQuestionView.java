package medizin.client.ui.view;

import medizin.client.ui.DeclineEmailPopupDelagate;
import medizin.client.ui.view.question.QuestionView.Delegate;
import medizin.client.proxy.QuestionProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface AcceptQuestionView extends IsWidget {


    void setPresenter(Presenter activityAcceptQuestion);

    public interface Presenter {
        void goTo(Place place);
    }


	
	   CellTable<QuestionProxy> getTable();
	    
	    String[] getPaths();
		void setDelegate(DeclineEmailPopupDelagate delegate);
	    


}
