package medizin.client.ui.view;

import medizin.client.proxy.QuestionEventProxy;
import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface EventView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityEvent);

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteClicked(QuestionEventProxy event);
		void editClicked(QuestionEventProxy proxy, String questionEventName);
		void newClicked(String eventName);
	}

    CellTable<QuestionEventProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);

	ApplicationLoadingView getLoadingPopup();
    


}
