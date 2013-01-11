package medizin.client.ui.dnd3.ui;

import medizin.client.proxy.AssesmentQuestionProxy;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
/**
 * Interface for a draggable question
 * @author masterthesis
 *
 */
public interface QuestionView extends IsWidget {
	

	
	public interface Delegate{

		public void questionDropped(EntityProxyId<?> questionId);

		public void twistieOpenQuestionClicked(ClickHandler clickHandler);
		
	}

	VerticalPanel getPanel();
	Integer getOrderAversion();
	

	void setDelegate(Delegate delegate);
	void setVerticalPanel(VerticalPanel vertPanel);
	void setOrderAversion(Integer orderAversion);
	void setQuestionProxy(AssesmentQuestionProxy questionProxy);
	AssesmentQuestionProxy getQuestionProxy();
	

}
