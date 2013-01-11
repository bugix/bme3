package medizin.client.ui.view;

import medizin.client.ui.view.SystemOverviewView.Delegate;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface SystemOverviewView extends IsWidget {
    void setName(String helloName);
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }
    
    public interface Delegate {
        void buttonClicked();
    }

	void setDelegate(Delegate delegate);

}
