package medizin.client.ui.view.assesment;

import java.util.Collection;

import medizin.client.proxy.PersonProxy;

import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public interface QuestionTypeCountAddDialogbox extends IsWidget {
 

    void setPresenter(Presenter activityInstitution);
    void display();

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addClicked();
		void cancelClicked();
	}

 
    
    void setDelegate(Delegate delegate);
	void setQuestionTypesAssignedPickerValues(Collection<QuestionTypeProxy> values);
	RequestFactoryEditorDriver<QuestionTypeCountPerExamProxy, QuestionTypeCountAddDialogboxImpl> createEditorDriver();

    

}
