package medizin.client.ui.view.user;

import java.util.List;

import medizin.client.proxy.PersonProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.widget.Sorting;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public interface UserView extends IsWidget {
 

    void setPresenter(Presenter activityInstitution);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		
		void newClicked();

		void performSearch(String value);

		void columnClickedForSorting(String sortname, Sorting sortorder);
	}

	AdvanceCellTable<PersonProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
	SimplePanel getDetailsPanel();
	SplitLayoutPanel getSplitLayoutPanel();
	List<String> getColumnSortSet();
	
	ScrollPanel getScrollDetailPanel();	

}
