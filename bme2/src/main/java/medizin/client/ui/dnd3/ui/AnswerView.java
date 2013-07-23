package medizin.client.ui.dnd3.ui;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
/**
 * Inteface for view for answers  used for the drag and drop features of assessment book.
 *
 * @author masterthesis
 *
 */
public interface AnswerView extends IsWidget {

	
	public interface Delegate{

		public void answerDropped(EntityProxyId<?> answerId);
		
	}

	public void setDelegate(Delegate delegate);
	public void setProxy(AnswerProxy answer);
	public HorizontalPanel getLblAnswerText();
	public AnswerToAssQuestionProxy getAnswerToAssQueston();
	public void setAnswerToAssQueston(AnswerToAssQuestionProxy answerToAssQueston);


}
