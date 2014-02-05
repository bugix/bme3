package medizin.client.ui.dnd3.ui;



import medizin.client.proxy.QuestionEventProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Inteface for a draggable event.
 * @author masterthesis
 *
 */
public interface EventView extends IsWidget {
	
	
	
	public interface Delegate{
		
		public void openEventContainer(QuestionEventProxy questionEvent, EventViewImpl eventView);
	}

	
	void setDelegate(Delegate delegate);
	void setVerticalPanel(VerticalPanel vertPanel);
	void setEventProxy(QuestionEventProxy eventProxy);


}
