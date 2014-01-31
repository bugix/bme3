package medizin.client.ui.view.assesment;

import java.util.List;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

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
		//void addQuestionSumPerPersonClicked();
		void cancelQuestionSumPerPersonClicked();
		void addQuestionSumPerPersonClicked(QuestionSumPerPersonDialogboxImpl questionSumPerPersonDialogboxImpl);
	}

 
    
    void setDelegate(Delegate delegate);
	void setResponsiblePersonValues(List<PersonProxy> values);
	void setQuestionEventValues(List<QuestionEventProxy> values);
	void setValueInProxy(QuestionSumPerPersonProxy questionSumPerPersonProxy);
	ApplicationLoadingView getLoadingPopup();

	/*RequestFactoryEditorDriver<QuestionSumPerPersonProxy, QuestionSumPerPersonDialogboxImpl> createEditorDriver();
	void setQuestionEventPickerValues(Collection<QuestionEventProxy> values);
	void setResponsiblePersonPickerValues(Collection<PersonProxy> values);*/

    

}
