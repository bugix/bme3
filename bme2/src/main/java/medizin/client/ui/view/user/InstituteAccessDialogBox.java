package medizin.client.ui.view.user;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface InstituteAccessDialogBox extends IsWidget {
	 	
	   void setPresenter(Presenter activityInstitution);
	   void display();

	    public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void addClicked(medizin.shared.AccessRights rights, InstitutionProxy questionEvent);
			void filterInstituteChanged(String text);
		}

	    CellTable<InstitutionProxy> getTable();
	    String[] getPaths();
	    
	    void setDelegate(Delegate delegate);
		ApplicationLoadingView getLoadingPopup();
		 
}
