package medizin.client.ui.view.user;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.DoctorProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public interface UserEditView extends IsWidget {

    void setDelegate(Delegate delegate);
    
    void setValue(PersonProxy person);
    
    void disableAdminField(Boolean flag);
    
    public PersonProxy getProxy();
    
    public TextBox getName();
    
    public TextBox getPrename();
    
	public TextBox getEmail();

	public TextBox getAlternativEmail();

	public TextBox getPhoneNumber();

	public CheckBox getIsAdmin();
	
	public CheckBox getIsAccepted();

	public CheckBox getIsDoctor();
	
	public DefaultSuggestBox<DoctorProxy, EventHandlingValueHolderItem<DoctorProxy>> getDoctorSuggestBox();
    
    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

}
