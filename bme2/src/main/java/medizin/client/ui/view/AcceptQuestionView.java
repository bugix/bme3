package medizin.client.ui.view;

import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.widget.Sorting;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface AcceptQuestionView extends IsWidget {


    void setPresenter(Presenter activityAcceptQuestion);

    public interface Presenter {
        void goTo(Place place);
    }

    interface Delegate {

		void columnClickedForSorting(String sortname, Sorting sortorder);
		
		
	}

	
	   CellTable<QuestionProxy> getTable();
	    
	    String[] getPaths();
		
	    void setDelegate(Delegate delegate);

	    
		public SimplePanel getDetailsPanel();

}
