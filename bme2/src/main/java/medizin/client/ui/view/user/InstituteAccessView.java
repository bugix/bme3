package medizin.client.ui.view.user;

import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.ui.view.user.EventAccessView.Delegate;
import medizin.client.ui.view.user.EventAccessView.Presenter;
import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;

public interface InstituteAccessView {

	void setName(String helloName);

    void setPresenter(Presenter activityEvent);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteInstituteAccessClicked(UserAccessRightsProxy event);

		void addNewInstituteAccessClicked();
	}

    CellTable<UserAccessRightsProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);

	ApplicationLoadingView getLoadingPopup();
}
