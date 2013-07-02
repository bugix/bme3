package medizin.client.ui.view.assignquestion;

import com.google.gwt.dom.client.TableElement;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionProxy;



public interface AssesmentQuestionView extends IsWidget {
	
	   
	AssesmentQuestionProxy getProxy();
	   
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void twistieOpenAssQuestionClicked(AssesmentQuestionView questionView, boolean isInAssement);
			void addAssQuestionClicked();
			void forceAccept(AssesmentQuestionViewImpl assesmentQuestionViewImpl);
			void addToAssesmentButtonClicked(
					AssesmentQuestionViewImpl assesmentQuestionViewImpl);
			void deleteAssesmentQuestion(
					AssesmentQuestionViewImpl assesmentQuestionViewImpl);
		}

	    void setDelegate(Delegate delegate);
	    
	    void addAnswer(AnswerView answer);
	    void removeAnswer(AnswerView answer);
		Widget getDragControler();


		void setProxy(AssesmentQuestionProxy assesmentQuestion,
				boolean dellOrAdd);

		void setOpen();
		
		public Button getForceAcceptButton();
		
		public TableElement getQuestionTable();
		
		public Label getDeleteFromAssesment();

}
