package medizin.client.ui.view.assesment;

import java.util.Collection;

import medizin.client.proxy.PersonProxy;

import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public interface QuestionSumPerPersonDialogbox extends IsWidget {
 

    void setPresenter(Presenter activityInstitution);
    void display();

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addQuestionSumPerPersonClicked();
		void cancelQuestionSumPerPersonClicked();
	}

 
    
    void setDelegate(Delegate delegate);

	RequestFactoryEditorDriver<QuestionSumPerPersonProxy, QuestionSumPerPersonDialogboxImpl> createEditorDriver();
	void setQuestionEventPickerValues(Collection<QuestionEventProxy> values);
	void setResponsiblePersonPickerValues(Collection<PersonProxy> values);

    

}
