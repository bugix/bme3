package medizin.client.ui.view.assignquestion;

import com.google.gwt.user.client.ui.IsWidget;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;



public interface AnswerView extends IsWidget {


	   AnswerProxy getProxy();
	   
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void twistieOpenClicked(AnswerView answerView);
			void addClicked();
		}

	    void setDelegate(Delegate delegate);
		//void setProxy(AnswerToAssQuestionProxy answerToAssQuestionProxy);
		void setProxy(AnswerToAssQuestionProxy answerToAssQuestionProxy, boolean addCheck);
		void setProxy(AnswerProxy answer, boolean addCheck);
		boolean getChecked();
		
	    
}
