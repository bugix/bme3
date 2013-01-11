package medizin.client.ui.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AcceptPersonView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityAcceptPerson);

    public interface Presenter {
        void goTo(Place place);
    }

}
