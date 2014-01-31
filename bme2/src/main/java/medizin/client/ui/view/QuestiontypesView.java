package medizin.client.ui.view;

import java.util.Set;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.AdvanceCellTable;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public interface QuestiontypesView extends IsWidget {


	    void setPresenter(Presenter activityInstitution);

	    public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {

			void newClicked();
			
			void performSearch(String searchValue);

			void setXandYOfTablePopyp(int x, int y);
		}

		AdvanceCellTable<QuestionTypeProxy> getTable();
	    String[] getPaths();
	    
	    void setDelegate(Delegate delegate);
	    
		SimplePanel getDetailsPanel();
		
		ScrollPanel getScrollDetailPanel();
    
}
