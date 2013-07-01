package medizin.client.ui.view.assignquestion;

import java.util.List;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.ui.view.assignquestion.QuestionPanel.Delegate;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface AssesmentQuestionPanel extends IsWidget {
    void setDelegate(Delegate delegate);
 
    
	interface Delegate {

		void authorValueChanged(PersonProxy value);

		void loadTemplate();

		void sendMail(String messageContent);
		
	}

	void addAssesmentQuestion(AssesmentQuestionView question);

	void removeAll();

	VerticalPanel getAssesmentQuestionDisplayPanel();
	
	public ValueListBox<PersonProxy> getAuthorListBox();
	
	public Button getSendMail();
	
	public SendMailPopupViewImpl getSendMailPopupViewImpl();
	
	public List<QuestionTypeCountPerExamProxy> getQuestionTypeCountPerExams();

	public void setQuestionTypeCountPerExams(
			List<QuestionTypeCountPerExamProxy> questionTypeCountPerExams) ;
	
	public VerticalPanel getQuestionTypeVP() ;
}
