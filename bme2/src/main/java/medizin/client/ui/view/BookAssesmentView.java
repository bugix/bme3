package medizin.client.ui.view;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface BookAssesmentView extends IsWidget {
    void setPresenter(Presenter activityBookAssesment);
    void setDelegate(Delegate delegate);
    
    public interface Delegate {

		void yearSelected(String selectedYear);

    }
    
    public interface Presenter {
        void goTo(Place place);
    }


	public void createTab(String name, EntityProxyId<?> stableId);
	public AcceptsOneWidget getDetailsPanel();
	void addTabHandler();
    
}
