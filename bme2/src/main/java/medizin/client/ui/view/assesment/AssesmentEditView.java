package medizin.client.ui.view.assesment;

import java.util.Collection;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public interface AssesmentEditView extends IsWidget {
    void setName(String helloName);

    void setPresenter(Presenter activityAcceptAnswer);
    void setDelegate(Delegate delegate);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

	RequestFactoryEditorDriver<AssesmentProxy, AssesmentEditViewImpl> createEditorDriver();

	void setRepeForPickerValues(Collection<AssesmentProxy> values);

	void setMcPickerValues(Collection<McProxy> values);

	void setEditTitle(boolean edit);
	


	void setInstitutionPickerValues(Collection<InstitutionProxy> values);

	void disableInstituteField();

	void setInstitutionValue(InstitutionProxy institutionProxy);
}
