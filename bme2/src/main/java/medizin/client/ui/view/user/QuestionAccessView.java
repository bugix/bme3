package medizin.client.ui.view.user;


import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.UserAccessRightsProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionAccessView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityEvent);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteQuestionAccessClicked(UserAccessRightsProxy questionAccess);

		void addNewQuestionAccessClicked();
	}

    CellTable<UserAccessRightsProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    


}
