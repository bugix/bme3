package medizin.client.ui.view.assignquestion;

import medizin.client.ui.view.assesment.AssesmentDetailsView.Delegate;
import medizin.client.ui.view.assesment.AssesmentDetailsView.Presenter;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface QuestionPanel extends IsWidget {
	
    void setDelegate(Delegate delegate);
 
    
	interface Delegate {

		void authorValueChanged(PersonProxy value);
		
	}

	void addQuestion(QuestionView question);

	void removeAll();

	VerticalPanel getQuestionDisplayPanel();

	void addAssesmentQuestion(AssesmentQuestionView assesmentQuestion);
	
	public ValueListBox<PersonProxy> getAuthorListBox();

	
}
