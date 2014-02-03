package medizin.client.ui.view.assesment;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.ui.widget.Sorting;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public interface AssesmentView extends IsWidget {

	interface Delegate {
		void newClicked();
		void columnClickedForSorting(String sortname, Sorting sortorder);
	}
    
    CellTable<AssesmentProxy> getTable();
    String[] getPaths();
    void setDelegate(Delegate delegate);
	SimplePanel getDetailsPanel();
	ScrollPanel getScrollDetailPanel();	
}
