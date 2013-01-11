package medizin.client.ui.dnd3.ui;



import medizin.client.proxy.QuestionEventProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
/**
 * Inteface for a draggable event.
 * @author masterthesis
 *
 */
public interface EventView extends IsWidget {
	
	
	
	public interface Delegate{

		public void questionDropped(EntityProxyId<?> questionId);
		
	}

	
	void setDelegate(Delegate delegate);
	void setVerticalPanel(VerticalPanel vertPanel);
	void setEventProxy(QuestionEventProxy eventProxy);


}
