package medizin.client.ui.view.assignquestion;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionProxy;



public interface QuestionView extends IsWidget {
	
	   void setProxy(QuestionProxy question);
	   QuestionProxy getProxy();
	   
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void twistieOpenQuestionClicked(QuestionView questionView);
			void addQuestionClicked();
			void addNewQuestionToAssesment(QuestionViewImpl questionViewImpl);
			void questionTabOpened(Long assesmentId, Long questionId, QuestionViewImpl questionViewImpl);
		}

	    void setDelegate(Delegate delegate);
	    
	    void addAnswer(AnswerView answer);
	    void removeAnswer(AnswerView answer);
		Widget getDragControler();
		VerticalPanel getAnswerPanel();
		void setLastUse(AssesmentQuestionProxy response);
		void setAssesment(AssesmentProxy assesment);

}
