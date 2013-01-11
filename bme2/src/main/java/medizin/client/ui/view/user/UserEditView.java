package medizin.client.ui.view.user;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.PersonProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public interface UserEditView extends IsWidget {

    void setDelegate(Delegate delegate);
    void setEditTitle(boolean edit);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

	RequestFactoryEditorDriver<PersonProxy, UserEditViewImpl> createEditorDriver();
}
