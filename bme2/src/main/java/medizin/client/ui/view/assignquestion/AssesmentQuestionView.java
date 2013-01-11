package medizin.client.ui.view.assignquestion;

import com.google.gwt.user.client.ui.IsWidget;
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
		}

	    void setDelegate(Delegate delegate);
	    
	    void addAnswer(AnswerView answer);
	    void removeAnswer(AnswerView answer);
		Widget getDragControler();


		void setProxy(AssesmentQuestionProxy assesmentQuestion,
				boolean dellOrAdd);

		void setOpen();

}
