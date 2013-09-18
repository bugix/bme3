package medizin.client.ui.view.assignquestion;

import medizin.client.proxy.PersonProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface QuestionPanel extends IsWidget {
	
    void setDelegate(Delegate delegate);
 
    
	interface Delegate {

		void authorValueChanged(PersonProxy value);

		void searchQuestion(String questionshortName, Integer questionId,
				String questionType);

		void authorValueChangedFromRightSide(PersonProxy value);
		
	}

	void addQuestion(QuestionView question);

	void removeAll();

	VerticalPanel getQuestionDisplayPanel();

	void addAssesmentQuestion(AssesmentQuestionView assesmentQuestion);
	
	//public ValueListBox<PersonProxy> getAuthorListBox();

	
}
