package medizin.client.ui.view;

import medizin.client.proxy.InstitutionProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface InstitutionView extends IsWidget {
    /*void setName(String helloName);

    void setPresenter(Presenter activityInstitution);

    public interface Presenter {
        void goTo(Place place);
    }*/
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteClicked(InstitutionProxy institution);
		void editClicked(InstitutionProxy proxy, String instituionName);
		void newClicked(String institutionName);
		void performSearch(String value);
	}

    CellTable<InstitutionProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
	SimplePanel getDetailsPanel();


}
