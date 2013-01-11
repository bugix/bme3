package medizin.client.ui.dnd3.ui;

import medizin.client.proxy.QuestionTypeCountPerExamProxy;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Interface for draggable QuestionTypePerExam (Kombination von Fragetypen)
 * @author masterthesis
 *
 */
public interface QuestionTypeDNDView extends IsWidget {
	
	 void setDelegate(Delegate delegate);
	
	 public interface Delegate{

		 public void downInOrderClicked(QuestionTypeCountPerExamProxy proxy);

		 public void onUpInOrderClicked(QuestionTypeCountPerExamProxy proxyUp);

		

		
	}

	void setProxy(QuestionTypeCountPerExamProxy proxy);

	AbsolutePanel getQuestionsContainer();

	VerticalPanel getEventsContainer();

	AbsolutePanel getQuestionTypeContent();
	

	

}
