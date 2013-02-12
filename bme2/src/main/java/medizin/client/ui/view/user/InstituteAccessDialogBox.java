package medizin.client.ui.view.user;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.ui.view.user.EventAccessDialogbox.Delegate;
import medizin.client.ui.view.user.EventAccessDialogbox.Presenter;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;

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
			void addClicked(medizin.client.shared.AccessRights rights, InstitutionProxy questionEvent);

			void filterInstitutionChanged(String value);
			void filterEventChanged(String value);


		}

	    CellTable<InstitutionProxy> getTable();
	    String[] getPaths();
	    
	    void setDelegate(Delegate delegate);
		 
}
